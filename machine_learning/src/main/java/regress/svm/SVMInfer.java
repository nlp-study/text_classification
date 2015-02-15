package regress.svm;

import org.apache.log4j.Logger;

import base.InstanceSetD;
import regress.AbstractRegressInfer;
import util.VectorOperation;
import validation.BinaryRegreeCrossValidation;

public class SVMInfer extends AbstractRegressInfer {
	Logger logger = Logger.getLogger(SVMInfer.class);
	
	//权重向量
    double[] w;
    
    	//截距
    double b;
    
    //每个输入特征的参数
    double[] alpha;
    
    InstanceSetD instanceSet;
    
    SVMModel model;

	@Override
	public void init(String path) throws Exception {
		model = (SVMModel)super.initModel(path);
        w = model.getW();
        b = model.getB();
        alpha = model.getAlpha();
        instanceSet = model.getInstanceSet();
	}

	@Override
	public int infer(double[] input) {
		double result = predict(input);
		
		if(result<0)
		{
			return -1;
		}
		
		return 1;
	}
	
	public double predict(double[] input)
	{
		double sum = 0;
		
		for(int i=0;i<instanceSet.getSize();++i)
		{
			int y = instanceSet.getClassID(i);
			double[] temp = instanceSet.getInstanceD(i).getVector();
			double kernal = kernelFunction(input,temp);
			
			sum += alpha[i]*y*kernal;
		}
		sum+=b;
		
		return sum;
	}
	
	public double kernelFunction(double[] xi,double[] xj)
	{
		double result = Kernel.linear(xi, xj);
		return result;
	}

}
