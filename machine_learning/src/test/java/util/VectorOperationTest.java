package util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class VectorOperationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void constantMultipTest() {
		double[] dest = {-0.0001,-0.002,100};
		double[] source = {0.01,0.2,-10000};
		double constant = -0.01;
		double[] temp = VectorOperation.constantMultip(source, constant);
		
		System.out.println(Arrays.toString(dest));
		System.out.println(Arrays.toString(temp));
	}

}
