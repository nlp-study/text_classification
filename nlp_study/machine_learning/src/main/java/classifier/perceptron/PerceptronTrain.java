package classifier.perceptron;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.apache.log4j.Logger;

import util.VectorOperation;
import vsm.VSM;
import vsm.VSMBuilder;
import classifier.AbstractTrainer;

public class PerceptronTrain implements AbstractTrainer {
	Logger logger = Logger.getLogger(PerceptronTrain.class);

	//权值向量
    double[] w;
    
	//偏置
	double b;
	
	//步长，学习率
	double eta;
	
	//输入特征的维度
	int N;
	
	List<VSM> vms = new ArrayList<VSM>(); 
	
	PerceptronModel model = new PerceptronModel();
	
	static final int  LOOP_MAX = 100;
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

	public void init(VSMBuilder vsmBuilder)
    {
    	vms.addAll(vsmBuilder.getVsms());
    	for(int i=0;i<vms.size();++i)
    	{
    		if(vms.get(i).getType() == 0)
    		{
    			vms.get(i).setType(-1);
    		}
    	}
    	
    	N = VSM.size;
    	
    	w = new double[N];
    	
    	for(int i=0;i<N;++i)
    	{
    		w[i] = 0;
    	}
    	
    	b = 0;
    	
    	eta = 1;	
    }
	
	public void train()
	{
		int loop = 0;
		int i=1;
		while(i != vms.size())
		{
			for(VSM vsm:vms)
			{
				double[] tempVector = vsm.getVector();
				double h = vsm.getType()*(VectorOperation.innerProduct(w,tempVector)+b);
				
				if(h<=0)
				{
					double constant = eta*vsm.getType();
					double[] tempArray = VectorOperation.constantMultip(tempVector, constant);
					w = VectorOperation.addition(w, tempArray);
					b = b + constant;
					
					i=0;
				}
				else
				{
					++i;
				}				
			}
			
			++loop;
			double correct_prop = i/vms.size();

			if(loop == LOOP_MAX || correct_prop>COORRECT_PROPORTION)
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
	
	public static void main(String[] args)
	{
		
	}

}
