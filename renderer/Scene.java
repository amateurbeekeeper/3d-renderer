package renderer;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import renderer.Scene.Polygon;

/**
 * The Scene class is where we store data about a 3D model and light source
 * inside our renderer. It also contains a static inner class that represents
 * one single polygon.
 * 
 * Method stubs have been provided, but you'll need to fill them in.
 * 
 * If you were to implement more fancy rendering, e.g. Phong shading, you'd want
 * to store more information in this class.
 */
public class Scene {
  List<Polygon> polygons;
  Vector3D lights;

  float width = 0.0f;
  float height = 0.0f;
  float minY = Float.MAX_VALUE;
  float maxY = -Float.MAX_VALUE;
  float minX = Float.MAX_VALUE;
  float maxX = -Float.MAX_VALUE;

  public Scene(List<Polygon> polygons, Vector3D lightPos) {
    this.polygons = new ArrayList<Polygon>(polygons);
    this.lights = lightPos;

    // UPDATE SCALING VALUES
    update();
  }

  @Override
  public String toString() {
    return "Scene [polygons=" + polygons + ", lights=" + lights + ", width=" + width + ", height=" + height + ", minY="
        + minY + ", maxY=" + maxY + ", minX=" + minX + ", maxX=" + maxX + "]";
  }

  /**
   * Update the min/ max x & y of the scene (used when scaling)
   * 
   */
  public void update() {
    for (Polygon poly : this.polygons) {
      Vector3D[] vectors = Arrays.copyOf(poly.getVertices(), 3);

      for (Vector3D v : vectors) {
        // ROUNDING
        float vy = Math.round(v.y);
        float vx = Math.round(v.x);

        this.minY = (this.minY < vy) ? this.minY : vy;
        this.maxY = (this.maxY > vy) ? this.maxY : vy;

        this.minX = (this.minX < vx) ? this.minX : vx;
        this.maxX = (this.maxX > vx) ? this.maxX : vx;
      }
    }

    this.width = Math.round(maxX - minX);
    this.height = Math.round(maxY - minY);
  }

  public Vector3D getLights() {
    update();
    return lights;
  }

  public float getWidth() {
    update();
    return width;
  }

  public float getHeight() {
    update();
    return height;
  }

  public float getMinY() {
    update();
    return minY;
  }

  public float getMaxY() {
    update();
    return maxY;
  }

  public float getMinX() {
    update();
    return minX;
  }

  public float getMaxX() {
    update();
    return maxX;
  }

  public void setPolygons(List<Polygon> polygons) {
    this.polygons = polygons;
  }

  public void setLights(Vector3D lights) {
    this.lights = lights;
  }

  public void setWidth(float width) {
    this.width = width;
  }

  public void setHeight(float height) {
    this.height = height;
  }

  public void setMinY(float minY) {
    this.minY = minY;
  }

  public void setMaxY(float maxY) {
    this.maxY = maxY;
  }

  public void setMinX(float minX) {
    this.minX = minX;
  }

  public void setMaxX(float maxX) {
    this.maxX = maxX;
  }

  public Vector3D getLight() {
    return this.lights;
  }

  public List<Polygon> getPolygons() {
    return this.polygons;
  }

  /**
   * Polygon stores data about a single polygon in a scene, keeping track of (at
   * least!) its three vertices and its reflectance.
   *
   * This class has been done for you.
   */
  public static class Polygon {
    Vector3D[] vertices;
    Color reflectance;

    /**
     * @param points
     *          An array of floats with 9 elements, corresponding to the (x,y,z)
     *          coordinates of the three vertices that make up this polygon. If the
     *          three vertices are A, B, C then the array should be [A_x, A_y, A_z,
     *          B_x, B_y, B_z, C_x, C_y, C_z].
     * @param color
     *          An array of three ints corresponding to the RGB values of the
     *          polygon, i.e. [r, g, b] where all values are between 0 and 255.
     */
    public Polygon(float[] points, int[] color) {
      this.vertices = new Vector3D[3];

      float x, y, z;
      for (int i = 0; i < 3; i++) {
        x = points[i * 3];
        y = points[i * 3 + 1];
        z = points[i * 3 + 2];
        this.vertices[i] = new Vector3D(x, y, z);
      }

      int r = color[0];
      int g = color[1];
      int b = color[2];
      this.reflectance = new Color(r, g, b);
    }

    /**
     * An alternative constructor that directly takes three Vector3D objects and a
     * Color object.
     */
    public Polygon(Vector3D a, Vector3D b, Vector3D c, Color color) {
      this.vertices = new Vector3D[] { a, b, c };
      this.reflectance = color;
    }

    public Vector3D[] getVertices() {
      return vertices;
    }

    public Color getReflectance() {
      return reflectance;
    }

    @Override
    public String toString() {
      String str = "polygon:";

      for (Vector3D p : vertices)
        str += "\n  " + p.toString();

      str += "\n  " + reflectance.toString();

      return str;
    }
  }
}

// code for COMP261 assignments
