package tagging.hmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import tagging.TaggingInput;
import tagging.TaggingInstance;
import tagging.TaggingTrainer;
import util.MatrixOperation;
import util.VectorOperation;

/**
 * @author xiaohe 创建于：2015年3月9日 隐马尔科夫模型
 */
public class HMMTrainer extends TaggingTrainer {
	Logger logger = Logger.getLogger(HMMTrainer.class);

	// 最大迭代次数
	private static final int ITERATIONS_NUMB = 1000;
	private static final double ITERATIONS_VALUE = 0.1;

	/**
	 * 用于训练的样本是否标注
	 */
	boolean isTagging;
	/**
	 * 状态转移矩阵
	 */
	double[][] A;
	/**
	 * 发射概率
	 */
	double[][] B;
	/**
	 * 初始概率
	 */
	double[] pi;
	/**
	 * 前向概率
	 */
	double[][] alpha;
	/**
	 * 后向概率
	 */
	double[][] beta;

	// 内部参数
	/**
	 * 状态的数量，状体的最大id是：stateSize-1
	 */
	int stateSize;
	/**
	 * 输入的数量，状体的最大id是：inputSize-1
	 */
	int inputSize;
	/**
	 * 词的数量，状体的最大id是：wordSize-1
	 */
	int wordSize;

	/**
	 * 输入的语料对
	 */
	List<TaggingInput> instances = new ArrayList<TaggingInput>();

	@Override
	public void init(TaggingInstance instances) {
		this.stateSize = instances.getMaxTaggingID() + 1;
		this.wordSize = instances.getMaxWordID() + 1;
		this.inputSize = instances.getInstanceSize();
		this.instances = instances.getInstances();
		this.isTagging = instances.isTagging();

		A = new double[stateSize][stateSize];
		B = new double[stateSize][wordSize];
		pi = new double[stateSize];
		alpha = new double[stateSize][inputSize];
		beta = new double[stateSize][inputSize];

	}

	public void temporaryInit(double[][] A, double[][] B, double[] pi,
			int stateSize, int inputSize, List<TaggingInput> instances) {
		this.A = A;
		this.B = B;
		this.pi = pi;

		alpha = new double[inputSize][stateSize];
		beta = new double[inputSize][stateSize];

		this.inputSize = inputSize;
		this.stateSize = stateSize;
		this.instances = instances;
	}

	private void parameterInit() {
		for (int i = 0; i < stateSize; ++i) {
			pi[i] = 1.0 / stateSize;
			Arrays.fill(A[i], 1.0 / stateSize);
			Arrays.fill(B[i], 1.0 / inputSize);
		}
	}

	@Override
	public void train() {

		if (isTagging) {
			logger.info("*************trainWithTagging*************");
			trainWithTagging();
		} else {
			logger.info("*************trainWithoutTagging*************");
			trainWithoutTagging();
		}

	}

	/**
	 * @comment:如果样本本身有标注
	 * @return:void
	 */
	public void trainWithTagging() {
		estimateA();
		estimateB();
		estimatePi();
	}
	
	public void estimateA() {
		int[][] countA = new int[stateSize][stateSize];
		int[] coutAI = new int[stateSize];
		
		for(int t=0;t<inputSize-1;++t)
		{
			countA[instances.get(t).getTagging()][instances.get(t+1).getTagging()]++;
			coutAI[instances.get(t).getTagging()]++;
		}
		
		for(int i=0;i<stateSize;++i)
		{
			for(int j=0;j<stateSize;++j)
			{
				A[i][j] = (double)countA[i][j]/coutAI[i];
			}
		}
	}

	public void estimateB() {
		int[][] countB = new int[stateSize][inputSize];
		int[] coutBI = new int[stateSize];
		
		for(int t=0;t<inputSize;++t)
		{
			countB[instances.get(t).getTagging()][instances.get(t).getWord()]++;
			coutBI[instances.get(t).getTagging()]++;
		}
		
		for(int i=0;i<stateSize;++i)
		{
			for(int j=0;j<stateSize;++j)
			{
				B[i][j] = (double)countB[i][j]/coutBI[i];
			}
		}
	}

