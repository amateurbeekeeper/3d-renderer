package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import renderer.Pipeline;
import renderer.Scene;
import renderer.Scene.Polygon;
import renderer.Vector3D;

/**
 * @author tony
 */
public class RotationTests {

	private static boolean eq(Polygon p1, Polygon p2) {
		Vector3D[] v1 = p1.getVertices();
		Vector3D[] v2 = p2.getVertices();

		return v1[0].equals(v2[0]) & v1[1].equals(v2[1]) & v1[2].equals(v2[2]);
	}

	@Test
	/** Check our polygon doesn't move when we don't rotate. */
	public void testNoRotation() {
		float[] verts = new float[] { 10, 5, 5, 2, 3, 2, 9, 5, 4 };
		int[] col = new int[] { 0, 0, 0 };
		final Vector3D light = new Vector3D(0, 0, 0);

		final Polygon p1 = new Polygon(verts, col);

		Scene scene = new Scene(null, null) {
			public List<Polygon> getPolygons() {
				List<Polygon> ar = new ArrayList<>();
				ar.add(p1);
				return ar;
			}

			public Vector3D getLight() {
				return light;
			}
		};

		Scene res = Pipeline.rotateScene(scene, 0, 0);
		Polygon p2 = res.getPolygons().get(0);

		assertTrue(eq(p1, p2));
	}

	@Test
	/** Test a rotation in the XZ plane. */
	public void testXZRotation() {
		float[] verts = new float[] { 10, 5, 5, 2, 3, 2, 9, 5, 4 };
		int[] col = new int[] { 0, 0, 0 };
		final Vector3D light = new Vector3D(0, 0, 0);

		final Polygon p1 = new Polygon(verts, col);

		Scene scene = new Scene(null, null) {
			public List<Polygon> getPolygons() {
				List<Polygon> ar = new ArrayList<>();
				ar.add(p1);
				return ar;
			}

			public Vector3D getLight() {
				return light;
			}
		};

		Scene res = Pipeline.rotateScene(scene, 0, 0.3f);
		Polygon p2 = res.getPolygons().get(0);

		float[] vertsExpected = new float[] { 11.030966f, 5.0f, 1.8214803f, 2.5017135f, 
				3.0f, 1.3196325f, 9.780109f, 5.0f, 1.161664f };
		Polygon expected = new Polygon(vertsExpected, col);

		assertTrue(eq(expected, p2));
	}
	
	@Test
	/** Test a rotation in the YZ plane. */
	public void testYZRotation() {
		float[] verts = new float[] { 10, 5, 5, 2, 3, 2, 9, 5, 4 };
		int[] col = new int[] { 0, 0, 0 };
		final Vector3D light = new Vector3D(0, 0, 0);

		final Polygon p1 = new Polygon(verts, col);

		Scene scene = new Scene(null, null) {
			public List<Polygon> getPolygons() {
				List<Polygon> ar = new ArrayList<>();
				ar.add(p1);
				return ar;
			}

			public Vector3D getLight() {
				return light;
			}
		};

		Scene res = Pipeline.rotateScene(scene, 0.6f, 0);
		Polygon p2 = res.getPolygons().get(0);

		float[] vertsExpected = new float[] { 10.0f, 1.3034656f, 6.94989f, 
				2.0f, 1.346722f, 3.3445988f, 9.0f, 1.868108f, 6.1245546f };
		Polygon expected = new Polygon(vertsExpected, col);

		assertTrue(eq(expected, p2));
	}
}

// code for COMP261 assignments