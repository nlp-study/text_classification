package classifier.maxent;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import validation.Maxent;
import base.InstanceSetI;

/**
 * @author xiaohe
 * 创建于：2015年2月9日
 * 代码中出现的问题：
 * 1. modelE除考虑的问题不全面，应该是所有类别的概率，这里单独以为是指定的类别
 */
public class MaxEntropyTrainerTest {
	private static MaxEntropyTrainer maxEntropy = new MaxEntropyTrainer();
    
	@Before
	public void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		
		String path = "data/corpus/maxent.data";
	    Maxent maxent = new Maxent();
	    maxent.readData(path);	    
	    InstanceSetI inputFeature = maxent.getInputFeature();
	    
	
		maxEntropy.init(inputFeature);
	}

	@Test
	public void test() throws Exception {
		maxEntropy.train();
		String path = "data/result/model.m";
		maxEntropy.saveModel(path);
		
		
		MaxEntropyInfer maxEntropyInfer = new MaxEntropyInfer();
		maxEntropyInfer.init(path);
		int[] input = {0,1,1};
		int classid = maxEntropyInfer.infer(input);
		assertEquals(0,classid);
	}

}
