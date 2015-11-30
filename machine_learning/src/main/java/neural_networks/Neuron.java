package neural_networks;

import java.util.ArrayList;
import java.util.Arrays;


public class Neuron {
	public ArrayList<Double> weights;
	public double bias;
	public double[] inputs;
	public double output;
	public int inputNumb;
	
	public Neuron(double bias)
	{
//		this.bias = bias;
		this.weights = new ArrayList<Double>();
	}
	
	double calculateOutput(double[] inputs)
	{
		if(inputs.length == 0)
		{
			System.out.println("Error:input array size is zero!!");
			System.exit(0);
		}
		
		this.inputs = new double[inputs.length+1];
		this.inputs[0] = 1;
		for(int i=0;i<inputs.length;++i)
		{
			this.inputs[i+1] = inputs[i];
		}
		this.output = squash(calculateTotalNetInput());
        return this.output;
	}


    double calculateTotalNetInput()
    {
        double total = 0;
        for(int i=0;i<inputs.length;++i)
        {
        	total += inputs[i] * this.weights.get(i);
        }
            
        return total; //+ bias;
    }



    double squash(double totalNetInput)
    {
    	return 1 / (1 +  Math.exp(-totalNetInput));
    }
        

    
    public double calculate_pd_error_wrt_total_net_input(double target_output)
    {
    	return calculatePdErrorWrtOutput(target_output) * calculate_pd_total_net_input_wrt_input();
    }
        

    public double calculate_error(double target_output)
    {
    	return 0.5 * (target_output - this.output) *(target_output - this.output);
    }
        

    
    double calculatePdErrorWrtOutput(double target_output)
    {
    	return -(target_output - output);
    }
        

    
    public double calculate_pd_total_net_input_wrt_input()
    {
    	return output * (1 - output);
    }
       

   
    public double calculate_pd_total_net_input_wrt_weight(int index)
    {
    	return inputs[index];
    }
        
	
	
	

}
