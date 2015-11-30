package neural_networks.bp;

import java.io.IOException;

import neural_networks.NeuronOriginal;
import base.InstanceSetD;
import regress.AbstractRegressTrainer;
import validation.Iris;

/**
 * @author Administrator
 * 需要注意的问题：
 * 1. 因为输入和隐藏层的向量都进行了扩展，这块很容易出错。
 */
public class BPNNTrainBackup extends AbstractRegressTrainer {
	//	误差率	
	private final double ERROR_RATE = 0.1;
	//迭代次数
	private final int ITER_NUM = 1000;
	
	int inputNumb;
	int hiddenNumb;
	int outputNumber;
	
	InstanceSetD inputFeature;  //输入的数据
	
	double[] inputLayer;
	NeuronOriginal[] hiddenLayer;
	NeuronOriginal[] outputLayer;
	
	double[] hiddenResult;
	double[] outputResult;
	double[] inputVector;
	double[] hiddenVector;
	double[] token_tag;
	
	int input_size = 0;
	double eta_1 = 0.1;
	double eta_2 = 0.1;
	
	/**
	 * @param inputNumb  输入层数量
	 * @param hiddenNumb  隐藏层数量
	 * @param outputNumber  输出层数量
	 */
	public BPNNTrainBackup(int inputNumb, int hiddenNumb, int outputNumber) 
	{ 
		this.inputNumb = inputNumb;
		this.hiddenNumb = hiddenNumb;
		this.outputNumber = outputNumber;		
		inputLayer = new double[inputNumb];
		hiddenResult = new double[hiddenNumb];
		outputResult = new double[outputNumber];
		inputVector = new double[inputNumb+1];
		hiddenVector = new double[hiddenNumb+1];
		hiddenLayer = new NeuronOriginal[hiddenNumb];
		token_tag = new double[outputNumber];
		
		for(int k=0;k<hiddenNumb;++k)
		{
			hiddenLayer[k] = new NeuronOriginal(inputNumb+1);
		}
		outputLayer = new NeuronOriginal[outputNumber];
		for(int j=0;j<outputNumber;++j)
		{
			outputLayer[j] = new NeuronOriginal(hiddenNumb+1);
		}
	}

	@Override
	public void init(InstanceSetD inputFeature) {
		this.inputFeature = inputFeature;
		input_size = inputFeature.getSize();
		inputVector[0] = 1;
		for(int i=0;i<inputNumb;++i)
		{
			inputVector[i+1] = inputLayer[i];
		}
		hiddenVector[0] = 1;
		for(int i=0;i<hiddenNumb;++i)
		{
			hiddenVector[i+1] = hiddenResult[i];
		}
		
	}

	@Override
	public void train() {
		double errorRate = 0;
		int iter_numb = 0;
		while(true)
		{			
			for(int i = 0;i<input_size;++i)
			{
				inputLayer = this.inputFeature.getInstanceD(i).getVector();
				inputVector = vectorLengthen(inputLayer);
				setTokenValue(this.inputFeature.getClassID(i));
				calculateNetwork();
				calculateW();
				calcualteV();				
			}
			
			errorRate = calculateGlobalError();
			System.out.println("global error: "+errorRate);
			
//			if(errorRate < ERROR_RATE)
//			{
//				break;
//			}
			if(++iter_numb > ITER_NUM)
			{
				break;
			}
		}
	}
	
	private void setTokenValue(int token)
	{
		token_tag[token] = 1;
	}
	
	private double[] vectorLengthen(double[] input)
	{
		double[] temp_vector = new double[input.length+1];
		temp_vector[0] = 1;
		for(int i=1;i<temp_vector.length;++i)
		{
			temp_vector[i] = input[i-1];
		}	
		return temp_vector;
	}
	
	private void calculateW()
	{
		for(int j=0;j<this.outputNumber;++j)
		{
			for(int k=0;k<this.hiddenNumb+1;++k)
			{
				double deltaWjk = calculateDeltaWjk(j,k);
				double Wjk = outputLayer[j].getFeautureVecByIndex(j) + deltaWjk;
				outputLayer[j].setFeautureVecByIndex(k, Wjk);
			}
		}
	}
	
	//calculate output layer gradient Wjk
	private double calculateDeltaWjk(int j,int k)
	{
		double Wjk = eta_2 * (getSingleError(j))*this.outputResult[j]*(1-this.outputResult[j])*this.hiddenVector[k];
		return Wjk;
	}
	
	private void calcualteV(){
		for(int k=0;k<this.hiddenNumb;++k)
		{
			for(int i=0;i<this.inputNumb+1;++i)
			{
				double deltaVki = calcualteDeltaVki(k, i);
				double Vki = hiddenLayer[k].getFeautureVecByIndex(i) + deltaVki;
				hiddenLayer[k].setFeautureVecByIndex(i, Vki);				
			}
		}
	}
	
	private double calcualteDeltaVki(int k, int i)
	{
		double hiddenImpactFactor = 0.0;
		for(int j=0;j<this.outputNumber;++j)
		{
			hiddenImpactFactor += getSingleError(j)*this.outputResult[j]*(1-this.outputResult[j])*this.outputLayer[j].getFeautureVecByIndex(k);
		}
		double deltaVki = hiddenImpactFactor * this.hiddenResult[k]*(1-this.hiddenResult[k])*this.inputVector[i];
		return deltaVki;
	}
	
	private void calculateNetwork()
	{
		getHiddenResult();
		getOutputResult();		
	}
	
	private void getHiddenResult()
	{
		for(int i=0;i<hiddenNumb;++i)    //note that the hiddenResult length is hideng_length+1
		{
			hiddenResult[i] = hiddenLayer[i].feedForward(inputVector);
			hiddenVector[i+1] = hiddenResult[i];
		}
		
	}
	
	private void getOutputResult()
	{
		for(int i=0;i<outputNumber;++i)
		{
			outputResult[i] = outputLayer[i].feedForward(hiddenVector);
		}
	}
	
	private double calculateGlobalError()
	{
		double globalError = 0;
		for(int j=0;j<this.outputNumber;++j)
		{
			globalError += getSingleError(j)*getSingleError(j);
		}
		globalError = globalError /2;
		return globalError;
	}
	
	private double getSingleError(int j)
	{
		return token_tag[j] - this.outputResult[j];
	}


	@Override
	public void clear() {

	}
	
	public static void main(String[] args) throws IOException
	{
		String path = "data/corpus/iris.data";
		Iris iris = new Iris();
		iris.readData(path);
		InstanceSetD inputFeature = iris.getInputFeature();
		
		int inputNumb = 4;
		int hiddenNumb = 12;
		int outputNumber = 3;
		BPNNTrainBackup bpNNTrain = new BPNNTrainBackup(inputNumb,hiddenNumb,outputNumber);
		bpNNTrain.init(inputFeature);
		bpNNTrain.train();
	}

}
