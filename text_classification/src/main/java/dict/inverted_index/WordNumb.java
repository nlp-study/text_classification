package dict.inverted_index;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class WordNumb {
	private Map<String,Integer> wordNumb = new LinkedHashMap<String,Integer>();
	
	public void add(String word,Integer numb)
	{
		wordNumb.put(word, numb);
	}
	
	public int size()
	{
		return wordNumb.size();
	}
	
	public int getNumb(String word)
	{
		return wordNumb.get(word);
	}
	
	public Set<String> getWordSet()
	{
		return wordNumb.keySet();
	}
	
	public void writeFile(String path) throws IOException
	{
		if(wordNumb.size() == 0)
		{
			return ;
		}
		
		File file = new File(path);
		if(!file.exists())
		{
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file);
		
        BufferedWriter bwriter = new BufferedWriter(fw);
        
        for(String str:wordNumb.keySet())
		{
        	bwriter.write(str);
        	bwriter.write("-");
        	bwriter.write(wordNumb.get(str)+"\r\n");
		}
		
		bwriter.close();
		fw.close();		
	}	
	
	public void clear()
	{
		wordNumb.clear();
	}
		
	public void sortByNumb() {
		
	    if (wordNumb != null && !wordNumb.isEmpty()) {
	        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(wordNumb.entrySet());  
	        Collections.sort(entryList,  
	                new Comparator<Map.Entry<String, Integer>>() {  
	                    public int compare(Entry<String, Integer> entry1,  
	                            Entry<String, Integer> entry2) {  
	                        int value1 = 0, value2 = 0;  
	                        try {  
	                            value1 = entry1.getValue();  
	                            value2 = entry2.getValue();  
	                        } catch (NumberFormatException e) {  
	                            value1 = 0;  
	                            value2 = 0;  
	                        }  
	                        return value2 - value1;  
	                    }  
	                });  
	        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();  
	        Map.Entry<String, Integer> tmpEntry = null; 
	        wordNumb.clear();
	        while (iter.hasNext()) {  
	            tmpEntry = iter.next();  
	            wordNumb.put(tmpEntry.getKey(), tmpEntry.getValue());  
	        }  
	    }  
	}  
	
	
	public static void main(String[] args)
	{
		WordNumb wordNumb = new WordNumb();
		wordNumb.add("1",11);
		wordNumb.add("2",2);
		wordNumb.add("3",3);
		
		System.out.println();
	}
	
	
}
