package validation.slice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import validation.Iris;
import base.InputFeature;

public class KFolderBase {
	Logger logger = Logger.getLogger(KFolderDataSlice.class);

	//测试数据占所有数据的
		private  int crossNumb = 10;
		
		private int stepSize;
		private int step;
		int size;
		private int[] stones;
		
		//最终求出来的用于交叉验证的id，需要和输入的特征配合使用
		List<ValidationID> verificationIDs = new ArrayList<ValidationID>();
		
		List<Integer> randoms = new ArrayList<Integer>();
	   
		
		public KFolderBase(List<Integer> inputFeature,int crossNumb)
		{
			this.randoms = inputFeature;
			size = inputFeature.size();
			this.crossNumb = crossNumb;
		}
		
		public List<ValidationID> getVerificationIDs() {
			return verificationIDs;
		}

		public void init()
		{
			stepSize = size / crossNumb;
			
			if(stepSize == 0)
			{
				stepSize = 1;
				step = size / stepSize;
			}
			else
			{
				step = crossNumb;
			}
			
			stones = new int[step];
		}
		
		public void excute()
		{
			init();
			shuffle();
			calculateSection();
			calculateVerification();
			addForK();
		}
		
		public void shuffle()
		{
//			for(int i=0;i<size;++i)
//			{
//				randoms.add(i);
//			}
			logger.info("shuffle:"+randoms);
			Collections.shuffle(randoms);
		}
		
		public void calculateSection()
		{
			stones[0] = 0;
			for(int i=1;i<step;++i)
			{
				stones[i] = stones[i-1] + stepSize;
			}
		}
		
		public void calculateVerification()
		{
			for(int i=0;i<stones.length;++i)
			{
				if(i < stones.length-1)
				{
					Set<Integer> test = new HashSet<Integer>();
					test.addAll(randoms.subList(stones[i], stones[i+1]));
					
					Set<Integer> all = new HashSet<Integer>();
					all.addAll(randoms);
					
					all.removeAll(test);
					
					ValidationID verificationID = new ValidationID(all,test);
					verificationIDs.add(verificationID);
				}
				if(i == stones.length-1)
				{
					Set<Integer> test = new HashSet<Integer>();
					test.addAll(randoms.subList(stones[i], randoms.size()-1));
					
					Set<Integer> all = new HashSet<Integer>();
					all.addAll(randoms);
					
					all.removeAll(test);
					
					ValidationID verificationID = new ValidationID(all,test);
					verificationIDs.add(verificationID);
				}
			}
		}
		
		public void addForK()
		{
			while(verificationIDs.size() < crossNumb)
			{
				System.out.println("addForK");
				int size = verificationIDs.size()-1;
				verificationIDs.add(verificationIDs.get(size));
			}
		}
		
		public void showVerificationID()
		{
			for(int i=0;i<verificationIDs.size();++i)
			{
				System.out.println(verificationIDs.get(i));
			}
		}
		
		public void checkVerifications()
		{
			for(int i=0;i<verificationIDs.size();++i)
			{
				System.out.println("i:"+verificationIDs.get(i).checkValidity(randoms.size()));
			}
			
			showVerificationID();
		}
		
		public static void main(String[] args) throws Exception {
			String path = "data/corpus/iris.data";
			Iris iris = new Iris();
			iris.readData(path);
			InputFeature inputFeature = iris.getInputFeature();
			
//			KFolderDataSliceLess crossVerification = new KFolderDataSliceLess(inputFeature);
//			crossVerification.excute();
//			crossVerification.checkVerifications();
			
			List<Integer> temp = new ArrayList<Integer>();
			for(int i=0;i<150;++i)
			{
				temp.add(i);
			}
			
			KFolderBase kFolderBase = new KFolderBase(temp,10);
			kFolderBase.excute();
			kFolderBase.checkVerifications();
		}

}
