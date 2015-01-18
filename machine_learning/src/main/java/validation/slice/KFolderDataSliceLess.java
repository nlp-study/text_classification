package validation.slice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.ClassDistribution;
import validation.Iris;
import base.InputFeature;

/**
 * @author xiaohe
 * 2015年1月17日
 * 产生交叉验证的数据，该数据分隔类没有考虑数据中各个类的分布情况，
 * 对那种各个种类的数据分布不均匀的数据集，
 * 可能会出现某个类没有训练而全部在测试集中的情况，
 * 因此这个数据分隔类只能用在类别比较少，而且各个类的数据都很多的情况下
 */
public class KFolderDataSliceLess 
{
	//测试数据占所有数据的
	private static final int CROSS_NUMB = 10;
	
	private int stepSize;
	private int step;
	InputFeature inputFeature;
	int size;
	private int[] stones;
	
	//最终求出来的用于交叉验证的id，需要和输入的特征配合使用
	List<ValidationID> verificationIDs = new ArrayList<ValidationID>();
	
	List<Integer> randoms = new ArrayList<Integer>();
   
	
	public KFolderDataSliceLess(InputFeature inputFeature)
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
		addForK();
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
	
	public void addForK()
	{
		while(verificationIDs.size() < CROSS_NUMB)
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
		
		KFolderDataSliceLess crossVerification = new KFolderDataSliceLess(inputFeature);
		crossVerification.excute();
		crossVerification.checkVerifications();
		  
	}

}
