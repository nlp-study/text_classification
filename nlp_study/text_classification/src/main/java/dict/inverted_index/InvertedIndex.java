package dict.inverted_index;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class InvertedIndex {
	Logger logger = Logger.getLogger(InvertedIndex.class);
	
	private Map<String,List<IndexDocRecord>> index = new LinkedHashMap<String,List<IndexDocRecord>>();
	
	
	public InvertedIndex()
	{
		
	}
	
	public InvertedIndex(InvertedIndex invertedIndex)
	{
		index.putAll(invertedIndex.getIndex());
	}
	
	public Map<String,List<IndexDocRecord>> getIndex()
	{
		return index;
	}
	
	public Set<String> getWordsSet()
	{
		return index.keySet();
	}
	
	public int docNumb(String word)
	{
		List<IndexDocRecord> docRecord = index.get(word);
		if(docRecord == null)
		{
			logger.error("倒排表中不存在该词："+word);
			System.exit(1);
			return -1;
		}
		return docRecord.size();
	}
	
	public List<IndexDocRecord> getIndexDocRecord(String word)
	{
		return index.get(word);
	}
	
	public int size()
	{
		return index.size();
	}
	
	public void insert(String word,List<IndexDocRecord> docRecords)
	{
		index.put(word, docRecords);
	}
	
	public void insert(String word,int docID,int pos)
	{
		List<IndexDocRecord> indexDocRecords = index.get(word);
		
		if(indexDocRecords == null)
		{
			indexDocRecords = new ArrayList<IndexDocRecord>();
			IndexDocRecord indexDocRecord = new IndexDocRecord();
			indexDocRecord.setDocID(docID);
			indexDocRecord.insert(pos);
			indexDocRecords.add(indexDocRecord);
			index.put(word, indexDocRecords);
			return ;
		}
		
		insertDocRecord(docID,pos,indexDocRecords);
		index.put(word,indexDocRecords);
	}
	
	public void insertDocRecord(int docID,int pos,List<IndexDocRecord> indexDocRecords)
	{
		for(IndexDocRecord indexDocRecord: indexDocRecords)
		{
			if(indexDocRecord.getDocID() == docID)
			{
				indexDocRecord.insert(pos);
				return ;
			}
		}
		
		IndexDocRecord indexDocRecord = new IndexDocRecord();
		indexDocRecord.setDocID(docID);
		indexDocRecord.insert(pos);
		indexDocRecords.add(indexDocRecord);
	}
	
	public int getWordDocNumb(String word)
	{
		List<IndexDocRecord> indexDocRecords = index.get(word);
		return indexDocRecords.size();
	}	
	
	public int getWordNumb(String word)
	{
		int numb = 0;
		List<IndexDocRecord> indexDocRecords = index.get(word);
		for(IndexDocRecord record:indexDocRecords)
		{
			numb += record.size();
		}
		
		return numb;
	}
	
	public boolean empty()
	{
		if(index.size() == 0)
		{
			return true;
		}
		return false;
	}
	
	public void remove(String word)
	{
		if(index.keySet().contains(word))
		{
			index.remove(word);
		}
	}
	
	
	public void merge(InvertedIndex tempIndex)
	{
		for(String word:tempIndex.getWordsSet())
		{
			if(index.containsKey(word))
			{
				List<IndexDocRecord> docRecords = index.get(word);
				docRecords.addAll(tempIndex.getIndexDocRecord(word));
				index.put(word, docRecords);
			}
			else
			{
				List<IndexDocRecord> docRecords = tempIndex.getIndexDocRecord(word);
				index.put(word, docRecords);
			}
		}
	}
	
	
	public void writeFile(String path) throws IOException
	{
		if(index.size() == 0)
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
        
        for(String str:index.keySet())
		{
        	bwriter.write(str);
        	bwriter.write("-");
        	bwriter.write(index.get(str)+"\r\n");
		}
		
		bwriter.close();
		fw.close();		
	}
	
	
	public void clear()
	{
		index.clear();
	}

}
