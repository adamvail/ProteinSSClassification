package neuralnetwork;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class Unit {

	private HashMap<Unit, Double> inputs;
	double value;
	private Vector<Unit> outputs;
	
	public Unit(){
		inputs = new HashMap<Unit, Double>();
		outputs = new Vector<Unit>();
	}
	
	public Unit(Collection<Unit> inputs){
		addAllInput(inputs);
	}
	
	public void calculateValue(){
		double inputSum = 0;
		for(Unit in : inputs.keySet()){
			inputSum += in.getValue() * inputs.get(in);
		}
		value = sigmoid(inputSum);
	}
	
	private double sigmoid(double inputSum){
		return 1 / (1 + Math.exp(-1 * inputSum));
	}
	
	public void addInput(Unit input, double weight){
		inputs.put(input, weight);
	}
	
	public void addInput(Unit input){
		// get a random double between -1 and 1
		double randomWeight = (Math.random() * 2) - 1;
		this.inputs.put(input, randomWeight);
	}
	
	public void addAllInput(Collection<Unit> inputs){
		for(Unit u : inputs){
			// get a random double between -1 and 1
			double randomWeight = (Math.random() * 2) - 1;
			this.inputs.put(u, randomWeight);
		}
	}
	
	public HashMap<Unit, Double> getInputs(){
		return inputs;
	}
	
	public Vector<Unit> getOutputs(){
		return outputs;
	}
	
	public double getValue(){
		return value;
	}
	
}
