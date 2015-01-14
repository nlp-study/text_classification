package dict.inverted_index;

import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author xiaohe
 * 创建于：2014年12月30日
 * 词典创建类，主要是创建统计词的数量和词频的两个词典
 */
public class DictBuilder {
	Logger logger = Logger.getLogger(DictBuilder.class);
	
	private InvertedIndex invertedIndex = new InvertedIndex();
	private WordFreq wordFreq = new WordFreq();
	private WordNumb wordNumb = new WordNumb();
	private int totalNumber = 0;
	
	
	public DictBuilder()
	{
		
	}
	
	public DictBuilder(InvertedIndex invertedIndex)
	{
		this.invertedIndex = invertedIndex;
	}
	
	public InvertedIndex getInvertedIndex() {
		return invertedIndex;
	}

	public void setInvertedIndex(InvertedIndex invertedIndex) {
		this.invertedIndex = invertedIndex;
	}

	public void createDict()
	{
		if(invertedIndex.size() == 0 )
		{
			logger.error("请先建立倒排表！倒排表为空");
			return;
		}
		buildWordNumb();
		calculateAllNumber();
		buildWordFreq();
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
	public void saveDict(String path) throws IOException
	{
		logger.info("path:"+path);
		wordFreq.writeFile(path+"wordFreq.txt");
		wordNumb.writeFile(path+"wordNumb.txt");
	}
	
	
	public void clear()
	{
		wordFreq.clear();
		wordNumb.clear();
	}

}
