package renderer;

/**
 * EdgeList should store the data for the edge list of a single polygon in your
 * scene. A few method stubs have been provided so that it can be tested, but
 * you'll need to fill in all the details.
 *
 * You'll probably want to add some setters as well as getters or, for example,
 * an addRow(y, xLeft, xRight, zLeft, zRight) method.
 */
public class EdgeList {
  int startY;
  int endY;
  int leftX = 0;
  int rightX = 3;
  int leftZ = 1;
  int rightZ = 2;
  float[][] values;
  int size;

  public EdgeList(int startY, int endY) {
    this.startY = startY;
    this.endY = endY;

    this.size = (Math.abs(endY - startY + 1));
    this.values = new float[size][4];
  }

  public int getStartY() {
    return this.startY;
  }

  public int getEndY() {
    return this.endY;
  }

  // ------- X
  public void setLeftX(int y, float x) {
    values[y - startY][leftX] = x;
  }

  public float getLeftX(int y) {
    return values[y - startY][leftX];
  }

  public float getRightX(int y) {
    return values[y - startY][rightX];
  }

  public void setRightX(int y, float x) {
    values[y - startY][rightX] = x;
  }

  // ------- Z
  public float getLeftZ(int y) {
    return values[y - startY][leftZ];
  }

  public float getRightZ(int y) {
    return values[y - startY][rightZ];
  }

  public void setLeftZ(int y, float z) {
    values[y - startY][leftZ] = z;
  }

  public void setRightZ(int y, float z) {
    values[y - startY][rightZ] = z;
  }
}

// code for comp261 assignments
