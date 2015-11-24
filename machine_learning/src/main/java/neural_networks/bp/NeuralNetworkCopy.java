package neural_networks.bp;

import java.util.ArrayList;
import java.util.Random;

import neural_networks.NeuronLayer;

public class NeuralNetworkCopy {
	double LEARNING_RATE = 0.5;
	int num_inputs;
	NeuronLayer hidden_layer;
	NeuronLayer output_layer;
	

		    public NeuralNetworkCopy(int num_inputs, int num_hidden, int num_outputs, double[] hidden_layer_weights,double hidden_layer_bias, double[] output_layer_weights, double output_layer_bias)
		    {
		    	this.num_inputs = num_inputs;

		    	this.hidden_layer = new NeuronLayer(num_hidden, hidden_layer_bias);
		    	this.output_layer = new NeuronLayer(num_outputs, output_layer_bias);

				        this.init_weights_from_inputs_to_hidden_layer_neurons(hidden_layer_weights)
				        this.init_weights_from_hidden_layer_neurons_to_output_layer_neurons(output_layer_weights)
		    }
		    	
		    	

		    void init_weights_from_inputs_to_hidden_layer_neurons(double[] hidden_layer_weights)
		    {
		        int weight_num = 0;
		        		Random random = new Random();
		        for(int h=0;h<this.hidden_layer.neurons.size();++h)
		        {
		        	 for(int i=0;i<this.num_inputs;++i)
		        	 {
		        		 if (hidden_layer_weights == null)
		        		 {
		        			 double tempRandom = random.nextDouble();
		        			 this.hidden_layer.neurons.get(h).weights.add(tempRandom);
		        		 }
		        		 else{
		        			 this.hidden_layer.neurons.get(h).weights.add(hidden_layer_weights[weight_num]);
		        		 }
			                	
			                weight_num += 1;
		        	 }
			               
		        }
		           
		    }


		    void init_weights_from_hidden_layer_neurons_to_output_layer_neurons(double[] output_layer_weights)
		    {
		        int weight_num = 0;
		        Random random = new Random();
		        for(int o = 0;o< this.output_layer.neurons.size();++o)
		        {
		        	for(int h=0;h< this.hidden_layer.neurons.size();++h)
		        	{
		                if (output_layer_weights == null)
		                {
		                	double tempRandom = random.nextDouble();
		                    this.output_layer.neurons.get(o).weights.add(tempRandom);
		                }
		                	
		                else
		                {
		                	this.output_layer.neurons.get(o).weights.add(output_layer_weights[weight_num]);
		                }
		                    
		                weight_num += 1;
		        	}

		        }
		    }

		    void inspect()
		    {
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
		       

		    double[] feed_forward(double[] inputs)
		    {
		        double[] hidden_layer_outputs = this.hidden_layer.feed_forward(inputs);
		        return this.output_layer.feed_forward(hidden_layer_outputs);
		    }


		    
		    void train(double[] training_inputs, double[] training_outputs)
		    {
		    	  this.feed_forward(training_inputs);

			        
			        double[] pd_errors_wrt_output_neuron_total_net_input = new double[this.output_layer.neurons.size()];
			        for(int o=0;o< this.output_layer.neurons.size();o++)
			        {
			        	pd_errors_wrt_output_neuron_total_net_input[o] = this.output_layer.neurons.get(o).calculate_pd_error_wrt_total_net_input(training_outputs[o]);
			        }
			            

			        
			        double[] pd_errors_wrt_hidden_neuron_total_net_input = new double[this.hidden_layer.neurons.size()];
			        for(int h=0;h< this.hidden_layer.neurons.size();++h)
			        {
				            double d_error_wrt_hidden_neuron_output = 0;
				            for(int o=0; o<this.output_layer.neurons.size();++o)
				            {
				            	 d_error_wrt_hidden_neuron_output += pd_errors_wrt_output_neuron_total_net_input[o] * this.output_layer.neurons.get(o).weights.get(h);
				            }
				               
				            pd_errors_wrt_hidden_neuron_total_net_input[h] = d_error_wrt_hidden_neuron_output * this.hidden_layer.neurons[h].calculate_pd_total_net_input_wrt_input()
			        }

			           

			        # 3. Update output neuron weights
			        for o in range(len(this.output_layer.neurons)):
			            for w_ho in range(len(this.output_layer.neurons[o].weights)):

			                # ∂Eⱼ/∂wᵢⱼ = ∂E/∂zⱼ * ∂zⱼ/∂wᵢⱼ
			                pd_error_wrt_weight = pd_errors_wrt_output_neuron_total_net_input[o] * this.output_layer.neurons[o].calculate_pd_total_net_input_wrt_weight(w_ho)

			                # Δw = α * ∂Eⱼ/∂wᵢ
			                this.output_layer.neurons[o].weights[w_ho] -= this.LEARNING_RATE * pd_error_wrt_weight

			        # 4. Update hidden neuron weights
			        for h in range(len(this.hidden_layer.neurons)):
			            for w_ih in range(len(this.hidden_layer.neurons[h].weights)):

			                # ∂Eⱼ/∂wᵢ = ∂E/∂zⱼ * ∂zⱼ/∂wᵢ
			                pd_error_wrt_weight = pd_errors_wrt_hidden_neuron_total_net_input[h] * this.hidden_layer.neurons[h].calculate_pd_total_net_input_wrt_weight(w_ih)

			                # Δw = α * ∂Eⱼ/∂wᵢ
			                this.hidden_layer.neurons[h].weights[w_ih] -= this.LEARNING_RATE * pd_error_wrt_weight
		    }
		      

		    def calculate_total_error(self, training_sets):
		        total_error = 0
		        for t in range(len(training_sets)):
		            training_inputs, training_outputs = training_sets[t]
		            this.feed_forward(training_inputs)
		            for o in range(len(training_outputs)):
		                total_error += this.output_layer.neurons[o].calculate_error(training_outputs[o])
		        return total_error

}
