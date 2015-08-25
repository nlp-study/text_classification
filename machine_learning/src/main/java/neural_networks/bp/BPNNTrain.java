package neural_networks.bp;

import java.io.IOException;
import java.util.ArrayList;

import neural_networks.BaseThreeLayerNN;
import base.InstanceSetD;
import regress.AbstractRegressTrainer;
import validation.Iris;

public class BPNNTrain extends AbstractRegressTrainer {
	//	误差率	
	private final double ERROR_RATE = 0.1;
	//迭代次数
	private final int ITER_NUM = 100;
	
	BaseThreeLayerNN threeLayerNN;	
	
	InstanceSetD inputFeature;  //输入的数据	
	int input_size; //输入数据的总量
	
	double[] token_tag;	
	double eta_1 = 0.1;
	double eta_2 = 0.1;
	double errorRate = 0;
	
	int inputNumb;
	int hiddenNumb;
	int outputNumber;
	
	ArrayList<double[]> deltaV;
	ArrayList<double[]> deltaW;
	
	public BPNNTrain(int inputNumb, int hiddenNumb, int outputNumber)
	{
		this.inputNumb = inputNumb;
		this.hiddenNumb = hiddenNumb;
		this.outputNumber = outputNumber;
		threeLayerNN = new BaseThreeLayerNN(inputNumb,hiddenNumb,outputNumber);
		token_tag = new double[outputNumber];
		deltaV = new ArrayList<double[]>();
		deltaW = new ArrayList<double[]>();
		for(int i=0;i<hiddenNumb;++i)
		{
			double[] tempArray = new double[inputNumb+1];
			deltaV.add(tempArray);
		}		
		for(int i=0;i<outputNumber;++i)
		{
			double[] tempArray = new double[hiddenNumb+1];
			deltaW.add(tempArray);
		}
	}

	@Override
	public void init(InstanceSetD inputFeature) {
		// TODO Auto-generated method stub
		
		this.inputFeature = inputFeature;
		input_size = inputFeature.getSize();
		if(this.inputNumb != inputFeature.getLength())
		{
			System.out.println("Error:input train data length is not equal inputNumber");
			System.exit(0);
		}
	}

	@Override
	public void train() {
		// TODO Auto-generated method stub
		
		
		int iter_numb = 0;
		while(true)
		{			
			errorRate = 0;
			for(int i = 0;i<input_size;++i)
			{
				
				double[] inputVector = inputFeature.getInstanceD(i).getVector();
				setTokenValue(inputFeature.getInstanceD(i).getType());
				threeLayerNN.infer(inputVector);
				calculateDelta();	
				calculateGlobalError();
			}
					
			System.out.println("global error: "+errorRate);
			
			if(errorRate < ERROR_RATE)
			{
				break;
			}
			if(++iter_numb > ITER_NUM)
			{
				break;
			}
			
			iterateWightVector();  //迭代权重向量
		}

	}
	
	private void calculateDelta()
	{
		calculateW();
		calcualteV();
	}
	
	private void calculateW()
	{
		for(int j=0;j<this.outputNumber;++j)
		{
			for(int k=0;k<this.hiddenNumb+1;++k)
			{
				double deltaWjk = calculateDeltaWjk(j,k);
				double[] tempArray = deltaW.get(j);
				double Wjk = deltaW.get(j)[k] + deltaWjk;
				tempArray[k] = Wjk;
				deltaW.set(j, tempArray);
			}
		}
	}
	
	//calculate output layer gradient Wjk
	private double calculateDeltaWjk(int j,int k)
	{
		
		double Wjk = eta_2 * (getSingleError(j))*threeLayerNN.getOutputVector()[j]*(1-threeLayerNN.getOutputVector()[j])*threeLayerNN.getHiddenVector()[k];
		return Wjk;
	}
	
	private void calcualteV(){
		for(int k=0;k<this.hiddenNumb;++k)
		{
			for(int i=0;i<this.inputNumb+1;++i)
			{
//				System.out.println("input k: "+k+" i: "+i+" length of deltaV: "+deltaV.size());
				double deltaVki = calcualteDeltaVki(k, i);
				
				double[] tempArray = deltaV.get(k);
				double Vki = deltaV.get(k)[i] + deltaVki;
				tempArray[i] = Vki;
				deltaV.set(k, tempArray);
			}
		}
	}
	
	private double calcualteDeltaVki(int k, int i)
	{
		double hiddenImpactFactor = 0.0;
		for(int j=0;j<this.outputNumber;++j)
		{
			hiddenImpactFactor += getSingleError(j)*threeLayerNN.getOutputVector()[j]*(1-threeLayerNN.getOutputVector()[j])*threeLayerNN.getOutputLayerWightVector(j)[k];
		}
		double deltaVki = eta_1 * hiddenImpactFactor * threeLayerNN.getHiddenLayerValue()[k]*(1-threeLayerNN.getHiddenLayerValue()[k])*threeLayerNN.getInputVector()[i];
		return deltaVki;
	}
	
	private double getSingleError(int j)
	{
		return token_tag[j] - threeLayerNN.getOutputVector()[j];
	}
	
	private void calculateGlobalError()
	{
		double temp = 0;
		for(int j=0;j<this.outputNumber;++j)
		{
			temp += getSingleError(j) * getSingleError(j);
		}
		temp = temp * 0.5;
		errorRate += temp;		
	}
	
	private void iterateWightVector()
	{
		iterateW();
		iterateV();
	}
	
	private void iterateW()
	{
		for(int i=0;i<this.outputNumber;++i)
		{
			double[] tempWightVector = this.threeLayerNN.getOutputLayerWightVector(i);
			double[] tempDeltaVector = this.deltaW.get(i);
			double[] resultVector = addDoubleArray(tempWightVector,tempDeltaVector);
			this.threeLayerNN.setOutputLayerWightVector(i, resultVector);
		}
	}
	
	private void iterateV()
	{
		for(int i=0;i<this.hiddenNumb;++i)
		{
			double[] tempWightVector = this.threeLayerNN.getHiddenLayerWightVector(i);
			double[] tempDeltaVector = this.deltaV.get(i);
			double[] resultVector = addDoubleArray(tempWightVector,tempDeltaVector);
			this.threeLayerNN.setHiddenLayerWightVector(i, resultVector);
		}
	}
	
	private double[] addDoubleArray(double[] array1,double[] array2)
	{
		if(array1.length != array2.length)
		{
			System.out.println("Error:input array size is not equal!!!");
		}
		double[] temp = new double[array1.length];
		for(int i=0;i<temp.length;++i)
		{
			temp[i] = array1[i] + array2[i];
		}
		return temp;
	}
	
	private void setTokenValue(int token)
	{
		for(int i=0;i<outputNumber;++i)
		{
			token_tag[i] = 0;
		}
		token_tag[token] = 1;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

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
