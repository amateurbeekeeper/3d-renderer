package renderer;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.JSlider;

import renderer.Scene.Polygon;

public class Renderer extends GUI {
  public Scene scene;
  public Color directionalLight = Color.WHITE;
  public Color ambientLight;

  File monkeyFile = new File("../3D RENDER/code/data/monkey.txt");

  @Override
  protected void onLoad(File file) {
    Reader r = new Reader(file);
    this.scene = new Scene(r.getPolygons(), r.getLightPos());
  }

  public void quickLoad(File file) {
    Reader r = new Reader(file);
    this.scene = new Scene(r.getPolygons(), r.getLightPos());
  }

  @Override
  protected void onKeyPress(KeyEvent ev) {
    if (this.scene == null)
      quickLoad(monkeyFile);

    if (ev.getKeyCode() == KeyEvent.VK_LEFT) {
      this.scene = Pipeline.rotateScene(scene, 0.0f, (float) (-0.1 * 0.33));
    } else if (ev.getKeyCode() == KeyEvent.VK_RIGHT) {
      this.scene = Pipeline.rotateScene(scene, 0.0f, (float) (0.1 * 0.33));
    } else if (ev.getKeyCode() == KeyEvent.VK_UP) {
      this.scene = Pipeline.rotateScene(scene, (float) (0.1 * 0.33), 0.0f);
    } else if (ev.getKeyCode() == KeyEvent.VK_DOWN) {
      this.scene = Pipeline.rotateScene(scene, (float) (-0.1 * 0.33), 0.0f);
    }
  }

  @Override
  protected BufferedImage render() {
    Color[][] zbuffer = new Color[CANVAS_WIDTH][CANVAS_HEIGHT];
    float[][] zdepth = new float[CANVAS_WIDTH][CANVAS_HEIGHT];

    for (int i = 0; i < CANVAS_WIDTH; i++) {
      for (int j = 0; j < CANVAS_HEIGHT; j++) {
        zdepth[i][j] = (int) Integer.MAX_VALUE;
        zbuffer[i][j] = Color.GRAY;
      }
    }

    if (this.scene == null)
      return convertBitmapToImage(zbuffer);

    this.scene = Pipeline.translateScene(scene);
    this.scene = Pipeline.scaleScene(scene);
    this.ambientLight = new Color(getAmbientLight()[0], getAmbientLight()[1], getAmbientLight()[2]);

    for (Polygon p : this.scene.getPolygons()) {
      // IF HIDDEN SKIP
      if (Pipeline.isHidden(p))
        continue;

      // GET COLOR OF POLYGON
      Color col = Pipeline.getShading(p, scene.lights, directionalLight, ambientLight);

      // COMPUTE EDGE LIST : Z & X
      EdgeList edgeList = Pipeline.computeEdgeList(p);

      // COMPUTE Z BUFFER
      Pipeline.computeZBuffer(zbuffer, zdepth, edgeList, col);
    }

    return convertBitmapToImage(zbuffer);
  }

  /**
   * Converts a 2D array of Colors to a BufferedImage. Assumes that bitmap is
   * indexed by column then row and has imageHeight rows and imageWidth columns.
   * Note that image.setRGB requires x (col) and y (row) are given in that order.
   */
  private BufferedImage convertBitmapToImage(Color[][] bitmap) {
    BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < CANVAS_WIDTH; x++) {
      for (int y = 0; y < CANVAS_HEIGHT; y++) {
        image.setRGB(x, y, bitmap[x][y].getRGB());
      }
    }
    return image;
  }

  public static void main(String[] args) {
    new Renderer();
  }
}

// code for comp261 assignments
