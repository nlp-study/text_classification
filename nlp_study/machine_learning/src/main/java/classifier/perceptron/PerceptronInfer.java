package classifier.perceptron;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;

import util.VectorOperation;
import classifier.AbstractInfer;
import classifier.bayes.BayesModel;

public class PerceptronInfer implements AbstractInfer {
	Logger logger = Logger.getLogger(PerceptronInfer.class);

	PerceptronModel model;
	
	//权值向量
    double[] w;
    
	//偏置
	double b;
	
	public  void init(String path) throws Exception
	{
		FileInputStream fi = new FileInputStream(path);

		ObjectInputStream si = new ObjectInputStream(fi);

		try {

			model = (PerceptronModel) si.readObject();

			si.close();

		} catch (IOException e)

		{
			System.out.println(e);
		}
		
		w = model.getW();
		b = model.getB();
	}

	public int infer(double[] input)
	{
		double product = VectorOperation.innerProduct(w, input);
		double result  = product + b;
		if(result<0)
		{
			return -1;
		}
		else if(result>0)
		{
			return 1;
		}
		else
		{
			logger.info("不能有效分类！");
			return -1;
		}
	}

	

}
