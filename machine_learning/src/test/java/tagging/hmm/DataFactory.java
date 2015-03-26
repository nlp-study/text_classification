package tagging.hmm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class DataFactory {
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

	int[] trainSequence;
	
	int inputSize;

	public void readFile(String path) throws IOException {
		File file = new File(path);
		BufferedReader bf = new BufferedReader(new FileReader(file));

		int line = 0;
		String str = bf.readLine();
		
		while (str != null) {
			++line;
			if (line == 1) {
				String[] temp = str.split(" ");
				int n = Integer.parseInt(temp[0]);
				int m = Integer.parseInt(temp[1]);

				A = new double[n][m];
				int j = 0;
				int i = 0;
				for (int k = 2; k < temp.length; ++k) {
					if (j < m) {
						A[i][j] = Double.parseDouble(temp[k]);
						++j;
					} else {
						++i;
						j = 0;
						
						A[i][j] = Double.parseDouble(temp[k]);
						++j;
					}
					
				}
				System.out.println("A:" + Arrays.deepToString(A));

			} else if (line == 2) {
				String[] temp = str.split(" ");
				int n = Integer.parseInt(temp[0]);
				int m = Integer.parseInt(temp[1]);

				B = new double[n][m];
				int j = 0;
				int i = 0;
				for (int k = 2; k < temp.length; ++k) {
					if (j < m) {
						B[i][j] = Double.parseDouble(temp[k]);
						++j;
					} else {
						++i;
						j = 0;
						B[i][j] = Double.parseDouble(temp[k]);
						++j;
					}					
				}
				System.out.println("B:" + Arrays.deepToString(B));
			} else if (line == 3) {
				String[] temp = str.split(" ");
				int n = Integer.parseInt(temp[1]);

				pi = new double[n];
				int i = 0;
				for (int k = 2; k < temp.length;++k) {
					if (i < n) {
						pi[i] = Double.parseDouble(temp[k]);
						++i;
					}
					
				}
				System.out.println("pi:" + Arrays.toString(pi));

			} else if (line == 4) {
				String[] temp = str.split(" ");
				int n = Integer.parseInt(temp[0]);
				inputSize = n;

				trainSequence = new int[n];
				int i = 0;
				for (int k = 1; k < temp.length;++k) {					
						trainSequence[i] = Integer.parseInt(temp[k]);
						++i;					
				}
				System.out.println("trainSequence:" + Arrays.toString(trainSequence));
				System.out.println("trainSequence size:" + trainSequence.length);
			}
			str = bf.readLine();
		}
	}

	public double[][] getA() {
		return A;
	}

	public void setA(double[][] a) {
		A = a;
	}

	public double[][] getB() {
		return B;
	}

	public void setB(double[][] b) {
		B = b;
	}

	public double[] getPi() {
		return pi;
	}

	public void setPi(double[] pi) {
		this.pi = pi;
	}

	public int[] getTrainSequence() {
		return trainSequence;
	}

	public void setTrainSequence(int[] trainSequence) {
		this.trainSequence = trainSequence;
	}

	public int getInputSize() {
		return inputSize;
	}

	public void setInputSize(int inputSize) {
		this.inputSize = inputSize;
	}	

}
