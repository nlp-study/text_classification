package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import base.InputFeatureD;
import base.InstanceD;
import base.InstanceI;

public class ClassDistribution {
	private int K;
	private int[] numb;
	private List<InstanceD> instances;
	private int length;
		
	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public int[] getNumb() {
		return numb;
	}

	public void setNumb(int[] numb) {
		this.numb = numb;
	}

	public ClassDistribution(InputFeatureD inputFeature)
	{
		this.instances = inputFeature.getInstances();
		length = inputFeature.getLength();
	}
	
	public void excute()
	{
		calculateClassNumb();
		calculateClassDistribution();
	}
	
	
	public void calculateClassNumb()
	{
        Set<Integer> classids = new HashSet<Integer>();
		
		for(InstanceD instance:instances)
		{
			classids.add(instance.getType());
		}
		
		K = classids.size();
	}
	
	
	public void calculateClassDistribution()
	{
		numb = new int[K];
		Arrays.fill(numb, 0);
		
		for(InstanceD instance:instances)
		{
			numb[instance.getType()]++;
		}
	}

}
