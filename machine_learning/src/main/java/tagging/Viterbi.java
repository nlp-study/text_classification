package tagging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import tagging.hmm.HMMTrainer;

public class Viterbi {
	Logger logger = Logger.getLogger(Viterbi.class);
	
	/**
	 * 状态转义矩阵
	 */
	double[][] A;
	
	/**
	 * 发射矩阵
	 */
	double[][] B;
	
	/**
	 * 初始状态
	 */
	double[]  pi;
	
	/**
	 * 观察值
	 */
	int[]  O;
	
	/**
	 * 状态
	 */
	int[]  S;
	
	//内部参数
	/**
	 * δ,记录每一个观察值对应的最大概率
	 */
	double[][] delta;
	
	/**
	 * ψ,记录最大概率对应的状态
	 */
	int[][] psi;	
	
	/**
	 * 解码出的最大概率
	 */
	double P;	
	
	/**
	 * 最后一个观察值对应的最大的概率的下标 i*
	 */
	int I;
	
	/**
	 * 观察值的长度
	 */
	int inputSize;
	
	/**
	 * 状态值的长度
	 */
	int stateSize;
	
	public Viterbi()
	{
		
	}
	
	
	
	public int[] getS() {
		return S;
	}
	
	public void setS(int[] s) {
		S = s;
	}



	public void init(double[][] A,double[][] B,double[] pi,
			int[] O)
	{
		this.A = A;
		this.B = B;
		this.pi = pi;
		this.O = O;
		
		this.inputSize = O.length;
		this.stateSize = A.length;
		
		this.delta = new double[inputSize][stateSize];
		this.psi = new int[inputSize][stateSize];
		this.S = new int[inputSize];
	}
	
	public void clear()
	{
		this.A = null;
		this.B = null;
		this.pi = null;
		this.O = null;
		
		this.inputSize = 0;
		this.stateSize = 0;
		
		delta = null;
		psi = null;
	}	
		
	public void excute()
	{
		recursive();
		calculateState();
	}
	
	public void recursive()
	{
		for(int t=0;t<inputSize;++t)
		{
			if(t == 0)
			{
				for(int i=0;i<stateSize;++i)
				{
					delta[t][i] = pi[i]*B[i][O[t]];
				}
				Arrays.fill(psi[0], 0);
			}
			else
			{
				for(int i=0;i<stateSize;++i)
				{
					double[] deltaTemp = new double[stateSize];
					double[] psiTemp = new double[stateSize];
					
					for(int j=0;j<stateSize;++j)
					{
						deltaTemp[j] = delta[t-1][j]*A[j][i]*B[i][O[t]];
					}
					
					for(int j=0;j<stateSize;++j)
					{
						psiTemp[j] = delta[t-1][j]*A[j][i]*B[i][O[t]];
					}
					
					delta[t][i] = maxValue(deltaTemp);
					psi[t][i] = maxValueIndex(psiTemp);
				}
				logger.info("δ"+t+":"+Arrays.toString(delta[t]));
				logger.info("ψ"+t+":"+Arrays.toString(psi[t]));
			}			
		}
		
		P = maxValue(delta[inputSize-1]);
		I = maxValueIndex(delta[inputSize-1]);
	}
		
	/**
	 * @comment:找到数组中最大的值
	 * @param input
	 * @return:void
	 */
	public double maxValue(double[] input)
	{
		double max = 0;
		for(int i=0;i<input.length;++i)
		{
			if(input[i] > max)
			{
				max = input[i];
			}
		}
		
		return max;
	}
		
	/**
	 * @comment:找到数组中最大的值对应的下标
	 * @param input
	 * @return:void
	 */
	public int maxValueIndex(double[] input)
	{
		double max = 0;
		int index = 0;
		for(int i=0;i<input.length;++i)
		{
			if(input[i] > max)
			{
				max = input[i];
				index = i;
			}
		}
		
		return index;
	}
	
	public void calculateState()
	{
		logger.info("I:"+I);
		int tempI = I;
		for(int i=inputSize-1;i>=0;--i)
		{
			S[i] = tempI;
			tempI = psi[i][tempI];
		}
	}
	
	
	public static void main(String[] args)
	{
		PropertyConfigurator.configure("log4j.properties");
		
		double[][] A = {{0.5,0.2,0.3},{0.3,0.5,0.2},{0.2,0.3,0.5}};
		double[][] B = {{0.5,0.5},{0.4,0.6},{0.7,0.3}};
		double[] pi= {0.2,0.4,0.4};
		
		int[] O = {0,1,0};		
		
		Viterbi viterbi = new Viterbi();
		viterbi.init(A, B, pi, O);
		viterbi.excute();
		int[] S = viterbi.getS();
		
		System.out.println(Arrays.toString(S));
		
	}
}
