package util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

public class VectorOperationTest {

	@Before
	public void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");

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
	
	@Test
	public void errorSumSquaresTest()
	{
		double[] dest = {1,1,0};
		double[] source = {-1,1,1};
		
		double temp = VectorOperation.errorSumSquares(dest, source);
		System.out.println(temp);
		assertEquals("",5.0,temp);
	}

}
