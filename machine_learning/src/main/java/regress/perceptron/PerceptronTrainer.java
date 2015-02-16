package regress.perceptron;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import base.InstanceD;
import base.InstanceSetD;
import regress.AbstractRegressTrainer;
import util.VectorOperation;
import classifier.AbstractTrainer;

public class PerceptronTrainer extends AbstractRegressTrainer {
	Logger logger = Logger.getLogger(PerceptronTrainer.class);

	//权值向量
    double[] w;
    
	//偏置
	double b;
	
	//步长，学习率
	double eta;
	
	//输入特征的维度
	int length;
	
	int inputSize;
	
	List<InstanceD> instances = new ArrayList<InstanceD>();
	
	PerceptronModel model = new PerceptronModel();
	
	static final int  LOOP_MAX = 1000;
	static final double  COORRECT_PROPORTION = 0.95;
	
		
    public double[] getW() {
		return w;
	}

	public void setW(double[] w) {
		this.w = w;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public void init(InstanceSetD instanceSet)
    {
		this.instances = instanceSet.getInstances();
    	
    	
    	length = instanceSet.getLength();
    	
    	w = new double[length];
    	
    	inputSize = instanceSet.getSize();
    	
    	b = 0;
    	
    	eta = 1;	
    }
	
	public void train()
	{
		int loop = 0;
		while(true)
		{   
			++loop;
			double[] wLast = new double[length];
			double bLast = b;
			System.arraycopy(w, 0, wLast, 0, length);
			
			logger.info("loop:"+loop);
			for(InstanceD vsm:instances)
			{
				double[] tempVector = vsm.getVector();
				double h = vsm.getType()*(VectorOperation.innerProduct(w,tempVector)+b);
//				logger.info("h:"+h);
				
				if(h<=0)
				{
					double constant = eta*vsm.getType();
					double[] tempArray = VectorOperation.constantMultip(tempVector, constant);
					w = VectorOperation.addition(w, tempArray);
					b = b + constant;
				}
			}
			
//			logger.info("w:"+Arrays.toString(w));
//			logger.info("b:"+b);
			 
			
			if(loop == LOOP_MAX )
			{
				break;
			}
			if(Arrays.equals(w, wLast) && bLast == b)
			{
				break;
			}
		}
	}
	
	public void saveModel(String path)throws Exception
	{
		model.setB(b);
		model.setW(w);
		
		FileOutputStream fo = new FileOutputStream(path);   
	     ObjectOutputStream so = new ObjectOutputStream(fo);   
	  
	     try {   
	            so.writeObject(model);   
	            so.close();   
	  
	     } catch (IOException e) {   
	            System.out.println(e);   
	     }   
	}
	
	public void clear()
	{
		
	}
	
	public static void main(String[] args)
	{
		
	}

}
