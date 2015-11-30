package neural_networks.bp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import neural_networks.NeuronLayer;

public class NeuralNetworkCopy {
	double LEARNING_RATE = 0.5;
	int num_inputs;
	NeuronLayer hidden_layer;
	NeuronLayer output_layer;

	public NeuralNetworkCopy(int num_inputs, int num_hidden, int num_outputs,
			double[] hidden_layer_weights, double hidden_layer_bias,
			double[] output_layer_weights, double output_layer_bias) {
		this.num_inputs = num_inputs;

		this.hidden_layer = new NeuronLayer(num_hidden, hidden_layer_bias);
		this.output_layer = new NeuronLayer(num_outputs, output_layer_bias);

		this.init_weights_from_inputs_to_hidden_layer_neurons(hidden_layer_weights);
		this.init_weights_from_hidden_layer_neurons_to_output_layer_neurons(output_layer_weights);
	}

	void init_weights_from_inputs_to_hidden_layer_neurons(
			double[] hidden_layer_weights) {
		int weight_num = 0;
		Random random = new Random();
		for (int h = 0; h < this.hidden_layer.neurons.size(); ++h) {
			for (int i = 0; i < this.num_inputs+1; ++i) {
				if (hidden_layer_weights == null) {
					double tempRandom = random.nextDouble();
					this.hidden_layer.neurons.get(h).weights.add(tempRandom);
				} else {
//					System.out.println(Arrays.toString(hidden_layer_weights));
//					System.out.println(Arrays.toString(hidden_layer_weights));
					this.hidden_layer.neurons.get(h).weights
							.add(hidden_layer_weights[weight_num]);
				}

				weight_num += 1;
			}

		}

	}

	void init_weights_from_hidden_layer_neurons_to_output_layer_neurons(
			double[] output_layer_weights) {
		int weight_num = 0;
		Random random = new Random();
		for (int o = 0; o < this.output_layer.neurons.size(); ++o) {
			for (int h = 0; h < this.hidden_layer.neurons.size()+1; ++h) {
				if (output_layer_weights == null) {
					double tempRandom = random.nextDouble();
					this.output_layer.neurons.get(o).weights.add(tempRandom);
				}

				else {
					this.output_layer.neurons.get(o).weights
							.add(output_layer_weights[weight_num]);
				}

				weight_num += 1;
			}

		}
	}

	void inspect() {
		System.out.println("--------");
		System.out.println(this.num_inputs);
		System.out.println("------");
		System.out.println("Hidden Layer");
		this.hidden_layer.inspect();
		System.out.println("------");
		System.out.println("* Output Layer");
		this.output_layer.inspect();
		System.out.println("------");
	}

	double[] feed_forward(double[] inputs) {
		double[] hidden_layer_outputs = this.hidden_layer.feed_forward(inputs);
		return this.output_layer.feed_forward(hidden_layer_outputs);
	}

	void train(double[] training_inputs, double[] training_outputs) {
		this.feed_forward(training_inputs);

		double[] pd_errors_wrt_output_neuron_total_net_input = new double[this.output_layer.neurons
				.size()];
		for (int o = 0; o < this.output_layer.neurons.size(); o++) {
			pd_errors_wrt_output_neuron_total_net_input[o] = this.output_layer.neurons
					.get(o).calculate_pd_error_wrt_total_net_input(
							training_outputs[o]);
		}

		double[] pd_errors_wrt_hidden_neuron_total_net_input = new double[this.hidden_layer.neurons
				.size()];
		for (int h = 0; h < this.hidden_layer.neurons.size(); ++h) {
			double d_error_wrt_hidden_neuron_output = 0;
			for (int o = 0; o < this.output_layer.neurons.size(); ++o) {
				d_error_wrt_hidden_neuron_output += pd_errors_wrt_output_neuron_total_net_input[o]
						* this.output_layer.neurons.get(o).weights.get(h);
			}

			pd_errors_wrt_hidden_neuron_total_net_input[h] = d_error_wrt_hidden_neuron_output
					* this.hidden_layer.neurons.get(h)
							.calculate_pd_total_net_input_wrt_input();
		}

		for (int o = 0; o < this.output_layer.neurons.size(); ++o) {
			for (int w_ho = 0; w_ho < this.output_layer.neurons.get(o).weights
					.size(); ++w_ho) {
				double pd_error_wrt_weight = pd_errors_wrt_output_neuron_total_net_input[o]
						* this.output_layer.neurons.get(o)
								.calculate_pd_total_net_input_wrt_weight(w_ho);

				double tempWeightValue = this.output_layer.neurons.get(o).weights
						.get(w_ho);
				tempWeightValue -= this.LEARNING_RATE * pd_error_wrt_weight;
				this.output_layer.neurons.get(o).weights.set(w_ho,
						tempWeightValue);
			}

		}

		for (int h = 0; h < this.hidden_layer.neurons.size(); ++h) {
			for (int w_ih = 0; w_ih < this.hidden_layer.neurons.get(h).weights
					.size(); ++w_ih) {

				double pd_error_wrt_weight = pd_errors_wrt_hidden_neuron_total_net_input[h]
						* this.hidden_layer.neurons.get(h)
								.calculate_pd_total_net_input_wrt_weight(w_ih);
				double tempWeightValue = this.hidden_layer.neurons.get(h).weights
						.get(w_ih);
				tempWeightValue -= this.LEARNING_RATE * pd_error_wrt_weight;
				this.hidden_layer.neurons.get(h).weights.set(w_ih,
						tempWeightValue);
			}

		}

	}

