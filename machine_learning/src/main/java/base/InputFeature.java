package base;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class InputFeature {
	Logger logger = Logger.getLogger(InputFeature.class);
		
	List<InstanceD> instances = new ArrayList<InstanceD>();
	
	public InputFeature(){}
	
	public InputFeature(List<InstanceD> instances)
	{
		this.instances = instances;
	}
	
	public void add(InstanceD instance)
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

	public List<InstanceD> getInstances() {
		return instances;
	}

	public void setInstances(List<InstanceD> instances) {
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
	
	public InstanceD getInstanceD(int index)
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