package classifier.util;

import org.apache.log4j.Logger;
import base.InstanceSetD;
import base.InstanceSetI;
import base.InstanceD;
import base.InstanceI;

/**
 * @author xiaohe 2015年1月7日 将输入特征由double类型转化成int类型
 */
public class FeatureDoubleToInt {
	Logger logger = Logger.getLogger(FeatureDoubleToInt.class);
	
	InstanceSetD inputfeature = new InstanceSetD();
	
	InstanceSetI outputfeature = new InstanceSetI();
	
	public FeatureDoubleToInt(InstanceSetD inputfeature)
	{
		this.inputfeature = inputfeature;
	}
	
	public void init(InstanceSetD inputfeature)
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

	public InstanceSetI getOutputfeature() {
		return outputfeature;
	}

}
