package tagging.hmm;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import tagging.TaggingInput;

public class HMMTrainerTest {
    public static HMMTrainer hmmTrainer = new HMMTrainer();
    
	@Before
	public void setUp() throws Exception {
		
		
		
		
	}

	@Test
	public void trainNoTaggingtest() throws IOException {
		PropertyConfigurator.configure("log4j.properties");
		
		String path = "data/corpus/hmm/hmm4_01.in";
		DataFactory dataFactory = new DataFactory();
		dataFactory.readFile(path);		
		
		HMMTrainer hmmTrainer = new HMMTrainer();
		double[][] A = dataFactory.getA();
		double[][] B = dataFactory.getB();
		double[] pi = dataFactory.getPi();
		int stateSize = 4;
		int inputSize = dataFactory.getInputSize();

		List<TaggingInput> instances = new ArrayList<TaggingInput>();
		for(int i=0;i<inputSize;++i)
		{
			TaggingInput taggingInput = new TaggingInput(dataFactory.getTrainSequence()[i], -1);
			instances.add(taggingInput);
		}
	
		hmmTrainer.temporaryInit(A, B, pi, stateSize, inputSize, instances);
		hmmTrainer.forward();
		hmmTrainer.backward();
		
	}
	
	
	

}
