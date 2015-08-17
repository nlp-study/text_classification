package neural_networks.bp;
import java.lang.Math;

import util.VectorOperation;


public class NeuralNetworkNode {
	//	特征向量的长度 = 输入长度 + 1
	private int feature_len = 0;
	//	特征向量
	private double[] feature_vec;
	
	public NeuralNetworkNode(int input_len)
	{
		this.feature_len = input_len + 1;
		this.feature_vec = new double[this.feature_len];
	}
	
	public double transferFunction(double[] input)
	{
		double[] temp_array = new double[input.length + 1];  //[1,x1,x2,.....,xn]
		temp_array[0] = 1;
		for (int i=0;i<input.length;++i)
		{
			temp_array[i+1] = input[i]; 
		}
		double temp_value = VectorOperation.innerProduct(this.feature_vec, temp_array);
		return sigmod(temp_value);
	}
	
	
	private double sigmod(double input)
	{
		return 1/(1 + Math.exp(-input));
	}
	
	
	
	

}
