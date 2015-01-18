package validation.slice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import manager.SingleValidation;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import validation.Iris;
import base.InputFeature;
import base.InstanceD;

/**
 * @author xiaohe
 * 2015年1月18日
 * 按照k-foler的方式分隔训练和测试数据
 * 思路：
 *    1. 输入为类别和特征向量的列表，类别是用0~n的特征来表示
 *    2. 首先把输入中各个类的数据的id分开来
 *    3. 然后从各个类中按照k-folder分割数据，集中各个类的数据组成总的训练和测试数据
 */
public class KFolderDataSlice implements DataSlice{
	Logger logger = Logger.getLogger(KFolderDataSlice.class);
	    //测试数据占所有数据的
		private static final int CROSS_NUMB = 10;
		
		//记录测试数据的步长，即id是多少到多少是测试数据
		private Map<Integer,Integer> stepSize;
		//记录数据被分成多少份
		private Map<Integer,Integer> step;
		
		InputFeature inputFeature;
		int size;
		private int[] stones;
		
		/**
		 * 把输入中各个类对应的id分到各个类中,key 是classid,value 是对应的id
		 */
		Map<Integer,List<Integer>>  everyClassDistrip = new HashMap<Integer,List<Integer>>();
		
		//每个类分割的结果
		Map<Integer,List<ValidationID>> everyClassSlice = new HashMap<Integer,List<ValidationID>>();
		
		//最终求出来的用于交叉验证的id，需要和输入的特征配合使用
		List<ValidationID> verificationIDs = new ArrayList<ValidationID>();
		
		List<Integer> randoms = new ArrayList<Integer>();
	   
		
		public KFolderDataSlice()
		{
			
		}
		
		public List<ValidationID> getVerificationIDs() {
			return verificationIDs;
		}

		public void init(InputFeature inputFeature)
		{
			this.inputFeature = inputFeature;
			size = inputFeature.getSize();
		}
		
		public void excute()
		{
			analysisInput();
			selectEveryClass();
			mergeEveryClass();
		}
		
		/**
		 * @comment:
		 * @return void
		 * 将各个类别的数据分类
		 */
		public void analysisInput()
		{
			for(int i=0;i<size;++i)
			{
				int classid = inputFeature.getInstanceD(i).getType();
				if(everyClassDistrip.containsKey(classid))
				{
					//这里试一下，是否要重新put操作，理论上不要
					List<Integer> temp = everyClassDistrip.get(classid);
					temp.add(i);
					everyClassDistrip.put(classid, temp);
				}
				else
				{
					List<Integer> temp = new ArrayList<Integer>();
					temp.add(i);
					everyClassDistrip.put(classid, temp);			
				}
			}
//			logger.info(everyClassDistrip);
		}
		
		public void selectEveryClass()
		{
			for(Integer i:everyClassDistrip.keySet())
			{
				List<Integer> temp = everyClassDistrip.get(i);
				KFolderBase kFolderBase = new KFolderBase(temp,CROSS_NUMB);
				kFolderBase.excute();
				List<ValidationID> tempID = kFolderBase.getVerificationIDs();
//				logger.info(tempID);
				everyClassSlice.put(i, tempID);
			}
		}
		
		public void mergeEveryClass()
		{
			for(int i=0;i<CROSS_NUMB;++i)
			{
				Set<Integer> trainids = new HashSet<Integer>();
                Set<Integer> inferids = new HashSet<Integer>();
				
                for(Integer key:everyClassSlice.keySet())
                {
                	List<ValidationID> temp = everyClassSlice.get(key);
                	trainids.addAll(temp.get(i).getTrainids());
                	inferids.addAll(temp.get(i).getInferids());
                }
                
				ValidationID validationID = new ValidationID(trainids,inferids);
				verificationIDs.add(validationID);
			}
		}
		
		public void checkVerifications()
		{
			for(int i=0;i<verificationIDs.size();++i)
			{
				System.out.println("i:"+verificationIDs.get(i).checkValidity(size));
			}
			System.out.println(verificationIDs);
		}
		
		public static void main(String[] args) throws Exception {
			PropertyConfigurator.configure("log4j.properties");

			String path = "data/corpus/iris.data";
			Iris iris = new Iris();
			iris.readData(path);
			InputFeature inputFeature = iris.getInputFeature();
			
			KFolderDataSlice crossVerification = new KFolderDataSlice();
			crossVerification.init(inputFeature);
			crossVerification.excute();
			crossVerification.checkVerifications();
		}

}
