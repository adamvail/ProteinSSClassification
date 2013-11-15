package neuralnetwork;

import java.util.ArrayList;
import java.util.Collection;

import dataprocessing.ProteinDataSet;

public class Controller {

	// handle to our network
	private ArrayList<ArrayList<Unit>> allUnits = new ArrayList<ArrayList<Unit>>();
	
	private ArrayList<InputUnit> inputs;
	int windowSize;
	final int aminoAcidLibrary = 20;
	final double LEARNING_RATE = .1;

	// Input is size of each layer of units.  Ex: 17 5 3
	public Controller(ArrayList<Integer> layerSize, int windowSize){
		this.windowSize = windowSize;
		
		initializeNeuralNetwork(layerSize);
		
	}
	
	public ArrayList<ArrayList<Unit>> getNetwork(){
		return allUnits;
	}
	
	public void initializeNeuralNetwork(ArrayList<Integer> layerSize){
		// initialize the units array list
		for(int i = 0; i < layerSize.size(); i++){
			allUnits.add(new ArrayList<Unit>());
		}
		
		createInputs();
		
		for(int i = 0; i < layerSize.size(); i++) {
			for (int j = 0; j < layerSize.get(i); j++) { 
				if (i < layerSize.size() - 1) {
					allUnits.get(i).add(new Unit(false));
				}
				else {
					allUnits.get(i).add(new Unit(true));
				}
			}
		}
		
		connectGraph();
	}
	
	private void connectGraph(){
		
		// connect all the inputs. Yields initial wedge weights
		for(int i = 0; i < allUnits.size() - 1; i++){
			for(Unit u : allUnits.get(i+1)){
				u.addAllInput(allUnits.get(i));
			}
		}
		
		// set the outputs
		for(int i = allUnits.size() - 1; i > 0; i--){
			for(Unit out : allUnits.get(i)){
				for(Unit in : out.getInputs().keySet()){
					in.addOutput(out, out.getInputs().get(in));
				}
			}
		}
	}
	
	public void createInputs() {
		for(int i = 0; i < windowSize; i++){
			inputs.add(new InputUnit(aminoAcidLibrary));
		}
		
		for(InputUnit u : inputs){
			allUnits.get(0).addAll(u.getInputSubUnits());
		}
	}

	// Train network on a piece of data
	public void trainOnInstance(String sequence, STRUCTURE structure) {
		// set inputs to correct value
		setInputs(sequence);
		
		// loop through layers
		feedForward();
		backPropagate(structure);
	}
	
	private void setInputs(String sequence){
		if(sequence.length() != inputs.size()){
			System.out.println("Sequence length and number of inputs are not equal!");
			System.exit(1);
		}
		
		for(int i = 0; i < sequence.length(); i++){
			inputs.get(i).setInputUnit(sequence.charAt(i));
		}
	}
	
	private void feedForward(){
		for(int i = 1; i < allUnits.size(); i++){
			for(Unit u : allUnits.get(i)){
				u.calculateOutputValue();
			}
		}
	}
	
	private void backPropagate(STRUCTURE structure){
	
		if(allUnits.get(allUnits.size() - 1).size() != 3){
			System.out.println("Don't have three output units!");
			System.exit(1);
		}
		
		double[] struct = convertStructure(structure);
		
		// handle output units
		for(int i = 0; i < allUnits.get(allUnits.size() - 1).size(); i++){
			allUnits.get(allUnits.size() - 1).get(i).trainWeights(struct[i], LEARNING_RATE);
		}
		
		// handle all hidden units
		for(int i = allUnits.size() - 2; i > 0; i++){
			for(Unit u : allUnits.get(i)){
				u.trainWeights(null, LEARNING_RATE);
			}
		}
	}
	
	private double[] convertStructure(STRUCTURE structure){
		double[] struct = new double[3];
		if(structure.equals(STRUCTURE.ALPHA)){		
			struct[0] = 1;
			struct[1] = 0;
			struct[2] = 0;			
		}
		else if(structure.equals(STRUCTURE.BETA)){		
			struct[0] = 0;
			struct[1] = 1;
			struct[2] = 0;			
		}
		else if(structure.equals(STRUCTURE.LOOP)){		
			struct[0] = 0;
			struct[1] = 0;
			struct[2] = 1;			
		}
		else {
			System.out.println("ERROR IN STRUCTURE!");
			System.exit(1);
		}
		return struct;
	}
	
	public enum STRUCTURE {
		ALPHA, BETA, LOOP
	}
}
