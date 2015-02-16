package validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import util.FileRead;
import util.Pair;
import base.InstanceD;
import base.InstanceSetD;
import classifier.bayes.BayesTrainer;

public class HeartScale {
	Logger logger = Logger.getLogger(HeartScale.class);

	InstanceSetD inputFeature = new InstanceSetD();
		
	public InstanceSetD getInputFeature() {
		return inputFeature;
	}

	public void setInputFeature(InstanceSetD inputFeature) {
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
			int typeid = 0;
			String[] temp = str.split(" ");
			
			if(temp[0].equals("+1"))
			{
				typeid = 1;
			}
			else
				typeid = -1;
			
			double[] vector = new double[13];
			for(int i=1;i<temp.length;i++)
			{
				String[] unit = temp[i].split(":");
				int id = Integer.parseInt(unit[0]);
				double value = Double.parseDouble(unit[1]);
				vector[id-1] = value;
			}
			
			
			InstanceD instance = new InstanceD(typeid,13,vector);
			inputFeature.add(instance);
		}
		
	}
	
	public static void main(String[] args) throws IOException
	{
		String path = "data/corpus/heart_scale.txt";
		HeartScale heartScale = new HeartScale();
		heartScale.readData(path);
		InstanceSetD inputFeature = heartScale.getInputFeature();
		System.out.println(inputFeature);
	}

}
