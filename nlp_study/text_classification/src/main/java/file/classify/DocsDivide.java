package file.classify;

import java.io.IOException;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import base.ClassName;

public class DocsDivide {
	static Logger logger = Logger.getLogger(DocsDivide.class);

	public static final int DIVIDE_RATE = 10;

	/**
	 * @comment:将输入的文本分成两部分，一部分训练，一部分推导
	 * @param path 文本的路劲
	 * @param proportion  文本分隔的比例
	 * @param trainList 训练部分
	 * @param testList  推动部分
	 * @return void
	 */
	public static  void singleDivide(String path,int proportion, FileClassifyList trainList,FileClassifyList testList)
	{
		FileClassifyList allList = new FileClassifyList();
		allList.processFolder(path);
		
		singleDivide(allList, proportion, trainList, testList);
	}
	
	
	/**
	 * @comment:處理輸入的文檔鏈錶
	 * @param allList
	 * @param proportion
	 * @param trainList
	 * @param testList
	 * @return:void
	 */
	public static  void singleDivide(FileClassifyList allList,int proportion, FileClassifyList trainList,FileClassifyList testList)
	{
        logger.info(allList.size());
		
		int[][] startEnds =  divideIndex(allList,proportion);
		
		int j=0;
		for(int i=0;i<allList.size();++i)
		{
			logger.info(i);
			
			if(j<startEnds.length)
			{
				if(startEnds[j][0]<=i&&startEnds[j][1]>i)
				{
					testList.add(allList.get(i));
				}
				
				if(startEnds[j][1] == i)
				{
					testList.add(allList.get(i));
					j++;
				}
				
				if(j == 0)
				{
					if(startEnds[j][0]>i||(startEnds[j][1]<i && i < startEnds[j+1][0]))
					{
						trainList.add(allList.get(i));
					}
				}
				else 
				{
					if(startEnds[j-1][1]<i && i<startEnds[j][0])
					{
						trainList.add(allList.get(i));
					}
				}
			}
			else if(j==startEnds.length)
			{
				
				trainList.add(allList.get(i));
				
			}
		}
		
		processID(trainList);
		processID(testList);
	}
	
	/**
	 * @comment:获得每个类取的推导数据的起始位置和结束位置
	 * @param allList
	 * @return
	 * @return int[][]
	 */
	public static  int[][] divideIndex(FileClassifyList allList,int proportion)
	{
		String currentClass = "";
		String preClass = "";
		int i=0;
		int j=0;
		int[][] result = new int[ClassName.size()][2];
		
		for(FileCategory fileCategory:allList)
		{
			currentClass = fileCategory.getClassName();
			if(!currentClass.equals(preClass))
			{
				logger.info(fileCategory.toString());
				logger.info("classID:"+i+" j:"+j);
				
				result[i][0] = j;
				if(i != 0)
				{
					int size = j - result[i-1][0];
					int step = size/proportion;
					result[i-1][1] = result[i-1][0] + step;
				}
				i++;
			}
			preClass = currentClass;
			j++;
			
		}
		
		int size = j - result[i-1][0];
		int step = size/proportion;
		result[i-1][1] = result[i-1][0] + step;
		
		return result;
	}
	
	/**
	 * @comment:因为是从总的列表上取下来的值，所以文档id会不连续，这里把文档id整理成连续的
	 * @param trainList
	 * @return void
	 */
	public static void processID(FileClassifyList trainList)
	{
		for(int i=0;i<trainList.size();++i)
		{
			FileCategory fileCategory = trainList.get(i);
			fileCategory.setId(i);
			trainList.set(i, fileCategory);
		}
	}
	
	
	public static void main(String[] args) throws IOException
	{
		PropertyConfigurator.configure("log4j.properties");

		String path = "F:/分类数据/复旦语料库/answer/";
//		String path = "data/corpus/";	

        int proportion = 10;
        FileClassifyList trainList = new FileClassifyList();
        FileClassifyList testList = new FileClassifyList();
        
        DocsDivide.singleDivide(path,proportion,trainList,testList);
        
        trainList.writeFiles("data/temp/train.txt");
        testList.writeFiles("data/temp/infer.txt");
		
	}

}
