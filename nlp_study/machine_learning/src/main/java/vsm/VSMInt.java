package vsm;

import java.util.Arrays;

/**
 * @author xiaohe
 * 创建于：2015年1月7日
 * 全部都用整数表示的vsm，其中的特征向量的值都为0~n的连续整数，便于某些机器学习算法处理
 */
public class VSMInt  {
	public static int size = 0;
	private int type;//需要修改成int型
	private int[] vector;
	
	public VSMInt(){}
	
	public VSMInt(int type, int[] vector) {
		this.type = type;
		this.vector = vector;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int[] getVector() {
		return vector;
	}

	public void setVector(int[] vector) {
		this.vector = vector;
	}
	
	public String toString()
	{
		String str = type+" "+Arrays.toString(vector);
		return str;
	}

}
