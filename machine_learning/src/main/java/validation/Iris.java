package validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import base.InstanceSetD;
import base.InstanceD;
import classifier.bayes.BayesTrainer;
import util.FileRead;

public class Iris {
	Logger logger = Logger.getLogger(BayesTrainer.class);

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
			String[] temp = str.split(",");
			double[] vector = new double[4];
			int classid = -1;
			for(int i=0;i<4;i++)
			{
				vector[i] = Double.parseDouble(temp[i]);
			}
			
			if(temp[4].equals("Iris-setosa"))
			{
				classid = 0;
			}
			else if(temp[4].equals("Iris-versicolor"))
			{
				classid = 1;
			}
			else
			{
				classid = 2;
			}
			InstanceD instance = new InstanceD(classid,4,vector);
			inputFeature.add(instance);
		}
		
		
	}
	
	public static void main(String[] args) throws IOException
	{
		String path = "data/corpus/iris.data";
		Iris iris = new Iris();
		iris.readData(path);
		InstanceSetD inputFeature = iris.getInputFeature();
		System.out.println(inputFeature);
	}
}