	double calculate_total_error(double[][][] training_sets) {
		double total_error = 0;
		for (int t = 0; t < training_sets.length; ++t) {
			double[][] temp_set = training_sets[t];
			double[] training_inputs = temp_set[0];
			double[] training_outputs = temp_set[1];

			this.feed_forward(training_inputs);
			for (int o = 0; o < training_outputs.length; ++o) {
				total_error += this.output_layer.neurons.get(o)
						.calculate_error(training_outputs[o]);
			}
		}

		return total_error;
	}

	public static void main(String[] args) {
		
		double[][][] training_sets ={{{5.0,3.5,1.3,0.3},{0,0,1}},
		{{4.5,2.3,1.3,0.3},{0,0,1}},
		{{4.4,3.2,1.3,0.2},{0,0,1}},
		{{5.0,3.5,1.6,0.6},{0,0,1}},
		{{5.1,3.8,1.9,0.4},{0,0,1}},
		{{5.1,3.5,1.4,0.2},{0,0,1}},
		{{4.9,3.0,1.4,0.2},{0,0,1}},
		{{4.7,3.2,1.3,0.2},{0,0,1}},
		{{4.6,3.1,1.5,0.2},{0,0,1}},
		{{5.0,3.6,1.4,0.2},{0,0,1}},
		{{5.4,3.9,1.7,0.4},{0,0,1}},
		{{4.6,3.4,1.4,0.3},{0,0,1}},
		{{5.0,3.4,1.5,0.2},{0,0,1}},
		{{4.4,2.9,1.4,0.2},{0,0,1}},
		{{4.9,3.1,1.5,0.1},{0,0,1}},
		{{5.4,3.7,1.5,0.2},{0,0,1}},
		{{4.8,3.4,1.6,0.2},{0,0,1}},
		{{4.8,3.0,1.4,0.1},{0,0,1}},
		{{4.3,3.0,1.1,0.1},{0,0,1}},
		{{5.8,4.0,1.2,0.2},{0,0,1}},
		{{5.7,4.4,1.5,0.4},{0,0,1}},
		{{5.4,3.9,1.3,0.4},{0,0,1}},
		{{5.1,3.5,1.4,0.3},{0,0,1}},
		{{5.7,3.8,1.7,0.3},{0,0,1}},
		{{5.1,3.8,1.5,0.3},{0,0,1}},
		{{5.4,3.4,1.7,0.2},{0,0,1}},
		{{5.1,3.7,1.5,0.4},{0,0,1}},
		{{4.6,3.6,1.0,0.2},{0,0,1}},
		{{5.1,3.3,1.7,0.5},{0,0,1}},
		{{4.8,3.4,1.9,0.2},{0,0,1}},
		{{5.0,3.0,1.6,0.2},{0,0,1}},
		{{5.0,3.4,1.6,0.4},{0,0,1}},
		{{5.2,3.5,1.5,0.2},{0,0,1}},
		{{5.2,3.4,1.4,0.2},{0,0,1}},
		{{4.7,3.2,1.6,0.2},{0,0,1}},
		{{4.8,3.1,1.6,0.2},{0,0,1}},
		{{5.4,3.4,1.5,0.4},{0,0,1}},
		{{5.2,4.1,1.5,0.1},{0,0,1}},
		{{5.5,4.2,1.4,0.2},{0,0,1}},
		{{4.9,3.1,1.5,0.1},{0,0,1}},
		{{5.0,3.2,1.2,0.2},{0,0,1}},
		{{5.5,3.5,1.3,0.2},{0,0,1}},
		{{4.9,3.1,1.5,0.1},{0,0,1}},
		{{4.4,3.0,1.3,0.2},{0,0,1}},
		{{5.1,3.4,1.5,0.2},{0,0,1}},
		{{5.7,3.0,4.2,1.2},{0,1,0}},
		{{5.7,2.9,4.2,1.3},{0,1,0}},
		{{6.2,2.9,4.3,1.3},{0,1,0}},
		{{5.1,2.5,3.0,1.1},{0,1,0}},
		{{5.7,2.8,4.1,1.3},{0,1,0}},
		{{7.0,3.2,4.7,1.4},{0,1,0}},
		{{6.4,3.2,4.5,1.5},{0,1,0}},
		{{6.9,3.1,4.9,1.5},{0,1,0}},
		{{5.5,2.3,4.0,1.3},{0,1,0}},
		{{6.5,2.8,4.6,1.5},{0,1,0}},
		{{5.7,2.8,4.5,1.3},{0,1,0}},
		{{6.3,3.3,4.7,1.6},{0,1,0}},
		{{4.9,2.4,3.3,1.0},{0,1,0}},
		{{6.6,2.9,4.6,1.3},{0,1,0}},
		{{5.2,2.7,3.9,1.4},{0,1,0}},
		{{5.0,2.0,3.5,1.0},{0,1,0}},
		{{5.9,3.0,4.2,1.5},{0,1,0}},
		{{6.0,2.2,4.0,1.0},{0,1,0}},
		{{6.1,2.9,4.7,1.4},{0,1,0}},
		{{5.6,2.9,3.6,1.3},{0,1,0}},
		{{6.7,3.1,4.4,1.4},{0,1,0}},
		{{5.6,3.0,4.5,1.5},{0,1,0}},
		{{5.8,2.7,4.1,1.0},{0,1,0}},
		{{6.2,2.2,4.5,1.5},{0,1,0}},
		{{5.6,2.5,3.9,1.1},{0,1,0}},
		{{5.9,3.2,4.8,1.8},{0,1,0}},
		{{6.1,2.8,4.0,1.3},{0,1,0}},
		{{6.3,2.5,4.9,1.5},{0,1,0}},
		{{6.1,2.8,4.7,1.2},{0,1,0}},
		{{6.4,2.9,4.3,1.3},{0,1,0}},
		{{6.6,3.0,4.4,1.4},{0,1,0}},
		{{6.8,2.8,4.8,1.4},{0,1,0}},
		{{6.7,3.0,5.0,1.7},{0,1,0}},
		{{6.0,2.9,4.5,1.5},{0,1,0}},
		{{5.7,2.6,3.5,1.0},{0,1,0}},
		{{5.5,2.4,3.8,1.1},{0,1,0}},
		{{5.5,2.4,3.7,1.0},{0,1,0}},
		{{5.8,2.7,3.9,1.2},{0,1,0}},
		{{6.0,2.7,5.1,1.6},{0,1,0}},
		{{5.4,3.0,4.5,1.5},{0,1,0}},
		{{6.0,3.4,4.5,1.6},{0,1,0}},
		{{6.7,3.1,4.7,1.5},{0,1,0}},
		{{6.3,2.3,4.4,1.3},{0,1,0}},
		{{5.6,3.0,4.1,1.3},{0,1,0}},
		{{5.5,2.5,4.0,1.3},{0,1,0}},
		{{6.3,3.3,6.0,2.5},{1,0,0}},
		{{5.8,2.7,5.1,1.9},{1,0,0}},
		{{7.1,3.0,5.9,2.1},{1,0,0}},
		{{6.3,2.9,5.6,1.8},{1,0,0}},
		{{6.5,3.0,5.8,2.2},{1,0,0}},
		{{7.6,3.0,6.6,2.1},{1,0,0}},
		{{4.9,2.5,4.5,1.7},{1,0,0}},
		{{7.3,2.9,6.3,1.8},{1,0,0}},
		{{6.7,2.5,5.8,1.8},{1,0,0}},
		{{7.2,3.6,6.1,2.5},{1,0,0}},
		{{6.5,3.2,5.1,2.0},{1,0,0}},
		{{6.4,2.7,5.3,1.9},{1,0,0}},
		{{6.8,3.0,5.5,2.1},{1,0,0}},
		{{5.7,2.5,5.0,2.0},{1,0,0}},
		{{5.8,2.8,5.1,2.4},{1,0,0}},
		{{6.4,3.2,5.3,2.3},{1,0,0}},
		{{6.5,3.0,5.5,1.8},{1,0,0}},
		{{7.7,3.8,6.7,2.2},{1,0,0}},
		{{7.7,2.6,6.9,2.3},{1,0,0}},
		{{6.0,2.2,5.0,1.5},{1,0,0}},
		{{6.9,3.2,5.7,2.3},{1,0,0}},
		{{5.6,2.8,4.9,2.0},{1,0,0}},
		{{7.7,2.8,6.7,2.0},{1,0,0}},
		{{6.3,2.7,4.9,1.8},{1,0,0}},
		{{6.7,3.3,5.7,2.1},{1,0,0}},
		{{7.2,3.2,6.0,1.8},{1,0,0}},
		{{6.2,2.8,4.8,1.8},{1,0,0}},
		{{6.1,3.0,4.9,1.8},{1,0,0}},
		{{6.4,2.8,5.6,2.1},{1,0,0}},
		{{7.2,3.0,5.8,1.6},{1,0,0}},
		{{7.4,2.8,6.1,1.9},{1,0,0}},
		{{7.9,3.8,6.4,2.0},{1,0,0}},
		{{6.4,2.8,5.6,2.2},{1,0,0}},
		{{6.3,2.8,5.1,1.5},{1,0,0}},
		{{6.1,2.6,5.6,1.4},{1,0,0}},
		{{7.7,3.0,6.1,2.3},{1,0,0}},
		{{6.3,3.4,5.6,2.4},{1,0,0}},
		{{6.4,3.1,5.5,1.8},{1,0,0}},
		{{6.0,3.0,4.8,1.8},{1,0,0}},
		{{6.9,3.1,5.4,2.1},{1,0,0}},
		{{6.7,3.0,5.2,2.3},{1,0,0}},
		{{6.3,2.5,5.0,1.9},{1,0,0}},
		{{6.5,3.0,5.2,2.0},{1,0,0}},
		{{6.2,3.4,5.4,2.3},{1,0,0}},
		{{5.9,3.0,5.1,1.8},{1,0,0}},};
		double accuracy = 0.0;
		int total_numb = training_sets.length;
		int error_numb = 0;
		int IterationNumb = 300000;
		int IterationError = 2;

		NeuralNetworkCopy nn = new NeuralNetworkCopy(
				training_sets[0][0].length, 12, training_sets[0][1].length,
				null, 0, null,0);
		
		for (int i = 0; i < IterationNumb; ++i) {
			for(int j=0;j<training_sets.length;++j)
			{
				double[] training_inputs = training_sets[j][0];
				double[] training_outputs = training_sets[j][1];

				nn.train(training_inputs, training_outputs);
			}
			System.out.println("total error:"+nn.calculate_total_error(training_sets));
			double error = nn.calculate_total_error(training_sets);
			if(error < IterationError)
			{
				break;
			}
		}
		
		for(int j=0;j<training_sets.length;++j)
		{
			double[] temp_result = nn.feed_forward(training_sets[j][0]);
			for(int i=0;i<temp_result.length;++i)
			{
				if(temp_result[i]>0.5)
				{
					temp_result[i] = 1.0;
				}
				else
				{
					temp_result[i] = 0.0;
				}
			}
			if( !Arrays.equals(temp_result,training_sets[j][1]))
			{
				System.out.print(" Error:   ");
				System.out.print(Arrays.toString(temp_result) + " -- ");
				System.out.println(Arrays.toString(training_sets[j][1]));
				error_numb++;
			}
		}

		accuracy = (double)(total_numb - error_numb) / total_numb;
		System.out.println("accuracy:"+accuracy);
	}

}
