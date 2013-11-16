package neuralnetwork;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NeuralNetworkController {

	// handle to our network
	private ArrayList<ArrayList<Unit>> allUnits = new ArrayList<ArrayList<Unit>>();
	
	final int aminoAcidLibrary = 20;
	final double LEARNING_RATE = .1;
	final int OUTPUT_LAYER_SIZE = 3;

	// Input is size of each layer of units.  Ex: 17 5 3
	public NeuralNetworkController(ArrayList<Unit> inputLayer, int hiddenLayerSize){	
		allUnits.add(inputLayer);
		createLayer(hiddenLayerSize, false);
		createLayer(inputLayer.size(), true);
		connectGraph();
	}
	
	public NeuralNetworkController(ArrayList<Unit> inputLayer){
		allUnits.add(inputLayer);
		createLayer(OUTPUT_LAYER_SIZE, true);
		connectGraph();
	}
	
	public ArrayList<ArrayList<Unit>> autoencoderLearn(ArrayList<ArrayList<Double>> inputValues){
		for(ArrayList<Double> inst : inputValues){
			setInputs(inst);
			printInitialWeights();
			feedForward();
			backPropagateAE(inst);
			printGraph();
		}
		
		// only return the hidden unit layer
		return allUnits;
	}
	
	public ArrayList<ArrayList<Unit>> neuralNetworkLearn(ArrayList<ArrayList<Double>> inputValues, 
				ArrayList<STRUCTURE> outputs){	
		
		for(int i = 0; i < outputs.size(); i++){
			setInputs(inputValues.get(i));
			feedForward();
			backPropagateNN(outputs.get(i));
		}
		
		// only return the hidden unit layer
		return allUnits;
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
	
	public void createLayer(int size, boolean outputs){
		allUnits.add(new ArrayList<Unit>());
		for(int i = 0; i < size; i++){
			// Get this most recent layer that was just added
			allUnits.get(allUnits.size() - 1).add(new Unit(outputs));
		}
	}
	
	public void setInputs(ArrayList<Double> inputValues) {		
		for(int i = 0; i < allUnits.get(0).size(); i++){
			allUnits.get(0).get(i).setValue(inputValues.get(i));
		}		
	}
	
	private void feedForward(){
		for(int i = 1; i < allUnits.size(); i++){
			for(Unit u : allUnits.get(i)){
				u.calculateOutputValue();
			}
		}
	}
	
	private void backPropagateNN(STRUCTURE structure){
	
		if(allUnits.get(allUnits.size() - 1).size() != 3){
			System.out.println("Don't have three output units!");
			System.exit(1);
		}
		
		double[] struct = convertStructure(structure);
		
		// handle output units
		for(int i = 0; i < allUnits.get(allUnits.size() - 1).size(); i++){
			allUnits.get(allUnits.size() - 1).get(i).trainWeights(struct[i], LEARNING_RATE);
		}
		
		for(int i = 0; i < allUnits.get(0).size(); i++){
			allUnits.get(0).get(i).trainWeights(struct[i], LEARNING_RATE);
		}
	}
	
	private void backPropagateAE(ArrayList<Double> outputs){
		// handle output units
		for(int i = 0; i < allUnits.get(allUnits.size() - 1).size(); i++){
			allUnits.get(allUnits.size() - 1).get(i).trainWeights(outputs.get(i), LEARNING_RATE);
		}
		
		// handle last layer of hidden units in the autoencoder
		// we can't use back propagation for more than three levels
		for(Unit u : allUnits.get(allUnits.size() - 2)){
			u.trainWeights(null, LEARNING_RATE);
		}
		
		// Doing this for input units only because we need consistent
		// weights for output units.
		for(Unit u : allUnits.get(0)){
			u.trainWeights(null, LEARNING_RATE);
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
	
	public void printInitialWeights(){
		DecimalFormat format = new DecimalFormat("0.0000");
		for(int i = 0; i < allUnits.get(0).size(); i++){
			Unit input = allUnits.get(0).get(i);
			for(int k = 0; k < allUnits.get(1).size(); k++){
				Unit hidden = allUnits.get(1).get(k);
				double weight = hidden.getInputs().get(input);
				System.out.println("I" + i + " -> H" + k + " : " + format.format(weight));
			}
			System.out.println();
		}
		
		for(int i = 0; i < allUnits.get(1).size(); i++){
			Unit input = allUnits.get(1).get(i);
			for(int k = 0; k < allUnits.get(2).size(); k++){
				Unit hidden = allUnits.get(2).get(k);
				double weight = hidden.getInputs().get(input);
				System.out.println("H" + i + " -> O" + k + " : " + format.format(weight));
			}
			System.out.println();
		}
		
		System.out.println("\n\n");
	}
	
	/**
	 * Print the resultant graph on a per input/output basis for clarity
	 */
	public void printGraph(){
		DecimalFormat format = new DecimalFormat("0.0000");
		System.out.println("Printing inputs");
		// Get the input unit
		for(Unit input : allUnits.get(0)){
			System.out.println(format.format(input.getValue()));
			for(Unit hidden : allUnits.get(1)){
				System.out.println("        Input w: " + format.format(hidden.getInputs().get(input)) + " -> Hidden v: " + format.format(hidden.getValue()));
				System.out.println();
			}
			System.out.println("\n");
		}
		
		System.out.println("Printing outputs");
		
		// Do the same for output units
		for(int k = 0; k < allUnits.get(2).size(); k++){
			Unit output = allUnits.get(2).get(k);
			System.out.println("                    O" + k + ": " + format.format(output.getValue()));
			for(int i = 0; i < allUnits.get(1).size(); i++){
				Unit hidden = allUnits.get(1).get(i);
				System.out.println("H" + i + " " + format.format(hidden.getValue()) + " -> w: " + format.format(output.getInputs().get(hidden)));
			}
			System.out.println();
		}		
	}
	
	public enum STRUCTURE {
		ALPHA, BETA, LOOP
	}
}
