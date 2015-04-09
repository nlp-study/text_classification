package dict.inverted_index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author xiaohe
 * 创建于：2015年4月8日
 * 读取倒排表的类，将倒排表的文档处理成对应的对象，以后成熟之后将改用java自带的序列文件
 */
public class InvertedIndexReader {
	Logger logger = Logger.getLogger(InvertedIndexReader.class);
	
	public InvertedIndex readInvertedIndex(String path) throws IOException {
		InvertedIndex invertedIndex = new InvertedIndex();
		BufferedReader buffReader = new BufferedReader(new FileReader(path));
		String str = "";
		int i=0;
		while((str = buffReader.readLine()) != null)
		{
			//logger.info("input file:"+path+" id:"+i);
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
}
