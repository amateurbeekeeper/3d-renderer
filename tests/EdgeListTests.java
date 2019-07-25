package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import renderer.EdgeList;
import renderer.Pipeline;
import renderer.Scene.Polygon;

/**
 * @author tony
 */
public class EdgeListTests {

	@Test
	/**
	 * Just check the startY and endY are stored properly in the EdgeList class.
	 */
	public void testStartAndEnd() {
		EdgeList el = new EdgeList(20, 50);
		assertEquals(20, el.getStartY());
		assertEquals(50, el.getEndY());
	}

	@Test
	/**
	 * This tests the interpolation of X with a simple triangle. The most useful
	 * part of this test is that it checks whether you've correctly updated the
	 * very corners of the polygon.
	 */
	public void testNiceXInterpolation() {
		float[] verts = new float[] { 0, 0, 0, 0, 10, 0, 10, 0, 0 };
		int[] col = new int[] { 0, 0, 0 };
		Polygon poly = new Polygon(verts, col);

		EdgeList el = Pipeline.computeEdgeList(poly);
		
		// half-way up the triangle
		assertEquals(0, el.getLeftX(5), 1e-5);
		assertEquals(5, el.getRightX(5), 1e-5);

		// the bottom of the triangle
		assertEquals(0, el.getLeftX(0), 1e-5);
		assertEquals(10, el.getRightX(0), 1e-5);

		// the top of the triangle
		assertEquals(0, el.getLeftX(10), 1e-5);
		assertEquals(0, el.getRightX(10), 1e-5);
		
		// READ THIS.
		// You can get away with this test failing. I think it's common that
		// either this test will fail, or you'll get 'whiskers' (little
		// horizontal lines) in your renderer. This means you've got an
		// off-by-one error somewhere in your edgelist code, and so you'll
		// either have pinprick holes at the vertices or whiskers.
		//
		// It's nice to fix this issue, but we don't mark you down for it
		// (especially if it's pinprick holes and not whiskers).
	}

	@Test
	/**
	 * This tests the interpolation of Z with a simple triangle. The most useful
	 * part of this test is that it checks whether you've correctly updated the
	 * very corners of the polygon.
	 */
	public void testNiceZInterpolation() {
		float[] verts = new float[] { 0, 0, 0, 0, 10, 10, 10, 0, 10 };
		int[] col = new int[] { 0, 0, 0 };
		Polygon poly = new Polygon(verts, col);

		EdgeList el = Pipeline.computeEdgeList(poly);
		
		// half-way up the triangle
		assertEquals(5, el.getLeftZ(5), 1e-5);
		assertEquals(10, el.getRightZ(5), 1e-5);

		// the bottom of the triangle
		assertEquals(0, el.getLeftZ(0), 1e-5);
		assertEquals(10, el.getRightZ(0), 1e-5);

		// the top of the triangle
		assertEquals(10, el.getLeftZ(10), 1e-5);
		assertEquals(10, el.getRightZ(10), 1e-5);

		// READ THIS.
		// You can get away with this test failing. I think it's common that
		// either this test will fail, or you'll get 'whiskers' (little
		// horizontal lines) in your renderer. This means you've got an
		// off-by-one error somewhere in your edgelist code, and so you'll
		// either have pinprick holes at the vertices or whiskers.
		//
		// It's nice to fix this issue, but we don't mark you down for it
		// (especially if it's pinprick holes and not whiskers).
	}

	@Test
	/**
	 * This is a nastier interpolation test, that checks you handle non-integer
	 * numbers correctly.
	 */
	public void testMeanInterpolation() {
		float[] verts = new float[] { 0, 0, 0, 0, 6, 11, 7, 0, 3 };
		int[] col = new int[] { 0, 0, 0 };
		Polygon poly = new Polygon(verts, col);

		EdgeList el = Pipeline.computeEdgeList(poly);

		// half-way up the triangle
		assertEquals(0, el.getLeftX(4), 1e-3);
		assertEquals(2.33333, el.getRightX(4), 1e-3);
		assertEquals(7.33333, el.getLeftZ(4), 1e-3);
		assertEquals(8.33333, el.getRightZ(4), 1e-3);
	}
}

// code for COMP261 assignments