	public void estimatePi() {
		int[] tempPI = new int[stateSize];
		for (int t = 0; t < inputSize; ++t) {
			tempPI[instances.get(t).getTagging()]++;
		}
		
		for(int i = 0; i < stateSize; ++i)
		{
			pi[i] = (double)tempPI[i]/inputSize;
		}
	}

	/**
	 * @comment:样本本身没有标注
	 * @return:void
	*/
	public void trainWithoutTagging() {
		parameterInit();

		double[][] lastA = new double[stateSize][stateSize];
		double[][] lastB = new double[stateSize][wordSize];
		double[] lastPi = new double[stateSize];

		int iteratNumb = 0;

		do {
			++iteratNumb;
			for (int i = 0; i < 2; ++i) {
				System.arraycopy(A[i], 0, lastA[i], 0, A[i].length);
				System.arraycopy(B[i], 0, lastB[i], 0, B[i].length);
				System.arraycopy(pi, 0, lastPi, 0, pi.length);
			}

			iteration();

			if (ITERATIONS_NUMB < iteratNumb) {
				break;
			}
		} while (isStop(lastA, lastB, lastPi));
	}

	/**
	 * @comment:对没有标注的语料进行迭代
	 * @return:void
	 */
	public void iteration() {
		forward();
		backward();

		double[][][] epsilon = calculateEpsilon();
		double[][] gamma = calculateGamma();

		estimateA(epsilon, gamma);
		estimateB(epsilon, gamma);
		estimatePi(gamma);
	}

	public double[][][] calculateEpsilon() {
		double[][][] epsilon = new double[inputSize][stateSize][stateSize];
		double sum = 0;

		for (int i = 0; i < stateSize; ++i) {
			sum += alpha[inputSize - 1][i];
		}

		for (int t = 0; t < inputSize - 1; ++t) {
			for (int i = 0; i < stateSize; ++i) {
				for (int j = 0; j < stateSize; ++j) {
					epsilon[t][i][j] = alpha[t][i] * A[i][j]
							* B[j][instances.get(t).getWord()] * beta[t + 1][j]
							/ sum;
				}
			}
		}

		return epsilon;
	}

	public double[][] calculateGamma() {
		double[][] gamma = new double[inputSize][stateSize];
		double sum = 0;

		for (int i = 0; i < stateSize; ++i) {
			sum += alpha[inputSize - 1][i];
		}

		for (int t = 0; t < inputSize - 1; ++t) {
			for (int i = 0; i < stateSize; ++i) {
				gamma[t][i] = alpha[t][i] * beta[t][i] / sum;
			}
		}

		return gamma;
	}

	public void estimateA(double[][][] epsilon, double[][] gamma) {
		for (int i = 0; i < stateSize; ++i) {
			for (int j = 0; j < stateSize; ++j) {
				double sumEpsilon = 0;
				double sumGamma = 0;
				for (int t = 0; t < inputSize - 1; ++t) {
					sumEpsilon += epsilon[t][i][j];
					sumGamma += gamma[t][i];
				}
				A[i][j] = sumEpsilon / sumGamma;
			}
		}
	}

	public void estimateB(double[][][] epsilon, double[][] gamma) {
		for (int j = 0; j < stateSize; ++j) {
			double sumPartGamma = 0;
			double sumGamma = 0;
			for (int k = 0; k < inputSize; ++k) {
				for (int t = 0; t < inputSize; ++t) {
					if (instances.get(t).getWord() == k) {
						sumPartGamma += gamma[t][j];
					}

					sumGamma += gamma[t][j];
				}
				B[j][k] = sumPartGamma / sumGamma;
			}
		}
	}

	public void estimatePi(double[][] gamma) {
		for (int i = 0; i < stateSize; ++i) {
			pi[i] = gamma[0][i];
		}
	}

