package neural_networks;

import java.util.ArrayList;


public class Neuron {
	double[] weights ;
	double bias;
	double[] inputs;
	double output;
	int inputNumb;
	
	public Neuron(int inputNumb)
	{
		this.inputNumb = inputNumb;
		double[] weights = new double[inputNumb];
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
        	total += inputs[i] * this.weights[i];
        }
            
        return total + bias;
    }



    double squash(double totalNetInput)
    {
    	return 1 / (1 +  Math.exp(-totalNetInput));
    }
        

    
    double calculatePdErrorWrtTotalNetInput(double target_output)
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
