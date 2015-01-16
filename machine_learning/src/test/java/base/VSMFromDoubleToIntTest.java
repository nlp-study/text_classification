package base;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class VSMFromDoubleToIntTest {
	private static VSMtransformer vsmFromDoubleToInt = new VSMtransformer();

	
	@BeforeClass
    public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		
		double[] x1 = {3,3,2};
		double[] x2 = {4,3,2};
		double[] x3 = {1,1,2};
		
		
		InstanceD vsm1 = new InstanceD(1,3,x1);
		InstanceD vsm2 = new InstanceD(1,3,x2);
		InstanceD vsm3 = new InstanceD(1,3,x3);
		
		List<InstanceD> vsms = new ArrayList<InstanceD>();
		vsms.add(vsm1);
		vsms.add(vsm2);
		vsms.add(vsm3);
		
		vsmFromDoubleToInt.setVsms(vsms);
    }
	
	
	
	@Test
	public void testInit() {
		vsmFromDoubleToInt.init();
		
		assertEquals("",3,vsmFromDoubleToInt.getDim());
	}

	@Test
	public void testCalculateFeatures() {
		vsmFromDoubleToInt.calculateFeatures();
		double[][] features = vsmFromDoubleToInt.getFeatures();
		double[][] featuresExcept = {{1,3,4},{1,3},{2}};
		System.out.println(Arrays.deepToString(features));
		assertArrayEquals(featuresExcept,features);
	}

	@Test
	public void testVsm2Int() {
		double[][] featuresExcept = {{1,3,4},{1,3},{2}};
		vsmFromDoubleToInt.setFeatures(featuresExcept);
		
		vsmFromDoubleToInt.vsm2Int();
		List<InstanceI> actual = vsmFromDoubleToInt.getVsmints();
		
		int[] x1 = {1,1,0};
		int[] x2 = {2,1,0};
		int[] x3 = {0,0,0};
		
		InstanceI vsm1 = new InstanceI(1,3,x1);
		InstanceI vsm2 = new InstanceI(1,3,x2);
		InstanceI vsm3 = new InstanceI(0,3,x3);
		
		List<InstanceI> expected = new ArrayList<InstanceI>();
		
		expected.add(vsm1);
		expected.add(vsm2);
		expected.add(vsm3);
		
		System.out.println("expected:"+expected);
		System.out.println("actual:"+actual);
		assertEquals(expected.toString(), actual.toString());
		
	}

}
