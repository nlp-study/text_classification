package classifier.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import base.InstanceSetD;
import base.InstanceD;

public class FeatureDoubleToIntTest {
    static FeatureDoubleToInt featureDoubleToInt;
	@Before
	public void setUp() throws Exception {
		double[] x1 = {3.1,3.6,2.5};
		double[] x2 = {4,3,2};
		double[] x3 = {1,1,2};
		
		
		InstanceD vsm1 = new InstanceD(1,3,x1);
		InstanceD vsm2 = new InstanceD(1,3,x2);
		InstanceD vsm3 = new InstanceD(0,3,x3);
		
		List<InstanceD> instances = new ArrayList<InstanceD>();
		instances.add(vsm1);
		instances.add(vsm2);
		instances.add(vsm3);
		
		InstanceSetD inputfeature = new InstanceSetD(instances);
		
		featureDoubleToInt = new FeatureDoubleToInt(inputfeature);
	}

	@Test
	public void test() {
		featureDoubleToInt.trans();
		System.out.println(featureDoubleToInt.getOutputfeature());
	}

}
