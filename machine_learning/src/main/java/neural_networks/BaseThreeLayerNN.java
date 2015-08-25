package neural_networks;

import java.util.Arrays;

import base.InstanceSetD;

public class BaseThreeLayerNN {
	int inputNumb;
	int hiddenNumb;
	int outputNumber;
	
	double[] inputLayer;
	NeuralNetworkNode[] hiddenLayer;
	NeuralNetworkNode[] outputLayer;

	double[] inputVector;
	double[] hiddenVector;
	double[] outputVector;
	
	double[] hiddenLayerValue;

	
		
	public NeuralNetworkNode[] getHiddenLayer() {
		return hiddenLayer;
	}
	

	public double[] getInputLayer() {
		return inputLayer;
	}


	public double[] getInputVector() {
		return inputVector;
	}

	public double[] getHiddenVector() {
		return hiddenVector;
	}

	public double[] getOutputVector() {
		return outputVector;
	}

	public double[] getHiddenLayerValue() {
		return hiddenLayerValue;
	}
	
	public double[] getOutputLayerWightVector(int number)
	{
		return outputLayer[number].getFeatureVec();
	}
	
	public void setOutputLayerWightVector(int number,double[] tempVector)
	{
		outputLayer[number].setFeatureVec(tempVector);
	}
	
	public double[] getHiddenLayerWightVector(int number)
	{
		return hiddenLayer[number].getFeatureVec();
	}
	
	public void setHiddenLayerWightVector(int number,double[] tempVector)
	{
		hiddenLayer[number].setFeatureVec(tempVector);
	}


	public BaseThreeLayerNN(int inputNumb, int hiddenNumb, int outputNumber) 
	{ 
		this.inputNumb = inputNumb;
		this.hiddenNumb = hiddenNumb;
		this.outputNumber = outputNumber;		
		inputLayer = new double[inputNumb];		
		inputVector = new double[inputNumb+1];
		hiddenVector = new double[hiddenNumb+1];
		hiddenLayer = new NeuralNetworkNode[hiddenNumb];
		hiddenLayerValue = new double[hiddenNumb];
		outputVector = new double[outputNumber];
		

		
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


	public void infer(double[] inputVector)
	{
		if(inputVector.length != inputNumb)
		{
			System.out.println("Error:input vector size is no validate!!");
			System.exit(0);
		}
		
		inputLayer = inputVector;
		inputVector = extendVector(inputVector);
		calcualteHiddenLayer();
		calculateOutputLayer();		
//		System.out.println(Arrays.toString(inputVector));
//		System.out.println(Arrays.toString(hiddenVector));
//		System.out.println(Arrays.toString(outputVector));	
	}
	
	private void calcualteHiddenLayer()
	{
		hiddenVector[0] = 1;
		for(int i=0;i<hiddenNumb;++i)
		{
			hiddenLayer[i].transferFunction(inputVector);
			hiddenVector[i+1] = hiddenLayer[i].getResult();
			hiddenLayerValue[i] = hiddenLayer[i].getResult();
		}		
	}
	
	private void calculateOutputLayer()
	{
		for(int i=0;i<outputNumber;++i)
		{
			outputLayer[i].transferFunction(hiddenVector);
			outputVector[i] =  outputLayer[i].getResult();
		}
	}
	
	private double[] extendVector(double[] inputVector)
	{
		double[] tempArray = new double[inputVector.length+1];
		tempArray[0] = 1;
		for(int i=0;i<inputVector.length;++i)
		{
			tempArray[i+1] = inputVector[i];
		}
		return tempArray;
	}
	
	public static void main(String[] args)
	{
		double[] inputVector = {1,1,1};
		BaseThreeLayerNN threeLaryNN = new BaseThreeLayerNN(3,3,3); 
		threeLaryNN.infer(inputVector);
	}
	

}
