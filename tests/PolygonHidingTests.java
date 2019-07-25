package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import renderer.Pipeline;
import renderer.Scene.Polygon;

/**
 * @author tony
 */
public class PolygonHidingTests {
	  @Test
	  /** A polygon facing you. */
	  public void testShouldShow1() {
		  float[] verts = new float[]{10,5,5,2,3,2,9,5,4};
		  int[] col = new int[]{0,0,0};
		  
		  Polygon poly = new Polygon(verts, col);
		  assertFalse(Pipeline.isHidden(poly));
	  }
	  
	  @Test
	  /** A polygon with its back to you. */
	  public void testShouldShow2() {
		  float[] verts = new float[]{-2,6,3,1,5,7,3,6,1};
		  int[] col = new int[]{0,0,0};
		  
		  Polygon poly = new Polygon(verts, col);
		  assertTrue(Pipeline.isHidden(poly));
	  }
	  
	  @Test
	  /** A polygon with its back to you. */
	  public void testShouldHide1() {
		  float[] verts = new float[]{1,1,0,2,1,0,1,2,0};
		  int[] col = new int[]{0,0,0};
		  
		  Polygon poly = new Polygon(verts, col);
		  assertTrue(Pipeline.isHidden(poly));
	  }
	  
	  @Test
	  /** A polygon with its back to you. */
	  public void testShouldHide2() {
		  float[] verts = new float[]{2,6,3,1,5,7,3,6,1};
		  int[] col = new int[]{0,0,0};
		  
		  Polygon poly = new Polygon(verts, col);
		  assertTrue(Pipeline.isHidden(poly));
	  }

}

//code for COMP261 assignments