package classifier.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import manager.BinaryClassValidation;

import org.apache.log4j.Logger;

import base.InputFeatureD;
import base.InputFeatureI;
import base.InstanceD;
import base.InstanceI;

/**
 * @author xiaohe 2015年1月7日 将输入特征由double类型转化成int类型
 */
public class FeatureDoubleToInt {
	Logger logger = Logger.getLogger(FeatureDoubleToInt.class);
	
	InputFeatureD inputfeature = new InputFeatureD();
	
	InputFeatureI outputfeature = new InputFeatureI();
	
	public FeatureDoubleToInt(InputFeatureD inputfeature)
	{
		this.inputfeature = inputfeature;
	}
	
	public void init(InputFeatureD inputfeature)
	{
		outputfeature.clear();
		this.inputfeature = inputfeature;
	}
	
	/**
	 * @comment:暂时只是用四舍五入的方法来处理
	 * @return:void
	 */
	public void trans()
	{
		for(InstanceD instance : inputfeature.getInstances())
		{
			int type = instance.getType();
			double[] vector = instance.getVector();
			int length = instance.getLength();
			int[] vectorI = new int[length];
			
			for(int i=0;i<length;++i)
			{
				int value = (int)Math.rint(vector[i]);
				vectorI[i] = value;
			}
			InstanceI instanceI = new InstanceI(type,length,vectorI);
			outputfeature.add(instanceI);
		}
	}

	public InputFeatureI getOutputfeature() {
		return outputfeature;
	}

}
