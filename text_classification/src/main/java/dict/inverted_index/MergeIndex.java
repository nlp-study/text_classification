package dict.inverted_index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class MergeIndex {
	Logger logger = Logger.getLogger(MergeIndex.class);
	
	InvertedIndex invertedIndex = new InvertedIndex();
	DictBuilder dictBuilder  = new DictBuilder();

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public InvertedIndex getInvertedIndex() {
		return invertedIndex;
	}

	public void setInvertedIndex(InvertedIndex invertedIndex) {
		this.invertedIndex = invertedIndex;
	}

	public void merge(String path) throws IOException {
		InvertedIndex tempIndex =  readInvertedIndex(path);
		invertedIndex.merge(tempIndex);
	}
	
	public void merge(InvertedIndex tempIndex) {
		invertedIndex.merge(tempIndex);
	}

	public InvertedIndex readInvertedIndex(String path) throws IOException {
		InvertedIndex invertedIndex = new InvertedIndex();
		BufferedReader buffReader = new BufferedReader(new FileReader(path));
		String str = "";
		int i=0;
		while((str = buffReader.readLine()) != null)
		{
			logger.info("input file:"+path+" id:"+i);
			str2InvertedIndex(str,invertedIndex);
			++i;
		}
		
		
		return invertedIndex;
	}

	private void str2InvertedIndex(String input,InvertedIndex invertedIndex) {
		String word = "";
		List<IndexDocRecord> docRecords = new ArrayList<IndexDocRecord>();

		String regex = ".*(?=-\\[)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		if (matcher.find()) {
			word = matcher.group();
		}

		String doc = "";
		IndexDocRecord docRecord = new IndexDocRecord();

		regex = "\\d+\\-[^\\s\\[]+(?=,\\s|\\])";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(input);

		while (matcher.find()) {
			doc = matcher.group();
			docRecord = anlysisDocRecord(doc);
			docRecords.add(docRecord);
		}
		
		invertedIndex.insert(word, docRecords);
	}

	private IndexDocRecord anlysisDocRecord(String input) {
		IndexDocRecord indexDocRecord = new IndexDocRecord();
		if (!input.contains("-")) {
			logger.error("输入的文本错误");
		}

		int pos = input.indexOf("-");
		String doc = input.substring(0, pos);
		String temPos = input.substring(pos + 1, input.length());
		String[] posArray = temPos.split(",");

		List<Integer> wordPos = new ArrayList<Integer>();

		for (String str : posArray) {
			wordPos.add(Integer.parseInt(str));
		}

		indexDocRecord.setDocID(Integer.parseInt(doc));
		indexDocRecord.setPos(wordPos);

		return indexDocRecord;

	}
	
	public void CreateDict()
	{
		dictBuilder.clear();
		dictBuilder.setInvertedIndex(invertedIndex);
		dictBuilder.createDict();
	}
	
	public void writeInvertedIndex(String path) throws IOException
	{
		invertedIndex.writeFile(path);
	}
	
	public void writeDict(String path) throws IOException
	{
		logger.info("path:"+path);
		dictBuilder.saveDict(path);
	}
	
	public void clear()
	{
		
	}

	public static void main(String[] args) throws IOException {
		MergeIndex mergeIndex = new MergeIndex();

		mergeIndex.readInvertedIndex("data/result/C11-Space-invertedIndex.txt");
		
	}

}
