package regress.knn;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import base.InputFeatureD;
import base.InstanceD;

public class KNNTrainerTest {
    static KNNTrainer  knnTrainer = new KNNTrainer();
    static KNNInfer knnInfer = new KNNInfer();
    
	@Before
	public void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		
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
		
		InputFeatureD inputfeature = new InputFeatureD(instances);
		knnTrainer.init(inputfeature);
	}

	@Test
	public void test() throws Exception {
		String path = "data/result/knn_model.m";
		knnTrainer.train();
		knnTrainer.saveModel(path);
		
		double[] x3 = {1,2,2};
		knnInfer.init(path);
		int classid = knnInfer.infer(x3);
		assertEquals(0,classid);
	}

}
