package vsm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class DocWordSelector {
	Logger logger = Logger.getLogger(DocWordSelector.class);

	public static final int EVERY_CLASS_FEATURE_SIZE = 300;	
	Set<String> types = new HashSet<String>(); // 所有的类别

	public void init(String path) throws IOException {
		PropertyConfigurator.configure("log4j.properties");
		buildTypes(path);
	}
	
	public Set<String> getTypes() {
		return types;
	}

	public void setTypes(Set<String> types) {
		this.types = types;
	}

	public int typeSize()
	{
		return types.size();
	}

	/**
	 * @comment: 通过卡方提取的结果的文件名字，解析目前有多少类，把各个类都取出
	 * @param path
	 * @return:void
	 */
	public void buildTypes(String path) {
		File file = new File(path);
		String[] filename = file.list();

		for (int i = 0; i < filename.length; ++i) {
			logger.info(filename[i]);
			String[] tempStr = filename[i].split("\\.");
			System.out.println(Arrays.asList(tempStr));
			String str = tempStr[0];

			types.add(str);
		}
		logger.info(types);
	}

	
	/**
	 * @comment:提取每个类的前 EVERY_CLASS_FEATURE_SIZE 个词添加到总的特征中
	 * @param path
	 * @return
	 * @throws IOException
	 * @return:List<String>
	 */
	public List<String> selectWord(String path) throws IOException {
		BufferedReader breader = new BufferedReader(new FileReader(path));

		String tempStr;
		List<String> words = new ArrayList<String>();

		int i = 0;
		while ((tempStr = breader.readLine()) != null) {
			if (tempStr.length() != 0 && i < EVERY_CLASS_FEATURE_SIZE) {
				String[] tempArray = tempStr.split("=");
				tempStr = tempArray[0];
				words.add(tempStr);
				++i;
			}
		}

		logger.info(words);
		return words;
	}

	



	

	public static void main(String[] args) throws IOException {
		

	}

}
