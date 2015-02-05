package vsm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import dict.inverted_index.InvertedIndexBuilder;

/**
 * @author xiaohe
 * 从所有的类别中提取词组成一个公共的词典
 */
public class VSMWordSelector {
	Logger logger = Logger.getLogger(VSMWordSelector.class);
	
	DocWordSelector docWordSelector = new DocWordSelector();
	Set<String> words = new LinkedHashSet<String>();  //所有作为特征的词
	Map<String,Integer> featureWords = new HashMap<String,Integer>();
	
	public void init(String path) throws IOException
	{
		docWordSelector.init(path);		
		
		selectFeatureWord(path);
		setNumbForFeature();
	}
	
	
	
	public void selectFeatureWord(String path) throws IOException
	{
		if(docWordSelector.typeSize() == 0)
		{
			logger.error("Class is empty!");
		}
		
		for(String type:docWordSelector.getTypes())
		{
			String tempPath = path+type+".txt";
			List<String> tempWords = docWordSelector.selectWord(tempPath);
			addFeatureWords(tempWords);
		}
	}
	
	public void addFeatureWords(List<String> tempWords)
	{
		for(String str:tempWords)
		{
			words.add(str);
		}
	}
	
	public void setNumbForFeature()
	{
		logger.info("特征词的数量："+words.size());
		int i=0;
		for(String word:words)
		{
			featureWords.put(word, i);
			++i;
		}
		
	}
	
	
	public void writeFeatureWords(String path) throws IOException
	{
		BufferedWriter buffwrite = new  BufferedWriter(new FileWriter(path));
		
		for(String str:featureWords.keySet())
		{
			buffwrite.write(str+" "+featureWords.get(str)+"\r\n");
		}
		
		buffwrite.close();
	}
	
	
	public static void main(String[] args) throws IOException
	{
		VSMWordSelector vsmBuilder = new VSMWordSelector();
		String path = "data/result/chi_square/";
		vsmBuilder.init(path);
		vsmBuilder.writeFeatureWords("data/train/vsmword.txt");
	}
	
	
	
	

}
