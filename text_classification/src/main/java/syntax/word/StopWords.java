package syntax.word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.util.FilterModifWord;
import org.apache.log4j.Logger;

import util.FileRead;
import dict.inverted_index.InvertedIndexBuilder;

public class StopWords {
	Logger logger = Logger.getLogger(StopWords.class);
	
	public static final String UTF8_BOM = "\uFEFF";
	
    private List<String> words = new ArrayList<String>();
    
    public void init(String path) throws IOException
    {
    	words = FileRead.readLine(path);    	
    }
    
    public void addWords(String path) throws IOException
    {
    	List<String> tempWords = FileRead.readLine(path);
    	words.addAll(tempWords);
    }

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}
	
	public void clear()
	{
		words.clear();
	}
	
	public boolean isStopWord(String word)
	{
		if(word.trim().length() == 0)
		{
			return true;
		}
		
		if(word.startsWith(UTF8_BOM))
		{
			return true;
		}
		
		if(word.matches(".*\\d+.*"))
		{
			return true;
		}
		
	    if(words.contains(word))
	    {
	    	return true;
	    }
	    
	    //排除了英文
	    if(word.matches(".*[a-zA-Z].*"))
	    {
	    	return true;
	    }
	    
	    return false;
	}
    
    
}
