package util;

/**
 * @author xiaohe
 * 创建于：2015年1月5日
 * 常见距离的定义，详见网址：http://blog.csdn.net/v_july_v/article/details/8203674
 */
public class DistanceCalculation {
	/**
	 * @comment:欧式距离
	 * @param v1
	 * @param v2
	 * @return:double
	 */
	public static double EuclideanDistance(double[] v1,double[] v2)
	{
		double[]  tempResult = new double[v1.length];
		
		double sum = 0.0;
		for(int i=0;i<v1.length;++i)
		{
			double tempDifValue = v1[i] - v2[i];
			sum += Math.pow(tempDifValue,2);
		}
		
		double result = Math.sqrt(sum);
		return result;
	}
	
	
	/**
	 * @comment:曼哈顿距离
	 * @param v1
	 * @param v2
	 * @return:double
	 */
	public static double ManhattanDistance(double[] v1,double[] v2)
	{
		return 0.0;
	}
	
	/**
	 * @comment:切比雪夫距离
	 * @param v1
	 * @param v2
	 * @return:double
	 */
	public static double ChebyshevDistance(double[] v1,double[] v2)
	{
		return 0.0;
	}
	
	
	/**
	 * @comment:标准欧式距离
	 * @param v1
	 * @param v2
	 * @return:double
	 */
	public static double StandardizedEuclideanDistance(double[] v1,double[] v2)
	{
		return 0.0;
	}
	
	/**
	 * @comment:马氏距离
	 * @param v1
	 * @param v2
	 * @return:double
	 */
	public static double MahalanobisDistance(double[] v1,double[] v2)
	{
		return 0.0;
	}
	
	/**
	 * @comment:巴氏距离
	 * @param v1
	 * @param v2
	 * @return:double
	 */
	public static double BhattacharyyaDistance(double[] v1,double[] v2)
	{
		return 0.0;
	}
	
	
	/**
	 * @comment:汉明距离
	 * @param v1
	 * @param v2
	 * @return:double
	 */
	public static double HammingDistance(double[] v1,double[] v2)
	{
		return 0.0;
	}
	
	
	/**
	 * @comment:夹角余弦
	 * @param v1
	 * @param v2
	 * @return:double
	 */
	public static double CosineDistance(double[] v1,double[] v2)
	{
		return 0.0;
	}
	
	/**
	 * @comment:杰卡德相似系数
	 * @param v1
	 * @param v2
	 * @return:double
	 */
	public static double JaccardSimilarityCoefficient(double[] v1,double[] v2)
	{
		return 0.0;
	}
	
	
	/**
	 * @comment:皮尔逊系数
	 * @param v1
	 * @param v2
	 * @return:double
	 */
	public static double PearsonCorrelationCoefficient(double[] v1,double[] v2)
	{
		return 0.0;
	}
	

}
