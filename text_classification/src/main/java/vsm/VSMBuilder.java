package vsm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import doc.preprocess.DocPreprocess;
import file.classify.FileClassifyList;
import syntax.word.StopWords;
import util.FileRead;

/**
 * @author xiaohe 从文本中提取vsm值组成向量
 */
public class VSMBuilder {
	Logger logger = Logger.getLogger(VSMBuilder.class);

	// key是词，value是词在词典中的编号
	Map<String, Integer> featureWords = new HashMap<String, Integer>();

	// 给每一个类别赋予一个id
	Map<String, Integer> classIDs = new HashMap<String, Integer>();

	// 每篇文档对应的vsm
	List<VSM> vsms = new ArrayList<VSM>();

	// 将文档处理成词向量
	DocPreprocess docPreprocess = new DocPreprocess();

	// 需要处理的文档的集合
	private FileClassifyList fileProcess = new FileClassifyList();

	// 特征词的数量
	int featureSize;

	public int getClassSize() {
		return classIDs.size();
	}

	public List<VSM> getVsms() {
		return vsms;
	}

	public void setVsms(List<VSM> vsms) {
		this.vsms = vsms;
	}

	public int getFeatureSize() {
		return featureSize;
	}

	public void setFeatureSize(int featureSize) {
		this.featureSize = featureSize;
	}

	public void init(String path, FileClassifyList fileProcess)
			throws IOException {
		buildFeatureWords(path + "result/train/vsmword.txt");
		featureSize = featureWords.size();
		VSM.setSize(featureSize);
		createClassMap(path + "result/file/classid.txt");
		docPreprocess.init(path);
		this.fileProcess = fileProcess;
	}

	public void init(String path, FileClassifyList fileProcess,
			StopWords stopWords) throws IOException {
		buildFeatureWords(path + "train/vsmword.txt");
		featureSize = featureWords.size();
		VSM.setSize(featureSize);
		createClassMap(path + "file/classid.txt");
		docPreprocess.init(stopWords);
		this.fileProcess = fileProcess;
	}

	public void createClassMap(String path) throws IOException {
		List<String> classids = FileRead.readLine(path);

		for (String str : classids) {
			String[] classid = str.split(" ");
			classIDs.put(classid[0], Integer.parseInt(classid[1]));
		}
	}

	public void buildFeatureWords(String path) throws IOException {
		BufferedReader buffWriter = new BufferedReader(new FileReader(path));
		String str;
		while ((str = buffWriter.readLine()) != null) {

			String word = "";
			Integer index = 0;
			String regex = ".+(?= \\d+)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			if (matcher.find()) {
				word = matcher.group();
			}

			regex = "(?<=.* )\\d+";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(str);
			if (matcher.find()) {
				index = Integer.parseInt(matcher.group());
			}

			featureWords.put(word, index);
		}

	}

	public void docs2vsms() throws IOException {
		for (int i = 0; i < fileProcess.size(); ++i) {
			String className = fileProcess.get(i).getClassName();
			int classid = classIDs.get(className);

			List<String> words = docPreprocess.doc2Words(fileProcess.get(i)
					.getFileame());
			VSM vsm = createBinarySVM(words, classid);
			vsms.add(vsm);
			words.clear();
			logger.info("共" + fileProcess.size() + "个文件，第" + i + "条");
		}
	}

	public void docs2vsms(String path) throws IOException {
		for (int i = 0; i < fileProcess.size(); ++i) {
			String className = fileProcess.get(i).getClassName();
			int classid = classIDs.get(className);

			List<String> words = docPreprocess.doc2Words(fileProcess.get(i)
					.getFileame());
			VSM vsm = createBinarySVM(words, classid);
			writeSingleVSMs(path, vsm);
			words.clear();
			logger.info("共" + fileProcess.size() + "个文件，第" + i + "条");
		}
	}

	public void writeSingleVSMs(String path, VSM vsm) throws IOException {
		BufferedWriter buffwrite = new BufferedWriter(new FileWriter(path,true));

		buffwrite.write(vsm.getType() + ":");
		double[] tempVSM = vsm.getVector();

		for (double temp : tempVSM) {
			buffwrite.write(temp + " ");
		}
		buffwrite.write("\r\n");

		buffwrite.close();
	}

	/**
	 * @comment:将类为type的文档生成向量
	 * @param words
	 * @param type
	 * @throws IOException
	 * @return VSM
	 */
	public VSM createBinarySVM(List<String> words, int typeID) throws IOException {

		VSM vsm = new VSM();
		double[] vector = words2vector(words);

		vsm.setType(typeID);
		vsm.setVector(vector);

		return vsm;
	}
	
	/**
	 * @comment:创建TFIDF类型的svm模型
	 * @param words
	 * @param typeID
	 * @return
	 * @throws IOException
	 * @return:VSM
	 */
	public VSM createTFIDFSVM(List<String> words, int typeID) throws IOException {

		VSM vsm = new VSM();
		double[] vector = words2TFIDFvector(words);

		vsm.setType(typeID);
		vsm.setVector(vector);

		return vsm;
	}

	public double[] words2vector(List<String> words) {
		double[] vector = new double[featureSize];

		for (int i = 0; i < vector.length; ++i) {
			vector[i] = 0.0;
		}

		for (String word : words) {
			Integer pos = featureWords.get(word);
			if (pos == null) {
				continue;
			}
			vector[pos] = 1.0;
		}
		return vector;
	}
	
	public double[] words2TFIDFvector(List<String> words) {
		double[] vector = new double[featureSize];

		for (int i = 0; i < vector.length; ++i) {
			vector[i] = 0.0;
		}

		for (String word : words) {
			Integer pos = featureWords.get(word);
			if (pos == null) {
				continue;
			}
			vector[pos] = 1.0;
		}
		return vector;
	}

	public void countAjs() {
		for (VSM vsm : vsms) {
			for (int i = 0; i < VSM.size; ++i) {

			}
		}
	}

	public void writeVSMs(String path) throws IOException {
		BufferedWriter buffwrite = new BufferedWriter(new FileWriter(path));

		for (int i = 0; i < vsms.size(); ++i) {

			buffwrite.write(vsms.get(i).getType() + ":");
			double[] tempVSM = vsms.get(i).getVector();

			for (double temp : tempVSM) {
				buffwrite.write(temp + " ");
			}
			buffwrite.write("\r\n");

		}

		buffwrite.close();
	}

	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure("log4j.properties");
		FileClassifyList fileProcess = new FileClassifyList();
		String sourcePath = "C:/projectStudy/data/text_classify/data/answer/";
		fileProcess.processFolder(sourcePath);

		VSMBuilder vsmBuilder = new VSMBuilder();
		String path = "data/";
		// vsmBuilder.init(path,fileProcess);
		// vsmBuilder.docs2vsms();
		// vsmBuilder.writeVSMs("data/train/everyVsmword.txt");
	}

}
