package neural_networks.bp;

import base.InstanceSetD;
import regress.AbstractRegressTrainer;

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
	
	int input_size = 0;
	double eta_1 = 0;
	double eta_2 = 0;
	
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
		inputLayer = new double[inputNumb];
		hiddenLayer = new NeuralNetworkNode[hiddenNumb];
		outputLayer = new NeuralNetworkNode[outputNumber];
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
				for(int j)
				{
					
				}
				hiddenGradient += getHiddenGradient(this.inputFeature.getInstanceD(i).getVector());	
				hiddenGradient += getOutputGradient(this.inputFeature.getInstanceD(i).getVector());
			}
			
			if(errorRate < ERROR_RATE)
			{
				break;
			}
		}
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
		// TODO Auto-generated method stub

	}

}
