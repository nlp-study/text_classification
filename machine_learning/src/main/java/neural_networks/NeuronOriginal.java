package neural_networks;
import java.lang.Math;
import java.util.Arrays;
import java.util.Random;

import util.VectorOperation;


public class NeuronOriginal {
	//	特征向量的长度 
	private int wieght_len = 0;
	//	特征向量
	private double[] wieghts;
	
	private double result = 0;
	
	public double getResult()
	{
		return result;
	}
	
	public double[] getFeatureVec()
	{
		return wieghts;
	}
	
	public void setFeatureVec(double[] inputVector)
	{
		this.wieghts = inputVector; 
	}
	
	public NeuronOriginal(int input_len)
	{
		this.wieght_len = input_len ;
		this.wieghts = new double[this.wieght_len];
		Random random = new Random();
		for(int i=0;i<this.wieght_len;++i)
		{
			this.wieghts[0] = random.nextDouble();
		}
	}
	
	public NeuronOriginal(double[] wieghts)
	{
		wieght_len  = wieghts.length;
		this.wieghts = new double[this.wieght_len];
		for(int i=0;i<this.wieght_len;++i)
		{
			this.wieghts[i] = wieghts[i];
		}
		
	}
	
	public double getFeautureVecByIndex(int index)
	{
		return wieghts[index];
	}
	
	public void setFeautureVecByIndex(int index,double value)
	{
		wieghts[index] = value;
	}
	
	public double feedForward(double[] input)
	{
//		System.out.println("input:"+Arrays.toString(input));
//		System.out.println("wieghts:"+Arrays.toString(wieghts));
		double temp_value = VectorOperation.innerProduct(this.wieghts, input);
//		System.out.println("temp_value:"+temp_value);
		result = sigmod(temp_value);
//		System.out.println("sigmod temp_value:"+result);
//		System.out.println("sigmod result:" + result);
		return result;
	}
	
	
	private double sigmod(double input)
	{
		return 1/(1 + Math.exp(-input));
	}
	
	public String toString()
	{
		return Arrays.toString(this.wieghts);
	}
}
