package neural_networks;

import java.util.ArrayList;

public class NeuronLayer {
	double bias = 0;
	public ArrayList<Neuron> neurons = new ArrayList<Neuron>();
	int neuronsNumb;

	public NeuronLayer(int neuronsNumb, double bias) {
		// Every neuron in a layer shares the same bias
		this.bias = bias;
		this.neuronsNumb = neuronsNumb;

		for (int i = 0; i < neuronsNumb; ++i) {
			this.neurons.add(new Neuron(bias));
		}

	}

	public void inspect() {

		for (int n = 0; n < neuronsNumb; ++n) {
			int weightLength = this.neurons.get(n).weights.size();
			for (int w = 0; w < weightLength; ++w) {
				System.out.println("Weight:" + neurons.get(n).weights.get(w));
			}

			System.out.println("Bias:" + this.bias);
		}

	}

	public double[] feed_forward(double[] inputs) {
		double[] outputs = new double[neuronsNumb];

		for (int i = 0; i < neuronsNumb; ++i) {
			double output = neurons.get(i).calculateOutput(inputs);
			outputs[i] = output;
		}

		return outputs;
	}

	public double[] get_outputs() {
		double[] outputs = new double[neuronsNumb];

		for (int i = 0; i < neuronsNumb; ++i) {
			outputs[i] = neurons.get(i).output;
		}

		return outputs;
	}

}
