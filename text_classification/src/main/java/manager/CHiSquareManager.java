package manager;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import base.ClassName;
import dict.inverted_index.InvertedIndex;
import dict.inverted_index.InvertedIndexReader;
import feature_selection.chi_square.ChiSquareBuilder;
import file.classify.FileClassifyList;

/**
 * @author xiaohe
 *  建立所有类别的卡方分布
 */
public class CHiSquareManager {
	Logger logger = Logger.getLogger(CHiSquareManager.class);

	
	ChiSquareBuilder chiSquareBuilder = new ChiSquareBuilder();
	
	FileClassifyList fileProcess = new FileClassifyList();
	
	//所有类别的总的倒排表
	InvertedIndex totalInvertedIndex = new InvertedIndex();
	
	//存放结果的路径
	String path;

	public void init(String path,FileClassifyList fileProcess) throws IOException
	{
		logger.info("start");
		this.path = path;
		this.fileProcess = fileProcess;
		//fileProcess.recoverFileCategs(path+"file/file_index.txt");
		totalInvertedIndex = inputInvertedIndex(path+"dict/InvertedIndex.txt");
	}
	
	public InvertedIndex inputInvertedIndex(String path) throws IOException
	{
		logger.info("start");
		
		InvertedIndexReader invertedIndexReader = new InvertedIndexReader();
		return invertedIndexReader.readInvertedIndex(path);
	}
	
	/**
	 * @comment:循环处理，提取各个类的特征
	 * @throws IOException
	 * @return:void
	 */
	public void excute() throws IOException
	{
		logger.info("start");
		
		for(String className:ClassName.getNames())
		{
			logger.info("process names:"+className);
			chiSquareBuilder.clear();
			logger.info("处理完之后："+fileProcess.size());
			String resultPath = path+"dict/"+className+"-invertedIndex.txt";
			chiSquareBuilder.init(resultPath,className,totalInvertedIndex,fileProcess);
			chiSquareBuilder.calculateValue();
			chiSquareBuilder.printlnMap(className,path);
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		PropertyConfigurator.configure("log4j.properties");
		CHiSquareManager chiSquareManager = new CHiSquareManager();
		
//		chiSquareManager.init("data/result/");
//		chiSquareManager.excute();
		
	}

}
