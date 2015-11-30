package neural_networks;

import java.util.Arrays;

import base.InstanceSetD;

public class BaseThreeLayerNN {
	int inputNumb;
	int hiddenNumb;
	int outputNumb;

	double[] inputLayer;
	NeuronOriginal[] hiddenLayer;
	NeuronOriginal[] outputLayer;

	double[] inputVector;
	double[] hiddenVector;
	double[] outputVector;

	double[] hiddenLayerValue;

	public NeuronOriginal[] getHiddenLayer() {
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

	public double[] getOutputLayerWightVector(int number) {
		return outputLayer[number].getFeatureVec();
	}

	public void setOutputLayerWightVector(int number, double[] tempVector) {
		outputLayer[number].setFeatureVec(tempVector);
	}

	public double[] getHiddenLayerWightVector(int number) {
		return hiddenLayer[number].getFeatureVec();
	}

	public void setHiddenLayerWightVector(int number, double[] tempVector) {
		hiddenLayer[number].setFeatureVec(tempVector);
	}

	public BaseThreeLayerNN(int inputNumb, int hiddenNumb, int outputNumber) {
		this.inputNumb = inputNumb;
		this.hiddenNumb = hiddenNumb;
		this.outputNumb = outputNumber;
		inputLayer = new double[inputNumb];
		inputVector = new double[inputNumb + 1];
		hiddenVector = new double[hiddenNumb + 1];
		hiddenLayerValue = new double[hiddenNumb];
		outputVector = new double[outputNumber];

		hiddenLayer = new NeuronOriginal[hiddenNumb];
		for (int k = 0; k < hiddenNumb; ++k) {
			hiddenLayer[k] = new NeuronOriginal(inputNumb + 1);
		}

		outputLayer = new NeuronOriginal[outputNumber];
		for (int j = 0; j < outputNumber; ++j) {
			outputLayer[j] = new NeuronOriginal(hiddenNumb + 1);
		}
	}

	public BaseThreeLayerNN(int inputNumb, NeuronOriginal[] hiddenLayer,
			NeuronOriginal[] outputLayer) {
		this.inputNumb = inputNumb;
		this.hiddenNumb = hiddenLayer.length;
		this.outputNumb = outputLayer.length;
		inputLayer = new double[inputNumb];
		inputVector = new double[inputNumb + 1];
		hiddenVector = new double[hiddenNumb + 1];
		hiddenLayerValue = new double[hiddenNumb];
		outputVector = new double[outputNumb];

		this.hiddenLayer = hiddenLayer;

		this.outputLayer = outputLayer;
		System.out.println("BaseThreeLayerNN construction");
	}

	public void infer(double[] inputVector) {
		if (inputVector.length != inputNumb) {
			System.out.println("Error:input vector size is no validate!!");
			System.out.println(Arrays.toString(inputVector));
			System.exit(0);
		}

		inputLayer = inputVector;
		this.inputVector = extendVector(inputVector);
		calcualteHiddenLayer();
		calculateOutputLayer();
		// System.out.println("input vector:"+Arrays.toString(inputVector));
		// System.out.println("hidden vector:"+Arrays.toString(hiddenVector));
		// System.out.println("output vector:"+Arrays.toString(outputVector));
	}

	private void calcualteHiddenLayer() {
		hiddenVector[0] = 1;
		for (int i = 0; i < hiddenNumb; ++i) {
//			System.out.println("calcualteHiddenLayer inputVector:"+Arrays.toString(inputVector));
			hiddenLayer[i].feedForward(inputVector);
			hiddenVector[i + 1] = hiddenLayer[i].getResult();
			hiddenLayerValue[i] = hiddenLayer[i].getResult();
		}
	}

	private void calculateOutputLayer() {
		for (int i = 0; i < outputNumb; ++i) {
			outputLayer[i].feedForward(hiddenVector);
			outputVector[i] = outputLayer[i].getResult();
		}
	}

	private double[] extendVector(double[] inputVector) {
		double[] tempArray = new double[inputVector.length + 1];
		tempArray[0] = 1;
		for (int i = 0; i < inputVector.length; ++i) {
			tempArray[i + 1] = inputVector[i];
		}
		return tempArray;
	}

	public void showNeuronLayerWeight() {
		System.out.println("hidden layer:");
		for (int i = 0; i < hiddenNumb; ++i) {
			System.out.println(hiddenLayer[i]);
		}
		System.out.println("output layer:");
		for (int i = 0; i < outputNumb; ++i) {
			System.out.println(outputLayer[i]);
		}
	}

	public void showNeuronLayerOutput() {
		System.out.println("hidden layer output:");
		System.out.println(Arrays.toString(hiddenVector));
		System.out.println("output layer output:");
		System.out.println(Arrays.toString(outputVector));
	}

	public static void main(String[] args) {
		double[] inputVector = { 1, 1, 1 };
		BaseThreeLayerNN threeLaryNN = new BaseThreeLayerNN(3, 3, 3);
		threeLaryNN.infer(inputVector);
	}

}
