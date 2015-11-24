package neural_networks;
import java.lang.Math;
import java.util.Random;

import util.VectorOperation;


public class Neuron_1 {
	//	特征向量的长度 
	private int feature_len = 0;
	//	特征向量
	private double[] feature_vec;
	
	private double result = 0;
	
	public double getResult()
	{
		return result;
	}
	
	public double[] getFeatureVec()
	{
		return feature_vec;
	}
	
	public void setFeatureVec(double[] inputVector)
	{
		this.feature_vec = inputVector; 
	}
	
	public Neuron_1(int input_len)
	{
		this.feature_len = input_len ;
		this.feature_vec = new double[this.feature_len];
		Random random = new Random();
		for(int i=0;i<this.feature_len;++i)
		{
			this.feature_vec[0] = random.nextFloat();
		}
	}
	
	public double getFeautureVecByIndex(int index)
	{
		return feature_vec[index];
	}
	
	public void setFeautureVecByIndex(int index,double value)
	{
		feature_vec[index] = value;
	}
	
	public double transferFunction(double[] input)
	{
		double temp_value = VectorOperation.innerProduct(this.feature_vec, input);
		result = sigmod(temp_value);
//		System.out.println("sigmod result:" + result);
		return result;
	}
	
	
	private double sigmod(double input)
	{
		return 1/(1 + Math.exp(-input));
	}
	
}
