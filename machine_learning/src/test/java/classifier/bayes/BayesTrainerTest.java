package classifier.bayes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import util.FileRead;
import base.InstanceSetD;
import base.InstanceSetI;
import base.InstanceD;
import base.InstanceI;
import junit.framework.TestCase;

/**
 * @return
 * @return BayesTrain
 * @throws Exception 
 * @comment:统计学习方法中的例题，用来验证算法是否正确
 */
public class BayesTrainerTest extends TestCase {
	static BayesTrainer bayesTrain;
	static BayesInfer bayesInfer;
	protected void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		
		int featureNumb  = 2;
		int classNumb = 2;
		List<Double[]> ajs = new ArrayList<Double[]>();
	    Double[] a1 = {1.0,2.0,3.0};
	    Double[] a2 = {4.0,5.0,6.0};
	    ajs.add(a1);
	    ajs.add(a2);
	    
		List<InstanceI> input = new ArrayList<InstanceI>();
		List<String> temp = FileRead.readLine("data/corpus/bayes.txt");
		
		for(int i=0;i<temp.size();++i)
		{
			String[] tempArray = temp.get(i).split(",");
			int x1 = Integer.parseInt(tempArray[0]);
			int x2 = 0;
			if(tempArray[1].equals("S"))
			{
				x2 = 4;
			}
			else if(tempArray[1].equals("M"))
			{
				x2 = 5;
			}
			else
			{
				x2 = 6;
			}
			
			int y = Integer.parseInt(tempArray[2]);
			if(y == -1)
			{
				y = 0;
			}
			int[] vector = {x1,x2}; 
			InstanceI vsm = new InstanceI(y,2,vector);
			input.add(vsm);
		}
		
//		bayesTrain = new BayesTrainer(featureNumb, classNumb, ajs, input,1);
		InstanceSetI inputFeature = new InstanceSetI(input);
		bayesTrain = new BayesTrainer();
		bayesTrain.init(inputFeature);
		
	}

	public void testTrain() throws IOException, ClassNotFoundException {		
		bayesTrain.train();
		bayesTrain.writeFile("data/result/bayes_result.txt");
		bayesTrain.saveModel("data/result/bayes_model.m");
		
		bayesInfer = new BayesInfer();
		bayesInfer.init("data/result/bayes_model.m");
		int result = bayesInfer.infer(new int[]{2,4});
		assertEquals(0,result);
	}
	
	

}
