package vsm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

public class FeatureMergerSelect {
	Logger logger = Logger.getLogger(FeatureMergerSelect.class);
	Set<String> types = new HashSet<String>(); // 所有的类别
	
	List<String> features = new ArrayList<String>();
	List<Double> scores = new ArrayList<Double>();
	
	public void init(String path) throws IOException
	{
		buildTypes(path);
		selectFeatureWord(path);
	}
	
	/**
	 * @comment: 通过卡方提取的结果的文件名字，解析目前有多少类，把各个类都取出
	 * @param path
	 * @return:void
	 */
	public void buildTypes(String path) {
		File file = new File(path);
		String[] filename = file.list();

		for (int i = 0; i < filename.length; ++i) {
			logger.info(filename[i]);
			String[] tempStr = filename[i].split("\\.");
			System.out.println(Arrays.asList(tempStr));
			String str = tempStr[0];

			types.add(str);
		}
		logger.info(types);
	}
	
	
	public void selectFeatureWord(String path) throws IOException
	{
		if(types.size() == 0)
		{
			logger.error("Class is empty!");
		}
		
		for(String type:types)
		{
			String tempPath = path+type+".txt";
			selectWord(tempPath);
		}
	}
	
	/**
	 * @comment:提取每个类的特征值
	 * @param path
	 * @return
	 * @throws IOException
	 * @return:List<String>
	 */
	public void selectWord(String path) throws IOException {
		logger.info("path:"+path);
		
		BufferedReader breader = new BufferedReader(new FileReader(path));
		String tempStr;
		int i = 0;
		while ((tempStr = breader.readLine()) != null) {
			
				String[] tempArray = tempStr.split("=");
//				logger.info("content:"+tempStr);
				String str = tempArray[0];
				double value = Double.parseDouble(tempArray[1]);
				insertFeature(str,value);
				++i;
		}
	}
	
	
	public void insertFeature(String word,double score)
	{
		if(features.contains(word))
		{
			int index = features.indexOf(word);
			double temp = scores.get(index);
			
			if(score>temp)
			{
				scores.set(index, score);
			}
			return;
		}
		else
		{
			for(int i=0;i<scores.size();++i)
			{
				if(score>scores.get(i))
				{
					features.add(i,word);
					scores.add(i,score);
					return;
				}
			}
			
			features.add(word);
			scores.add(score);
		}
	}
	
	public void writeFeatureSet(String path) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		
		for(int i=0;i<features.size();++i)
		{
			String str = features.get(i)+"="+scores.get(i)+"\r\n";
			bw.write(str);
		}
		
		bw.close();
	}

}
