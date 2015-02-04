package regress.softmax;

import java.util.Arrays;

import org.apache.log4j.Logger;

import regress.AbstractRegressInfer;
import util.VectorOperation;
import classifier.AbstractInfer;
import classifier.bayes.BayesInfer;
import classifier.bayes.BayesModel;

public class SoftmaxInfer extends AbstractRegressInfer {
	Logger logger = Logger.getLogger(SoftmaxInfer.class);

	SoftmaxModel model = new  SoftmaxModel();
	
	double [][] weight;
	
	int dim;
	
	int classNumb;

	
	public void init(String path) throws Exception {
		model = (SoftmaxModel)super.initModel(path);
		weight = model.getWeight();
		dim = model.getDim();
		classNumb = model.getClassNumb();
	}

	
	public int infer(double[] input) {
		
		//将输入的向量维度增加一位，并且增加的这一位设为1；
		double[] tempVector = new double[input.length+1];
		Arrays.fill(tempVector, 1);
		System.arraycopy(input, 0, tempVector, 0, input.length);
		
		double max = 0;
		int type = -1;
		
		for(int i=0;i<classNumb;++i )
		{
			double temp = VectorOperation.innerProduct(weight[i], tempVector);
			double result = Math.exp(temp);
			if(result > max)
			{
				max = result;
				type = i;
			}
		}
		
		return type;
	}
	
	
	
	

}
