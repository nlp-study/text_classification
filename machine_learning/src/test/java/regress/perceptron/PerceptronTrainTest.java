package regress.perceptron;

import static org.junit.Assert.*;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import regress.svm.SVMInfer;
import base.InstanceSetD;
import validation.Iris;

public class PerceptronTrainTest {
    private static PerceptronTrainer perceptronTrain = new PerceptronTrainer();
	
    
    @Before
	public void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");

		String path = "data/corpus/iris_bin.data";
		Iris iris = new Iris();
		iris.readData(path);
		InstanceSetD instanceSet = iris.getInputFeature();
		
		
		for(int i=0;i<instanceSet.getSize();++i)
		{
			int typeid = instanceSet.getClassID(i);
			if(typeid == 0)
			{
				instanceSet.setClassID(i, -1);
			}
		}
		
		perceptronTrain.init(instanceSet);
	}

	@Test
	public void test() throws Exception {
		String modelPath = "data/result/model.m";
		perceptronTrain.train();
		perceptronTrain.saveModel(modelPath);
		
		PerceptronInfer perceptronInfer = new PerceptronInfer();
		perceptronInfer.init(modelPath);
		double[] input = {4.7,3.2,1.6,0.2};
		int classid = perceptronInfer.infer(input);
		assertEquals(-1,classid);
		
	}

}
