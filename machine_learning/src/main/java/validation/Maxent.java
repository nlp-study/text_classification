package validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import util.FileRead;
import base.InstanceD;
import base.InstanceI;
import base.InstanceSetD;
import base.InstanceSetI;
import classifier.bayes.BayesTrainer;

public class Maxent {
	Logger logger = Logger.getLogger(Maxent.class);

	InstanceSetI inputFeature = new InstanceSetI();
		
	public InstanceSetI getInputFeature() {
		return inputFeature;
	}

	public void setInputFeature(InstanceSetI inputFeature) {
		this.inputFeature = inputFeature;
	}

	public void init(String path) throws IOException
	{
	}
	
	public void readData(String path) throws IOException
	{
		List<String> data = FileRead.readLine(path);
		List<InstanceD> instances = new ArrayList<InstanceD>();
		
		for(String str:data)
		{
			String[] temp = str.split(" ");
			int[] vector = new int[3];
			int classid = Integer.parseInt(temp[0]);
			for(int i=1;i<4;i++)
			{
				vector[i-1] = Integer.parseInt(temp[i]);
			}
			
			InstanceI instance = new InstanceI(classid,3,vector);
			inputFeature.add(instance);
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		String path = "data/corpus/maxent.data";
		Maxent maxent = new Maxent();
		maxent.readData(path);
		InstanceSetI inputFeature = maxent.getInputFeature();
		System.out.println(inputFeature);
	}
}
