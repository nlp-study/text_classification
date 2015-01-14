package feature_selection.chi_square;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import base.ClassName;
import util.WriteFile;
import dict.inverted_index.IndexDocRecord;
import dict.inverted_index.InvertedIndex;
import dict.inverted_index.InvertedIndexBuilder;
import dict.inverted_index.InvertedIndexReader;
import file.classify.FileClassifyList;

/**
 * @author xiaohe
 *  建立单个类的卡方分布
 */
public class ChiSquareBuilder {
	Logger logger = Logger.getLogger(ChiSquareBuilder.class);

	String className;
	InvertedIndex totalInvertedIndex = new InvertedIndex();
	InvertedIndex invertedIndex = new InvertedIndex();
	FileClassifyList fileProcess = new FileClassifyList();
	Set<String> words = new HashSet<String>();
	Map<String,Double> features = new LinkedHashMap<String,Double>();
	
    //本类的词典所在的目录
	String classPath ="";
	
	int alldocNumber = 0;    //所有类包含文档的数量
	int docNumber = 0;  //该类别中文档的数量
	
	public ChiSquareBuilder()
	{
		
	}
	
	public void init(String path,String className,
			InvertedIndex totalInvertedIndex,FileClassifyList tempFileProcess ) throws IOException
	{
		logger.info("start:'"+path+"'");
		this.classPath = path;
		this.className = className;
		this.fileProcess = new FileClassifyList(tempFileProcess);
		this.totalInvertedIndex = new InvertedIndex(totalInvertedIndex);
		invertedIndex = inputInvertedIndex(classPath);
		
		checkoutWords();
		alldocNumber = fileProcess.size();
		docNumber = fileProcess.numbForClass(className);
		
		logger.info("totalInvertedIndex:"+totalInvertedIndex.size()+" invertedIndex:"+invertedIndex.size());
	}
	
	public void init(String path,String thisPath,String className) throws IOException
	{
		logger.info("start");
		this.className = className;
		fileProcess.recoverFileCategs(path+"file/file_index.txt");
		totalInvertedIndex = inputInvertedIndex(path+"dict/InvertedIndex.txt");
		invertedIndex = inputInvertedIndex(path+thisPath);
		
		checkoutWords();
		alldocNumber = fileProcess.size();
		docNumber = fileProcess.numbForClass(className);
	}

	/**
	 * @comment:找出当前类的所有词
	 * @return:void
	 */
	private void checkoutWords()
	{
		words = invertedIndex.getWordsSet();
	}
	
	public InvertedIndex inputInvertedIndex(String path) throws IOException
	{
		InvertedIndexReader invertedIndexReader = new InvertedIndexReader();
		return invertedIndexReader.readInvertedIndex(path);
	}
	
	public void clear()
	{
		totalInvertedIndex.clear();
		invertedIndex.clear();		
		fileProcess.clear();
		words.clear();
		features.clear();
		
		alldocNumber = 0;	
		docNumber = 0;
		className = "";
	}
	
	/**
	 * 
	 * @return void
	 * @comment:計算每個詞的卡方特征值，然後排序
	 */
	public void calculateValue()
	{
		logger.info("start");
		
		for(String word:words)
		{
			//logger.info("calculate word:"+word);
			ChiUnit chiUnit = calculateABCD(word);
			Double value = ChiSquare.excute(chiUnit);
			features.put(word, value);
		}
		
		sortedFeatureVaule();
	}
	
	/**
	 * @param word
	 * @return void
	 * @comment:计算一个词所对应的A,B,C,D
	 */
	public ChiUnit calculateABCD(String word)
	{
		ChiUnit chiUnit = new ChiUnit();
		int A=0,B=0,C=0,D=0;
		A = invertedIndex.docNumb(word);
		B = totalInvertedIndex.docNumb(word) - A;
		C = docNumber - A;
		D = alldocNumber - docNumber - B;
		
		chiUnit.setA(A);
		chiUnit.setB(B);
		chiUnit.setC(C);
		chiUnit.setD(D);
		return chiUnit;
	}
	
	/**
	 * @return:void
	 * @comment:将结果由多到少排列
	 */
	public void sortedFeatureVaule()
	{
		if (features != null && !features.isEmpty()) {
	        List<Map.Entry<String, Double>> entryList = new ArrayList<Map.Entry<String, Double>>(features.entrySet());  
	        Collections.sort(entryList,  
	                new Comparator<Map.Entry<String, Double>>() {  
	                    public int compare(Entry<String, Double> entry1,  
	                    		Entry<String, Double> entry2) {
	                    	if ((entry2.getValue() - entry1.getValue()) > 0)
								return 1;
							else if ((entry2.getValue() - entry1.getValue()) == 0)
								return 0;
							else
								return -1; 
	                    }  
	                });  
	        Iterator<Map.Entry<String, Double>> iter = entryList.iterator();  
	        Map.Entry<String, Double> tmpEntry = null; 
	        features.clear();
	        
	        while (iter.hasNext()) {  
	            tmpEntry = iter.next();  
	            features.put(tmpEntry.getKey(), tmpEntry.getValue());  
	        }  
	    }  
	}
	
	public void printlnMap(String classname,String path)
	{
		List<String> tempList = new ArrayList<String>();
		for(String key:features.keySet())
		{
			tempList.add(key+"="+features.get(key));
		}
		
		try {
			WriteFile.writeFile(tempList, path+"chi_square/"+classname+".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		PropertyConfigurator.configure("log4j.properties");
		ChiSquareBuilder chiSquareBuilder = new ChiSquareBuilder();
		String className = "ART";
		chiSquareBuilder.init("data/result/", "dict/"+className+"-invertedIndex.txt", className);
		chiSquareBuilder.calculateValue();
		chiSquareBuilder.printlnMap(className,"data/result/");
	}
	
}
