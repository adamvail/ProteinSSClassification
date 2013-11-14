package neuralnetwork;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class Unit {

	private HashMap<Unit, Double> inputs;
	double value;
	private HashMap<Unit, Double> outputs;
	private boolean outputUnit = false;
	private double error;
	
	public Unit(boolean outputUnit){
		inputs = new HashMap<Unit, Double>();
		outputs = new HashMap<Unit, Double>();
		this.outputUnit = outputUnit;
	}

	// Use this constructor for output units
	public Unit(Collection<Unit> inputs, boolean outputUnit){
		addAllInput(inputs);
		this.outputUnit = outputUnit;
	}
	
	// Use this constructor for hidden units
	public Unit(Collection<Unit> inputs, Collection<Unit> outputs, boolean outputUnit){
		addAllInput(inputs);
		addAllOutput(outputs);
		this.outputUnit = outputUnit;
	}
	
	public void calculateOutputValue(){
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
	
	public void addOutput(Unit output, double weight){
		outputs.put(output, weight);
	}
	
	public void addOutput(Unit output){
		// get a random double between -1 and 1
		double randomWeight = (Math.random() * 2) - 1;
		this.outputs.put(output, randomWeight);
	}
	
	public void addAllOutput(Collection<Unit> outputs){
		for(Unit u : outputs){
			// get a random double between -1 and 1
			double randomWeight = (Math.random() * 2) - 1;
			this.outputs.put(u, randomWeight);
		}
	}
	
	public void setValue(double value){
		this.value = value;
	}
	
	public HashMap<Unit, Double> getInputs(){
		return inputs;
	}
	
	public HashMap<Unit, Double> getOutputs(){
		return outputs;
	}
	
	public double getValue(){
		return value;
	}
	
	public double getError() {
		return error;
	}
	
	public double getWeight(Unit u){
		return inputs.get(u);
	}
	
	private void calculateOutputUnitError(double y){
		error = (y - value) * value * (1 - value);
	}
	
	private void calculateHiddenUnitError(){
		double outputError = 0;
		for(Unit u : outputs.keySet()){
			outputError += u.getError() * outputs.get(u);
			outputs.put(u, u.getWeight(this));
		}
		
		error = value * (1 - value) * outputError;
	}
	
	private void changeWeights(double learningRate) {
		for(Unit u : inputs.keySet()){
			double weight = inputs.get(u) + (error * u.getValue() * learningRate);
			inputs.put(u, weight);
		}
	}
	
	public void trainWeights(double y, double learningRate){
		if(outputUnit){
			// train weights as an output unit
			calculateOutputUnitError(y);
		}
		else {
			// train weights as a hidden unit
			calculateHiddenUnitError();
		}
		
		changeWeights(learningRate);
	}
}
