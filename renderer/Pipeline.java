package renderer;

import java.awt.Color;
import renderer.Scene.Polygon;

public class Pipeline {
  public static boolean isHidden(Polygon poly) {
    Vector3D[] v = poly.getVertices();
    Vector3D normal = v[1].minus(v[0]).crossProduct(v[2].minus(v[1]));

    if (normal.z < 0)
      return false;

    return true;
  }

  public static Color getShading(Polygon poly, Vector3D lightDirection, Color lightColor, Color ambientLight) {
    Vector3D[] v = poly.getVertices();
    int[] shadingRGB = new int[] { 0, 0, 0 };

    // NORMAL
    Vector3D normal = v[1].minus(v[0]).crossProduct(v[2].minus(v[1]));

    // COS
    Float cos = lightDirection.cosTheta(normal);
    if (cos < 0)
      cos = 0f; // FIX: ??

    // REFLECTANCE
    Color ref = poly.getReflectance();

    // INDIVID. RGB
    for (int i = 0; i <= 2; i++) {
      float al = ambientLight.getRGBColorComponents(null)[i];
      float r = ref.getRGBColorComponents(null)[i];
      float Il = lightColor.getRGBColorComponents(null)[i];

      int result = (int) (((al * r) + (Il * r) * cos) * 255f);

      if (result > 255)
        result = 255;
      else if (result < 0)
        result = 0;

      shadingRGB[i] = result;
    }

    return new Color(shadingRGB[0], shadingRGB[1], shadingRGB[2]);
  }

  public static EdgeList computeEdgeList(Polygon poly) {
    Vector3D[] v = poly.getVertices();
    int startY = Math.round(v[0].y);
    int endY = Math.round(v[1].y);
    Vector3D a;
    Vector3D b;

    // FIND MAX (endY) AND MIN (startY) OF VERTICES
    for (int i = 0; i < v.length; i++) {
      if (v[i].y > endY)
        endY = Math.round(v[i].y);
      else if (v[i].y < startY)
        startY = Math.round(v[i].y);
    }

    // ROUNDING FOR GLITCHES/ HOLES
    startY = Math.round(startY);
    endY = Math.round(endY);

    EdgeList edgeList = new EdgeList(startY, endY);

    for (int i = 0; i < v.length; i++) {
      a = v[i];
      b = v[(v.length == i + 1 ? 0 : i + 1)];

      // ROUNDING FOR GLITCHES/ HOLES
      float bx = Math.round(b.x);
      float ax = Math.round(a.x);
      float ay = Math.round(a.y);
      float by = Math.round(b.y);
      float az = Math.round(a.z);
      float bz = Math.round(b.z);

      // ROUNDING FOR GLITCHES/ HOLES
      float slopeX = ((bx - ax) / (by - ay));
      float slopeZ = ((bz - az) / (by - ay));
      float x = Math.round(ax);
      float z = Math.round(az);
      int y = Math.round(a.y);

      // ROUNDING FOR GLITCHES/ HOLES
      if (ay < by) {
        while (y <= by) {
          edgeList.setLeftZ(y, z);
          edgeList.setLeftX(y, x);
          x += slopeX;
          z += slopeZ;
          y++;
        }
      } else {
        while (y >= by) {

          edgeList.setRightZ(y, z);
          edgeList.setRightX(y, x);
          x -= slopeX;
          z -= slopeZ;
          y--;

        }
      }
    }

    return edgeList;
  }

  public static void computeZBuffer(Color[][] zbuffer, float[][] zdepth, EdgeList el, Color col) {
    for (int y = el.startY; y < el.endY; y++) {
      float slope = (el.getRightZ(y) - el.getLeftZ(y)) / (el.getRightX(y) - el.getLeftX(y));
      int x = Math.round(el.getLeftX(y));
      float z = el.getLeftZ(y) + slope * (x - el.getLeftX(y));

      // if (x >= 0 && x < zbuffer.length && y >= 0 && x >= 0 && y < GUI.CANVAS_HEIGHT
      // && x < GUI.CANVAS_WIDTH) {
      while (x <= Math.round(el.getRightX(y)) - 1 && y >= 0 && x >= 0 && y < GUI.CANVAS_HEIGHT
          && x < GUI.CANVAS_WIDTH) {

        // IF INFRONT
        if (z < zdepth[x][y]) {
          zbuffer[x][y] = col;
          zdepth[x][y] = z;
        }
        z = z + slope;
        x++;
      }
    }
    // }
  }

  // ---------------------------------------------- COMPLETION
  // --------------------------------------------------------

  public static Scene rotateScene(Scene s, float x, float y) {
    // TRANSFORMATION OPERATOR(S)
    Transform xx = Transform.newXRotation(x);
    Transform yy = Transform.newYRotation(y);

    // APPLY TRANSFORAMTION OPERATOR TO MATRIX
    for (Scene.Polygon p : s.getPolygons()) {
      for (int i = 0; i < p.getVertices().length; i++) {
        if (x != 0.0f)
          p.getVertices()[i] = xx.multiply(p.getVertices()[i]);
        if (y != 0.0f)
          p.getVertices()[i] = yy.multiply(p.getVertices()[i]);
      }
    }

    return new Scene(s.getPolygons(), s.lights);
  }

  public static Scene scaleScene(Scene s) {
    float scaleFactor = 1;

    // IF NOT WITHIN BUFFER: CHANGE SCALE FACTOR TO FIT
    if (s.getWidth() - GUI.CANVAS_WIDTH > s.getHeight() - GUI.CANVAS_HEIGHT) {
      if (s.getWidth() > GUI.CANVAS_WIDTH)
        scaleFactor = GUI.CANVAS_WIDTH / s.getWidth();
      else if (s.getHeight() > GUI.CANVAS_HEIGHT)
        scaleFactor = GUI.CANVAS_HEIGHT / s.getHeight();
    }

    // TRANSFORMATION OPERATOR
    Transform t = Transform.newScale(new Vector3D(scaleFactor, scaleFactor, scaleFactor));

    // APPLY TRANSFORAMTION OPERATOR TO MATRIX
    for (Scene.Polygon p : s.getPolygons()) {
      for (int i = 0; i < p.getVertices().length; i++) {
        p.getVertices()[i] = t.multiply(p.getVertices()[i]);
      }
    }

    return new Scene(s.getPolygons(), s.lights);
  }

  public static Scene translateScene(Scene s) {
    // TRANSFORMATION OPERATOR
    Transform t = Transform.newTranslation(new Vector3D(-s.getMinX(), -s.getMinY(), 0.0f));

    // APPLY TRANSFORAMTION OPERATOR TO MATRIX
    for (Polygon p : s.getPolygons()) {
      for (int i = 0; i < p.getVertices().length; i++)
        p.getVertices()[i] = t.multiply(p.getVertices()[i]);
    }

    return new Scene(s.getPolygons(), s.lights);
  }
}

// code for comp261 assignments
