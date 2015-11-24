package neural_networks;

import java.util.ArrayList;


public class Neuron {
	public ArrayList<Double> weights;
	public double bias;
	public double[] inputs;
	public double output;
	public int inputNumb;
	
	public Neuron(double bias)
	{
		this.bias = bias;
		ArrayList<Double> weights = new ArrayList<Double>();
	}
	
	double calculateOutput(double[] inputs)
	{
		this.inputs = inputs;
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
            
        return total + bias;
    }



    double squash(double totalNetInput)
    {
    	return 1 / (1 +  Math.exp(-totalNetInput));
    }
        

    
    public double calculate_pd_error_wrt_total_net_input(double target_output)
    {
    	return calculatePdErrorWrtOutput(target_output) * calculatePdPotalNetInputWrtInput();
    }
        

    double calculate_error(double target_output)
    {
    	return 0.5 * (target_output - this.output) *(target_output - this.output);
    }
        

    
    double calculatePdErrorWrtOutput(double target_output)
    {
    	return -(target_output - output);
    }
        

    
    double calculatePdPotalNetInputWrtInput()
    {
    	return output * (1 - output);
    }
       

   
    double calculatePdTotalNetInputWrtWeight(int index)
    {
    	return inputs[index];
    }
        
	
	
	

}
