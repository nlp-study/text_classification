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

import org.apache.log4j.Logger;

public class WordFreq {
	Logger logger = Logger.getLogger(WordFreq.class);

	private Map<String, Double> index = new LinkedHashMap<String, Double>();

	public Double getFreq(String word) {
		return index.get(word);
	}

	public void add(String word, Double freq) {
		if (index.keySet().contains(word)) {
			logger.error("数据已经存在："+word);
		}
		index.put(word, freq);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (String str : index.keySet()) {
			sb.append(str);
			sb.append("-");
			sb.append(index.get(str));
		}
		return sb.toString();
	}

	public void writeFile(String path) throws IOException {
		if (index.size() == 0) {
			return;
		}

		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file);

		BufferedWriter bwriter = new BufferedWriter(fw);

		for (String str : index.keySet()) {

			bwriter.write(str);
			bwriter.write("-");
			bwriter.write(index.get(str) + "\r\n");
		}

		bwriter.close();
		fw.close();
	}

	public void clear() {
		index.clear();
	}

	public void sortByFreq() {

		if (index != null && !index.isEmpty()) {
			List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(
					index.entrySet());
			
			Collections.sort(list_Data,
					new Comparator<Map.Entry<String, Double>>() {
						public int compare(Map.Entry<String, Double> o1,
								Map.Entry<String, Double> o2) {
							if ((o2.getValue() - o1.getValue()) > 0)
								return 1;
							else if ((o2.getValue() - o1.getValue()) == 0)
								return 0;
							else
								return -1;
						}
					});

			Iterator<Map.Entry<String, Double>> iter = list_Data.iterator();
			Map.Entry<String, Double> tmpEntry = null;
			index.clear();
			while (iter.hasNext()) {
				tmpEntry = iter.next();
				index.put(tmpEntry.getKey(), tmpEntry.getValue());
			}
		}
	}

	public static void main(String[] args) {
		WordFreq wordFreq = new WordFreq();
		Double d = wordFreq.getFreq("1");
		System.out.println(d);
	}
}
