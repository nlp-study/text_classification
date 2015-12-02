package neural_networks.bp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import neural_networks.BaseThreeLayerNN;
import neural_networks.NeuronOriginal;
import base.InstanceD;
import base.InstanceSetD;
import regress.AbstractRegressTrainer;
import validation.Iris;

public class BPNNTrain extends AbstractRegressTrainer {
	//	误差率	
	private final double ERROR_RATE = 0.01;
	//迭代次数
	private final int ITER_NUM = 1000;
	
	BaseThreeLayerNN threeLayerNN;	
	
	InstanceSetD inputFeature;  //输入的数据	
	int input_size; //输入数据的总量
	
	double[] token_tag;	
	double eta_1 = 0.5;
	double eta_2 = 0.5;
	double errorRate = 0;
	
	int inputNumb;
	int hiddenNumb;
	int outputNumber;
	
	ArrayList<double[]> deltaV;
	ArrayList<double[]> deltaW;
	
	public BaseThreeLayerNN getBaseThreeLayerNN()
	{
		return threeLayerNN;
	}
	
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
	
	
	public BPNNTrain(int inputNumb, NeuronOriginal[] hiddenLayer, NeuronOriginal[] outputLayer)
	{
		this.inputNumb = inputNumb;
		this.hiddenNumb = hiddenLayer.length;
		this.outputNumber = outputLayer.length;
		threeLayerNN = new BaseThreeLayerNN(inputNumb,hiddenLayer,outputLayer);
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
		//threeLayerNN.showNeuronLayer();
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
//			System.out.println("global error: "+errorRate);
			for(int i = 0;i<input_size;++i)
			{				
				double[] inputVector = inputFeature.getInstanceD(i).getVector();
				setTokenValue(inputFeature.getInstanceD(i).getType());
				
				threeLayerNN.infer(inputVector);
//				threeLayerNN.showNeuronLayerWeight();
//				threeLayerNN.showNeuronLayerOutput();
//				System.out.println("outputlayer result:"+Arrays.toString(threeLayerNN.getOutputVector()));
				
				calculateDelta();	
				calculateGlobalError();
				
				iterateWightVector();  //迭代权重向量
//				threeLayerNN.showNeuronLayerWeight();
				

//				if(i==1)
//				{
//					break;
//				}
			}
			System.out.println("global error: "+errorRate);
			System.out.println("iter_numb:"+iter_numb);
			if(errorRate < ERROR_RATE)
			{
				break;
			}
			else if(++iter_numb > ITER_NUM)
			{
				break;
			}	
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
//				System.out.println("output deltaWjk:"+deltaWjk);
				double[] tempArray = deltaW.get(j);
				double Wjk = deltaW.get(j)[k] + deltaWjk;
//				System.out.println("j:"+j+" k:"+k+" deltaWjk:"+deltaWjk);
				tempArray[k] = Wjk / 2;
				deltaW.set(j, tempArray);
			}
			
//			System.out.println("deltaWj"+Arrays.toString(deltaW.get(j)));
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
//				System.out.println("output deltaVki:"+deltaVki);
//				System.out.println("output deltaV.get(k)[i]:"+deltaV.get(k)[i]);
				double[] tempArray = deltaV.get(k);
				double Vki = deltaV.get(k)[i] + deltaVki;
//				System.out.println("output Vki:"+Vki);
				tempArray[i] = Vki / 2;
//				tempArray[i] = deltaVki;
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
//		System.out.println(token);
		for(int i=0;i<outputNumber;++i)
		{
			token_tag[i] = 0;
		}
		token_tag[0] = token;
//		System.out.println("tokens:"+Arrays.toString(token_tag));
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
//		System.out.println(inputFeature);

		
//		double[][][] input_vector =   {{{0.0, 0.0}, {0.0}},
//			    {{0.0, 1.0}, {1.0}},
//			    {{1.0, 0.0}, {1.0}},
//			    {{1.0, 1.0}, {0.0}}
//			    };
//		InstanceSetD inputFeature = new InstanceSetD();
//		for(int i=0;i<input_vector.length;++i)
//		{
//			inputFeature.add(new InstanceD((int)input_vector[i][1][0],input_vector[i][0].length,input_vector[i][0]));
//		}
//		
		NeuronOriginal[] hiddenLayer = new NeuronOriginal[8];
		NeuronOriginal[] outputLayer = new NeuronOriginal[1];
 		hiddenLayer[0] = new NeuronOriginal(new double[]{0.7564041226474185, 0.42252503803428587, 0.6627626223255502, 0.9752596475352981, 0.22172134228347462});
		hiddenLayer[1] = new NeuronOriginal(new double[]{0.23469542637638974, 0.8730077505454303, 0.8475926798857802, 0.1387905405654285, 0.4516427309706035});
		hiddenLayer[2] = new NeuronOriginal(new double[]{0.036715873948607536, 0.7298165003417677, 0.446715315140231, 0.3504336798287797, 0.4236370873997892});
		hiddenLayer[3] = new NeuronOriginal(new double[]{0.35536502497436695, 0.6547252318955701, 0.9941972871817113, 0.4508485498660191, 0.23531512710593916});
		hiddenLayer[4] = new NeuronOriginal(new double[]{0.5169322921330748, 0.3680581063991989, 0.6287778901783017, 0.8664013223098325, 0.5679303071550258});
		hiddenLayer[5] = new NeuronOriginal(new double[]{0.0008145922239577441, 0.9215652000353717, 0.6335013924820957, 0.35178605112461037, 0.6496272626586509});
		hiddenLayer[6] = new NeuronOriginal(new double[]{0.4328117248944039, 0.35873386046966127, 0.4658828280035243, 0.7008460996637129, 0.1141863469235217});
		hiddenLayer[7] = new NeuronOriginal(new double[]{0.5742706322429426, 0.3133590515476936, 0.5616419887119258, 0.21826025191391285, 0.23774543863439557});
		outputLayer[0] = new NeuronOriginal(new double[]{0.26504468122437685, 0.14244966635377276, 0.36526126484656596, 0.3009178348861473, 0.2852700913267142, 0.7515927877764141, 0.8289900159779138, 0.5950682652905587, 0.6570992787762965});
		
		int inputNumb = 4;
		int hiddenNumb = 8;
		int outputNumb = 1;
		BPNNTrain bpNNTrain = new BPNNTrain(inputNumb,hiddenNumb,outputNumb);
		bpNNTrain.init(inputFeature);
		bpNNTrain.train();
		
		BaseThreeLayerNN threeLayerNN = bpNNTrain.getBaseThreeLayerNN();
		double[] inputVector = {5.7, 2.6, 3.5, 1.0};
		threeLayerNN.infer(inputVector);
		System.out.println("infer result:"+Arrays.toString(threeLayerNN.getOutputVector()));
	}


}
