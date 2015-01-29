package base;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author xiaohe
 * 创建于：2015年1月29日
 * 整型输入特征，用于分类
 */
public class InputFeatureI {
	Logger logger = Logger.getLogger(InputFeatureD.class);
	
	List<InstanceI> instances = new ArrayList<InstanceI>();
	
	public InputFeatureI(){}
	
	public InputFeatureI(List<InstanceI> instances)
	{
		this.instances = instances;
	}
	
	public void add(InstanceI instance)
	{
		instances.add(instance);
	}

	public int getSize() {
		return instances.size();
	}

	public int getLength() {
		if(instances.size() > 0)
		{
			return instances.get(0).getLength();
		}
		return 0;
	}	

	public List<InstanceI> getInstances() {
		return instances;
	}

	public void setInstances(List<InstanceI> instances) {
		this.instances = instances;
	}

	
	
	public String toString()
	{
		return instances.toString();
	}
	
	public void clear()
	{		
		instances.clear();
	}
	
	public InstanceI getInstanceD(int index)
	{
		if(index>instances.size()-1)
		{
			logger.error("out of size!");
		}
		
		return instances.get(index);
	}
	
	public int getClassID(int index)
	{
		if(index>instances.size()-1)
		{
			logger.error("out of size!");
		}
		
		return instances.get(index).getType();
	}

}
