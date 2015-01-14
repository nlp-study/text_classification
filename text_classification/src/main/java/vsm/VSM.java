package vsm;

import java.io.Serializable;
import java.util.Arrays;

public class VSM implements Serializable {
	public static  int size = 0;
	public int type;//需要修改成int型
	public double[] vector = new double[size];  //需要改成基本类型double
	
	public VSM()
	{
		
	}
	
	public VSM(int type, double[] vector) {
		super();
		this.type = type;
		this.vector = vector;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static int getSize() {
		return size;
	}
	public static void setSize(int size) {
		VSM.size = size;
	}
	public double[] getVector() {
		return vector;
	}
	public void setVector(double[] vector) {
		this.vector = vector;
	}
	public Double getFeature(int index)
	{
		return vector[index];
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(type+" ");
		sb.append(Arrays.toString(vector));
		return sb.toString();
	}
}
