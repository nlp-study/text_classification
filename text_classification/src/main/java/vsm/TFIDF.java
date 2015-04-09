package vsm;

/**
 * @author xiaohe
 * 创建于：2015年4月8日
 * TF-IDF的实现，
 */
public class TFIDF {
		
	/**
	 * @comment:
	 * @param tNumb  一篇文档中该term的数量
	 * @param docTermNumb  一偏文档中所有词的数量
	 * @return
	 * @return:double
	 */
	private double calcuateTF(int tNumb,int docTermNumb)
	{
		if(tNumb == 0)
		{
			return 0;
		}
		
		return (double)tNumb/docTermNumb;
	}
	
	/**
	 * @comment:
	 * @param idNumb 包含该词的文档的数量
	 * @param docNumb 文档的数量
	 * @return
	 * @return:double
	 */
	private double calculateIDF(int idNumb,int docNumb)
	{
		return Math.log((double)docNumb/(idNumb+1));
	}
	
	public double calculateTFIDF(int tNumb,int docTermNumb,int idNumb,int docNumb)
	{
		double tf = calcuateTF( tNumb,docTermNumb);
		double idf = calculateIDF(idNumb,docNumb);
		return tf*idf;
	}
}
