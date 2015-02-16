package regress.svm;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import validation.HeartScale;
import validation.Iris;
import base.InstanceD;
import base.InstanceSetD;

public class SVMTrainerTest {
	private static SVMTrainer svmTrainer = new SVMTrainer();
//	private static CopyOfSVMTrainer svmTrainer = new CopyOfSVMTrainer();
	
	
	@Before
	public void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		
//		String path = "data/corpus/iris_bin.data";
//		Iris iris = new Iris();
//		iris.readData(path);
//		InstanceSetD inputFeature = iris.getInputFeature();
		
		String path = "data/corpus/heart_scale.txt";
		HeartScale heartScale = new HeartScale();
		heartScale.readData(path);
		InstanceSetD inputFeature = heartScale.getInputFeature();
		
		double[] x1 = {0,0,1};
		double[] x2 = {0,0,2};
		double[] x3 = {1,1,0};
		
		
		InstanceD vsm1 = new InstanceD(0,3,x1);
		InstanceD vsm2 = new InstanceD(0,3,x2);
		InstanceD vsm3 = new InstanceD(1,3,x3);
		
		List<InstanceD> instances = new ArrayList<InstanceD>();
		instances.add(vsm1);
		instances.add(vsm2);
		instances.add(vsm3);
		
//		InstanceSetD inputFeature = new InstanceSetD(instances);
		
		
		
		for(int i=0;i<inputFeature.getSize();++i)
		{
			int typeid = inputFeature.getClassID(i);
			if(typeid == 0)
			{
				inputFeature.setClassID(i, -1);
			}
		}
		
		svmTrainer.init(inputFeature);
	}

	@Test
	public void test() throws Exception {
		String modelPath = "data/result/svmModel.m";
		svmTrainer.train();
		svmTrainer.saveModel(modelPath);
		
		SVMInfer svmInfer = new SVMInfer();
		svmInfer.init(modelPath);

		String path = "data/corpus/heart_scale.txt";
		HeartScale heartScale = new HeartScale();
		heartScale.readData(path);
		InstanceSetD inputFeature = heartScale.getInputFeature();
		
		int sum = 0;
		for(int i=0;i<inputFeature.getSize();++i)
		{
			double[] input = inputFeature.getInstanceD(i).getVector();
			int typeid = svmInfer.infer(input);
			int targetid = 0;
			if(inputFeature.getClassID(i) == 0)
			{
				targetid = -1;
			}
			else 
				targetid = 1;
			if(targetid == typeid)
			{
				System.out.println(i+"-success!");
				++sum;
			}
			else
			{
				System.out.println(i+"-false!");
			}
		}
		
		System.out.println((double)sum/inputFeature.getSize());
		
		
	}

}
