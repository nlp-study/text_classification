package classifier.iris;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import classifier.bayes.BayesTrainer;
import util.FileRead;
import vsm.VSM;
import vsm.VSMBuilder;

public class Iris {
	Logger logger = Logger.getLogger(BayesTrainer.class);

	VSMBuilder vsmBuilder = new VSMBuilder();
	
	public VSMBuilder getVsmBuilder() {
		return vsmBuilder;
	}

	public void setVsmBuilder(VSMBuilder vsmBuilder) {
		this.vsmBuilder = vsmBuilder;
	}

	public void init(String path) throws IOException
	{
	}
	
	public void readData(String path) throws IOException
	{
		List<String> data = FileRead.readLine(path);
		List<VSM> vsms = new ArrayList<VSM>();
		
		for(String str:data)
		{
			String[] temp = str.split(",");
			VSM.size = 4;
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
			VSM vsm = new VSM(classid,vector);
			vsms.add(vsm);
		}
		
		vsmBuilder.setVsms(vsms);
	}
	
	public static void main(String[] args) throws IOException
	{
		String path = "data/corpus/iris/iris.data";
		Iris iris = new Iris();
		iris.readData(path);
		VSMBuilder vsmBuilder = iris.getVsmBuilder();
		System.out.println(vsmBuilder);
	}
}
