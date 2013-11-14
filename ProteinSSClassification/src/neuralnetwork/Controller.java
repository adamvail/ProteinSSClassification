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
		
		// connect all the inputs.
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
		// loop through layers
		if (x.size() != inputs.size()) {
			// Error
			System.out.println("Error in training instances");
			return;
		}
		// Set the values of each element of the network, starting with first
		// layer of network and working forward
		for (int i = 0; i < x.size(); i++) {
			inputs.get(i).setValue(x.get(i));
		}

		// Train weights, starting with back row of network and working backward.
		trainWeights(y,learningRate);
	}

	private void setNetworkValues() {
		// Initialize Input layer
		for (int i = 0; i < x.size(); i++) {
			allUnits.get(0).get(i).setValue(x.get(i));
		}
		// Initialize other layers
		for (Collection<Unit> layer : allUnits) {
			for (Unit unit : layer) {
			}
		}

	}
	
	public enum STRUCTURE {
		ALPHA, BETA, LOOP
	}
}
