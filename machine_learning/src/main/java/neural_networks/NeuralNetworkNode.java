package neural_networks;
import java.lang.Math;

import util.VectorOperation;


public class NeuralNetworkNode {
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
	
	public NeuralNetworkNode(int input_len)
	{
		this.feature_len = input_len ;
		this.feature_vec = new double[this.feature_len];
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
		return result;
	}
	
	
	private double sigmod(double input)
	{
		return 1/(1 + Math.exp(-input));
	}
	
}
