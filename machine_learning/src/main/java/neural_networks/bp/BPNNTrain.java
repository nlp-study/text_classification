package neural_networks.bp;

import java.io.IOException;

import base.InstanceSetD;
import regress.AbstractRegressTrainer;
import validation.Iris;

/**
 * @author Administrator
 * 需要注意的问题：
 * 1. 因为输入和隐藏层的向量都进行了扩展，这块很容易出错。
 */
public class BPNNTrain extends AbstractRegressTrainer {
	//	误差率	
	private final double ERROR_RATE = 0.1;
	//迭代次数
	private final int ITER_NUM = 1000;
	
	int inputNumb;
	int hiddenNumb;
	int outputNumber;
	
	double[] inputLayer;
	NeuralNetworkNode[] hiddenLayer;
	NeuralNetworkNode[] outputLayer;
	InstanceSetD inputFeature;
	double[] hiddenResult;
	double[] outputResult;
	
	int input_size = 0;
	double eta_1 = 0.1;
	double eta_2 = 0.1;
	
	/**
	 * @param inputNumb  输入层数量
	 * @param hiddenNumb  隐藏层数量
	 * @param outputNumber  输出层数量
	 */
	public BPNNTrain(int inputNumb, int hiddenNumb, int outputNumber) 
	{ 
		this.inputNumb = inputNumb;
		this.hiddenNumb = hiddenNumb;
		this.outputNumber = outputNumber;		
		inputLayer = new double[inputNumb+1];
		hiddenResult = new double[hiddenNumb+1];
		outputResult = new double[outputNumber];
		hiddenLayer = new NeuralNetworkNode[hiddenNumb];
		for(int k=0;k<hiddenNumb;++k)
		{
			hiddenLayer[k] = new NeuralNetworkNode(inputNumb+1);
		}
		outputLayer = new NeuralNetworkNode[outputNumber];
		for(int j=0;j<outputNumber;++j)
		{
			outputLayer[j] = new NeuralNetworkNode(hiddenNumb+1);
		}
	}

	@Override
	public void init(InstanceSetD inputFeature) {
		this.inputFeature = inputFeature;
		input_size = inputFeature.getSize();
	}

	@Override
	public void train() {
		double errorRate = 0;
		while(true)
		{			
			double[] hiddenGradient = new double[this.inputNumb];
			double[] outputGradient = new double[this.hiddenNumb];
			for(int i = 0;i<input_size;++i)
			{
				double[] raw_inputlayer = this.inputFeature.getInstanceD(i).getVector();
				inputLayer = vectorLengthen(raw_inputlayer);
				calculateNetwork(inputLayer);
				calculateW();
				calcualteV();				
			}
			
			errorRate = calculateGlobalError();
			System.out.println("global error: "+errorRate);
			
			if(errorRate < ERROR_RATE)
			{
				break;
			}
		}
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
			for(int k=0;k<this.hiddenNumb;++k)
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
		double Wjk = eta_2 * (getSingleError(j))*this.outputResult[j]*(1-this.outputResult[j])*this.hiddenResult[k];
		return Wjk;
	}
	
	private void calcualteV(){
		for(int k=0;k<this.hiddenNumb;++k)
		{
			for(int i=0;i<this.inputNumb;++i)
			{
				double deltaVki = calcualteDeltaVki(k, i);
				double Vki = hiddenLayer[k].getFeautureVecByIndex(i) + deltaVki;
				hiddenLayer[k].setFeautureVecByIndex(k, Vki);				
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
		double deltaVki = hiddenImpactFactor * this.hiddenResult[k]*(1-this.hiddenResult[k])*this.inputLayer[i];
		return deltaVki;
	}
	
	private void calculateNetwork(double[] tempInputLayer)
	{
		getHiddenResult(tempInputLayer);
		getOutputResult();		
	}
	
	private void getHiddenResult(double[] tempInputLayer)
	{
		for(int i=0;i<hiddenNumb;++i)    //note that the hiddenResult length is hideng_length+1
		{
			hiddenResult[i+1] = hiddenLayer[i].transferFunction(tempInputLayer);
		}
	}
	
	private void getOutputResult()
	{
		for(int i=0;i<outputNumber;++i)
		{
			outputResult[i] = outputLayer[i].transferFunction(hiddenResult);
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
		return this.inputFeature.getClassID(j) - this.outputResult[j];
	}
	private double getHiddenGradient(double[] input)
	{
		return 0.0;
	}
	
	private double getOutputGradient(double[] input)
	{
		return 0.0;
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
		int hiddenNumb = 7;
		int outputNumber = 3;
		BPNNTrain bpNNTrain = new BPNNTrain(inputNumb,hiddenNumb,outputNumber);
		bpNNTrain.init(inputFeature);
		bpNNTrain.train();
	}

}
