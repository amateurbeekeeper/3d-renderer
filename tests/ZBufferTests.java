package tests;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

import renderer.EdgeList;
import renderer.Pipeline;

/**
 * @author tony
 */
public class ZBufferTests {

	// These tests use a dummy version of an EdgeList that returns values we can
	// control for the test. For example, in the first test the EdgeList just
	// returns constant values and so it doesn't represent a triangle at all!

	@Test
	/**
	 * First, check that colour is being put into the zbuffer array at all and
	 * that the edge lists with lesser Z-value (i.e. closer to the viewer) are
	 * placed on top of pixels with a higher Z-value (farther away).
	 */
	public void testFillUpEmptyZBuffer() {
		Color[][] zbuffer = new Color[10][10];
		float[][] zdepth = new float[10][10];

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				zdepth[i][j] = 50;

		Color col = new Color(100, 0, 0);

		// subclass EdgeList to return some dummy values.
		EdgeList el = new EdgeList(0, 10) {
			public int getStartY() {
				return 0;
			}

			public int getEndY() {
				return 10;
			}

			public float getLeftX(int y) {
				return 0;
			}

			public float getRightX(int y) {
				return 10;
			}

			public float getLeftZ(int y) {
				return 25;
			}

			public float getRightZ(int y) {
				return 25;
			}
		};

		Pipeline.computeZBuffer(zbuffer, zdepth, el, col);

		// check the colour is written.
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				assertEquals(col, zbuffer[i][j]);
			}
		}

		// check the zdepth is updated.
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				assertEquals(25, zdepth[i][j], 1e-5);
			}
		}
	}

	@Test
	/**
	 * Now check that, if we are trying to render a polygon _behind_ pixels with
	 * a lower (nearer the viewer) Z-value, we do not change the zbuffer or
	 * zdepth.
	 */
	public void testDontHaveXRayGlasses() {
		Color[][] zbuffer = new Color[10][10];
		float[][] zdepth = new float[10][10];

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				zdepth[i][j] = 50;

		Color col = new Color(100, 0, 0);

		// subclass EdgeList to return some dummy values.
		EdgeList el = new EdgeList(0, 10) {
			public int getStartY() {
				return 0;
			}

			public int getEndY() {
				return 10;
			}

			public float getLeftX(int y) {
				return 0;
			}

			public float getRightX(int y) {
				return 10;
			}

			public float getLeftZ(int y) {
				return 75;
			}

			public float getRightZ(int y) {
				return 75;
			}
		};

		Pipeline.computeZBuffer(zbuffer, zdepth, el, col);

		// check the colour is written.
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				assertEquals(null, zbuffer[i][j]);
			}
		}

		// check the zdepth is updated.
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				assertEquals(50, zdepth[i][j], 1e-5);
			}
		}
	}

	@Test
	/**
	 * Check that we only fill up the pixels specified by the edge list, and not
	 * areas outside it.
	 */
	public void testColourBetweenTheLines() {
		Color[][] zbuffer = new Color[10][10];
		float[][] zdepth = new float[10][10];
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				zdepth[i][j] = 50;

		Color col = new Color(100, 0, 0);

		// Let's make a triangle in the edge list.
		EdgeList el = new EdgeList(0, 10) {
			public int getStartY() {
				return 0;
			}

			public int getEndY() {
				return 10;
			}

			public float getLeftX(int y) {
				return 0;
			}

			public float getRightX(int y) {
				// this makes the triangle.
				return y;
			}

			public float getLeftZ(int y) {
				return 25;
			}

			public float getRightZ(int y) {
				return 25;
			}
		};

		Pipeline.computeZBuffer(zbuffer, zdepth, el, col);

		// now ensure that all values outside the triangle are left as null.
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
			  			  
			  //System.out.println(i + ": " + j + ": " + zbuffer[i][j]);
			  
				if (i < j)
					assertEquals(col, zbuffer[i][j]);
				else
					assertEquals("ERROR: "  + i + ": " + j + ": " + zbuffer[i][j] + " |||||||", null, zbuffer[i][j]);
			}
		}

	}
}

//code for COMP261 assignments
