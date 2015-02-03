package feature_selection.base;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import dict.inverted_index.MergeIndex;
import util.FileRead;

public class BaseFeatureSelection {
	Logger logger = Logger.getLogger(BaseFeatureSelection.class);

	Map<String,Integer> featureWords = new HashMap<String,Integer>();
    List<String> words = new ArrayList<String>();
	String path;
	
    public BaseFeatureSelection(List<String> words)
	{
		this.words = words;
	}
    
    /**
     * @param wordPath
     * @throws IOException
     * 输入是表示词的数量的词表
     */
    public BaseFeatureSelection(String wordPath) throws IOException
   	{
   		this.path = wordPath;
   	}
    
    public void init() throws IOException
    {
    	List<String> tempWords = FileRead.readLine(path);
    	
    	for(int i=0;i<tempWords.size();++i)
    	{
    		String regex  = ".*(?=-\\d)";
    		Pattern pattern  = Pattern.compile(regex);
    		Matcher match  = pattern.matcher(tempWords.get(i));
    	    
    		if(match.find())
    		{
    			String tempWord = match.group();
    			
    			if(!isEnglish(tempWord))
    			{
    				words.add(tempWord);
    			}
    			
//    			logger.info(tempWord);
    		}
//    		String[] temp = tempWords.get(i).split("-");
//    		if(temp.length != 2)
//    		{
//    			logger.error("输入的词表不对");
//    			return;
//    		}
//    		else{
//    			words.add(temp[0]);
//    		}
    	}
    }
    
    public boolean isEnglish(String str)
    {
    	return str.matches(".*[a-zA-Z].*");
    }
    
    public void execute()
    {
    	logger.info("start");
    	
    	int i=0;
    	for(String word:words)
    	{
    		if(!featureWords.containsKey(word))
    		{
    			featureWords.put(word, i);
    			++i;
    		}
    	}
    }
    
    
    public void clear()
    {
    	featureWords.clear();
    	words.clear();
    }
    
    public void writeFeature(String path) throws IOException
    {
    	logger.info("start");
    	
        BufferedWriter buffwrite = new  BufferedWriter(new FileWriter(path));
		
		for(String str:featureWords.keySet())
		{
			int temp = featureWords.get(str);
			String numb = temp+"";
			buffwrite.write(str+" "+numb+"\r\n");
//			logger.info(featureWords.get(str));
		}
		
		buffwrite.close();
    }
    
    
	
	

}
