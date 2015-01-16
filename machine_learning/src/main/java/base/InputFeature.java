package base;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class InputFeature {
	Logger logger = Logger.getLogger(InputFeature.class);
	
	//输入的实例的数量
	private int size;
	
	//向量的维度
	private int length;
	
	int classNumb;
	
	List<InstanceD> instances = new ArrayList<InstanceD>();
	
	public void add(InstanceD instance)
	{
		instances.add(instance);
	}

	public int getSize() {
		return instances.size();
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<InstanceD> getInstances() {
		return instances;
	}

	public void setInstances(List<InstanceD> instances) {
		this.instances = instances;
	}

	public int getClassNumb() {
		return classNumb;
	}

	public void setClassNumb(int classNumb) {
		this.classNumb = classNumb;
	}
	
	public String toString()
	{
		return instances.toString();
	}
	
	public void clear()
	{
		size = 0;
		length = 0;
		classNumb = 0;
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
	

}
