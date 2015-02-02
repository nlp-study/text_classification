package doc.preprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




import base.CodePrase;
import syntax.word.StopWords;
import syntax.word.WordTagging;
import util.FileRead;

public class DocPreprocess {
	private StopWords stopWords = new StopWords();

	//这个需要删除，硬编码太深
	public void init(String path) throws IOException
	{
		stopWords.addWords(path+"stopword/cn_stopwords.txt");
		stopWords.addWords(path+"stopword/english_stopwords.txt");
		stopWords.addWords(path+"stopword/punc_stopwords.txt");		
	}
	
	public void init(StopWords stopWords) throws IOException
	{
		this.stopWords = stopWords;
	}
	
	
	/**
	 * @comment:将文本处理成词向量：全半角转换，去停用词
	 * @param path
	 * @return
	 * @throws IOException
	 * @return:List<String>
	 */
	public List<String> doc2Words(String path) throws IOException
	{
		String doc = FileRead.readFile(path);
		doc = CodePrase.full2HalfChange(doc);
		List<String> words = WordTagging.segForWord(doc);
		List<String> result = new ArrayList<String>();
		
		for(String word:words)
		{
			if(!stopWords.isStopWord(word))
			{
				result.add(word);
			}
		}
		words.clear();
		return result;
	}

}
