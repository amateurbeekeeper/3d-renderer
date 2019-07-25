package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * If you run this file as a JUnit test, it automatically runs the whole test suite.
 * 
 * @author tony
 */

@RunWith(Suite.class)
@SuiteClasses({ EdgeListTests.class, ZBufferTests.class, PolygonHidingTests.class, ShadingTests.class, RotationTests.class })
public class AllTests {

}

//code for COMP261 assignments