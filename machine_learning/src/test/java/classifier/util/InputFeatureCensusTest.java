package classifier.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import base.InstanceI;

public class InputFeatureCensusTest {
	
	private static InstanceSetCensus inputFeatureCensus = new InstanceSetCensus();

	@Before
	public void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");

		int dim = 3;
		int[] vector1 = {1,12,21};
		int[] vector2 = {1,11,21};
		int[] vector3 = {1,11,22};
		int[] vector4 = {2,12,23};
		int[] vector5 = {3,13,23};
		int[] vector6 = {3,13,23};
		
		InstanceI vsmint1 = new InstanceI(1,dim, vector1);
		InstanceI vsmint2 = new InstanceI(1,dim, vector2);
		InstanceI vsmint3 = new InstanceI(2,dim, vector3);
		InstanceI vsmint4 = new InstanceI(2,dim, vector4);
		InstanceI vsmint5 = new InstanceI(3,dim, vector5);
		InstanceI vsmint6 = new InstanceI(3,dim, vector6);
		
		List<InstanceI> instanceInput = new ArrayList<InstanceI>();;
		instanceInput.add(vsmint1);
		instanceInput.add(vsmint2);
		instanceInput.add(vsmint3);
		instanceInput.add(vsmint4);
		instanceInput.add(vsmint5);
		instanceInput.add(vsmint6);
		
		inputFeatureCensus.setInputFeature(instanceInput);
		inputFeatureCensus.init();
		
	}

	@Test
	public void test() {
		inputFeatureCensus.excute();
		System.out.println(inputFeatureCensus);
	}

}
