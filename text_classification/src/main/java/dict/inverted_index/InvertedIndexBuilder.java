package dict.inverted_index;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import syntax.word.StopWords;

public class InvertedIndexBuilder {
	Logger logger = Logger.getLogger(InvertedIndexBuilder.class);
	
	private StopWords stopWords = new StopWords();
	private InvertedIndex invertedIndex = new InvertedIndex();
	private WordFreq wordFreq = new WordFreq();
	private WordNumb wordNumb = new WordNumb();
	private int totalNumber = 0;
	
	private DictBuilder dictBuilder = new DictBuilder();
	
	public InvertedIndexBuilder()
	{
		
	}
	
	public void init(StopWords stopWords)
	{
		this.stopWords = stopWords;
	}
	
		
	public void  build(List<String> words,int id)
	{
		for(int i=0;i<words.size();++i)
		{
			String word = words.get(i).trim();
			
			if(!isStopword(word))
			{
				invertedIndex.insert(word, id, i);
			}
		}
	}
	
	public boolean isStopword(String input)
	{
		
		
		if(stopWords.isStopWord(input))
		{
			return true;
		}
		
		return false;
	}
	

	
	/**
	 * @return:void
	 * @comment:由倒排表创建词典
	 */
	public void createDict()
	{
		if(invertedIndex.size() == 0 )
		{
			logger.error("请先建立倒排表！倒排表为空");
			return;
		}
		
		dictBuilder.setInvertedIndex(invertedIndex);
		dictBuilder.createDict();
	}
	
	private void buildWordFreq()
	{
		logger.info("start");
		Set<String> wordSet = invertedIndex.getWordsSet();
		
		for(String word:wordSet)
		{
			Double freq = (double)wordNumb.getNumb(word)/(double)totalNumber;
			wordFreq.add(word, freq);
		}
		wordFreq.sortByFreq();
	}
	
	
	private void buildWordNumb()
	{
		Set<String> wordSet = invertedIndex.getWordsSet();
		for(String word:wordSet)
		{
			wordNumb.add(word, invertedIndex.getWordNumb(word));
		}
		wordNumb.sortByNumb();
	}
	
	/**
	 * 
	 * @return void
	 * @comment:计算总共有多少个词
	 */
	private void calculateAllNumber()
	{
		int numb = 0;
		for(String word:wordNumb.getWordSet())
		{
			numb += wordNumb.getNumb(word);
		}
		totalNumber = numb;
	}
	
	/**
	 * @param path
	 * @throws IOException
	 * @return void
	 * @comment:保存倒排表到文件
	 */
	public void saveInvertedIndex(String path) throws IOException
	{
		logger.info("path:"+path);
		invertedIndex.writeFile(path+"invertedIndex.txt");
//		wordFreq.writeFile(path+"wordFreq.txt");
//		wordNumb.writeFile(path+"wordNumb.txt");
		dictBuilder.saveDict(path);
	}
	
	public void clear()
	{
		invertedIndex.clear();
		wordFreq.clear();
		wordNumb.clear();
		totalNumber = 0;
		dictBuilder.clear();
	}
	
	public boolean empty()
	{
		return invertedIndex.empty();
	}
	
	
	
	public static void main(String[] args)
	{
		InvertedIndexBuilder invertedIndexBuilder = new InvertedIndexBuilder();
		
	}
}
