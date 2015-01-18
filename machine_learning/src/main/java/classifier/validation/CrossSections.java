package classifier.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.ClassDistribution;
import base.InputFeature;

/**
 * @author xiaohe
 * 2015年1月17日
 * 产生交叉验证的数据
 */
public class CrossSections {
	//测试数据占所有数据的
	private static final int CROSS_NUMB = 10;
	
	private int stepSize;
	private int step;
	InputFeature inputFeature;
	int size;
	private int[] stones;
	List<ValidationID> verificationIDs = new ArrayList<ValidationID>();
	
	List<Integer> randoms = new ArrayList<Integer>();
   
	
	public CrossSections(InputFeature inputFeature)
	{
		this.inputFeature = inputFeature;
		size = inputFeature.getSize();
	}
	
	public List<ValidationID> getVerificationIDs() {
		return verificationIDs;
	}

	public void init()
	{
		stepSize = size / CROSS_NUMB;
		
		if(stepSize == 0)
		{
			stepSize = 1;
			step = size / stepSize;
		}
		else
		{
			step = CROSS_NUMB;
		}
		
		stones = new int[step];
	}
	
	public void excute()
	{
		init();
		shuffle();
		calculateSection();
		calculateVerification();
		
	}
	
	public void shuffle()
	{
		for(int i=0;i<size;++i)
		{
			randoms.add(i);
		}
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
		
		CrossSections crossVerification = new CrossSections(inputFeature);
		crossVerification.excute();
		crossVerification.checkVerifications();
		  
	}

}
