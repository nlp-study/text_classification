package util;

import org.apache.log4j.Logger;

public class MatrixOperation {
	static Logger logger = Logger.getLogger(MatrixOperation.class);
	
	public static double  errorSumSquares(double[][] v1 , double[][] v2)
	{
		if(v1.length != v2.length)
		{
			logger.error("输入的两个向量维度不等！");
			return -1;
		}
		double sum = 0;
		
		for(int i=0;i<v1.length;++i)
		{
			if(v1[i].length != v2[i].length)
			{
				logger.error("输入的两个向量维度不等！");
			    return -1;
				
			}
			
			for(int j=0;j<v1[i].length;++j)
			{
				sum+= Math.pow((v1[i][j] - v2[i][j]),2);
			}
			
		}
		
		sum = Math.sqrt(sum);
		
		return sum;
	}

}