	/**
	 * @comment:前端算法
	 * @return:void
	 */
	public void forward() {
		for (int t = 0; t < inputSize; ++t) {
			if (t == 0) {
				for (int j = 0; j < stateSize; ++j) {
					alpha[0][j] = pi[j] * B[j][instances.get(t).getWord()];
				}
			} else {
				for (int i = 0; i < stateSize; ++i) {
					alpha[t][i] = 0;

					for (int j = 0; j < stateSize; ++j) {
						alpha[t][i] += alpha[t - 1][j] * A[j][i];
					}
					alpha[t][i] *= B[i][instances.get(t).getWord()];
				}
			}

			logger.info(Arrays.toString(alpha[t]));
		}

		double sum = 0;

		for (int i = 0; i < stateSize; ++i) {
			sum += alpha[inputSize - 1][i];
		}

		logger.info("alpha prob is:" + sum);
	}

	/**
	 * @comment:后向算法
	 * @return:void
	 */
	public void backward() {
		for (int t = inputSize - 1; t >= 0; --t) {
			if (t == inputSize - 1) {
				Arrays.fill(beta[t], 1);
			} else {
				for (int i = 0; i < stateSize; ++i) {
					beta[t][i] = 0;

					for (int j = 0; j < stateSize; ++j) {
						beta[t][i] += A[i][j]
								* B[j][instances.get(t + 1).getWord()]
								* beta[t + 1][j];
					}
				}
			}

			logger.info(Arrays.toString(beta[t]));
		}

		double sum = 0;

		for (int i = 0; i < stateSize; ++i) {
			sum += pi[i] * B[i][instances.get(0).getWord()] * beta[0][i];
		}

		logger.info("beta prob is:" + sum);
	}

	
	/**
	 * @comment:判断是否停止迭代
	 * @param lastA
	 * @param lastB
	 * @param lastPi
	 * @return
	 * @return:boolean
	 */
	public boolean isStop(double[][] lastA, double[][] lastB, double[] lastPi) {
		double epsA = MatrixOperation.errorSumSquares(A, lastA);

		double epsB = MatrixOperation.errorSumSquares(B, lastB);
		double epsPi = VectorOperation.errorSumSquares(pi, lastPi);

		logger.info("epsA:" + epsA + "  epsB:" + epsB + "  epsPi:" + epsPi);

		if (epsA > ITERATIONS_VALUE) {
			return false;
		}

		if (epsB > ITERATIONS_VALUE) {
			return false;
		}

		if (epsPi > ITERATIONS_VALUE) {
			return false;
		}

		return true;
	}

	@Override
	public void clear() {
		isTagging = false;		
		A = null;		
		B = null;		
		pi = null;		
		alpha = null;		
		beta = null;
		stateSize = 0;
		inputSize = 0;
		wordSize = 0;
		instances = null;
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		HMMTrainer hmmTrainer = new HMMTrainer();
		double[][] A = { { 0.5, 0.2, 0.3 }, { 0.3, 0.5, 0.2 },
				{ 0.2, 0.3, 0.5 } };
		double[][] B = { { 0.5, 0.5 }, { 0.4, 0.6 }, { 0.7, 0.3 } };
		double[] pi = { 0.2, 0.4, 0.4 };
		int stateSize = 3;
		int inputSize = 3;

		TaggingInput taggingInput1 = new TaggingInput(0, -1);
		TaggingInput taggingInput2 = new TaggingInput(1, -1);
		TaggingInput taggingInput3 = new TaggingInput(0, -1);

		List<TaggingInput> instances = new ArrayList<TaggingInput>();
		instances.add(taggingInput1);
		instances.add(taggingInput2);
		instances.add(taggingInput3);

		hmmTrainer.temporaryInit(A, B, pi, stateSize, inputSize, instances);
		hmmTrainer.forward();
		hmmTrainer.backward();
	}
}
