package tests;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

import renderer.Pipeline;
import renderer.Scene.Polygon;
import renderer.Vector3D;

/**
 * @author tony
 */
public class ShadingTests {
	@Test
	/**
	 * An easy one, here there is no directional light only ambient. Polygon
	 * colour should just be the ambient light.
	 */
	public void testAmbientOnly() {
		float[] verts = new float[] { 0, 0, 5, 5, 10, 5, 10, 0, 5 };
		int[] col = new int[] { 255, 255, 255 };
		Vector3D light = new Vector3D(0, 0, -1);
		Color lightCol = new Color(0, 0, 0); // dark
		Color ambient = new Color(10, 10, 10);
		Polygon poly = new Polygon(verts, col);

		Color shading = Pipeline.getShading(poly, light, lightCol, ambient);
		Color expected = new Color(10, 10, 10);

		assertEquals(expected, shading);
	}

	@Test
	/**
	 * No ambient light, the directional light faces the polygon head on.
	 * The shading colour should be the
	 */
	public void testStraightOnDirectional() {
		float[] verts = new float[] { 0, 0, 5, 5, 10, 5, 10, 0, 5 };
		int[] col = new int[] { 100, 100, 255 };
		Vector3D light = new Vector3D(0, 0, -1);
		Color lightCol = new Color(100, 255, 100);
		Color ambient = new Color(0, 0, 0);
		Polygon poly = new Polygon(verts, col);

		Color shading = Pipeline.getShading(poly, light, lightCol, ambient);
		Color expected = new Color(39, 100, 100);

		assertEquals(expected, shading);
	}

	@Test
	/**
	 * This puts the directional light facing the back of the polygon. In this
	 * case the directional light should be ignored and only the ambient light
	 * used.
	 */
	public void testBacklit() {
		float[] verts = new float[] { 0, 0, 5, 5, 10, 5, 10, 0, 5 };
		int[] col = new int[] { 255, 255, 255 };
		Vector3D light = new Vector3D(0, 0, 1);
		Color lightCol = new Color(255, 0, 0);
		Color ambient = new Color(10, 10, 10);
		Polygon poly = new Polygon(verts, col);

		Color shading = Pipeline.getShading(poly, light, lightCol, ambient);
		Color expected = new Color(10, 10, 10);

		assertEquals(expected, shading);
	}
	
	@Test
	/**
	 * Now let's test the light coming in at an angle.
	 */
	public void testAngledDirectional() {
		float[] verts = new float[] { 0, 0, 5, 5, 10, 5, 10, 0, 5 };
		int[] col = new int[] { 100, 100, 255 };
		Vector3D light = new Vector3D(-2.5f, -1, -1);
		Color lightCol = new Color(100, 255, 100);
		Color ambient = new Color(0, 0, 0);
		Polygon poly = new Polygon(verts, col);

		Color shading = Pipeline.getShading(poly, light, lightCol, ambient);
		Color expected = new Color(13, 34, 34);

		assertEquals(expected, shading);
	}
}

//code for COMP261 assignments