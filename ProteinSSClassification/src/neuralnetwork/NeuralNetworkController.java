package neuralnetwork;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NeuralNetworkController {

	// handle to our network
	private ArrayList<ArrayList<Unit>> allUnits = new ArrayList<ArrayList<Unit>>();
	
	final int aminoAcidLibrary = 20;
	final double LEARNING_RATE = .1;
	final int OUTPUT_LAYER_SIZE = 3;
	
	final double TOLERANCE = 1e-7;
	final int MAX_ITER = 4000;
			
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
	
	public ArrayList<Double> testHiddenLayer(ArrayList<Double> inputValues) {
		setInputs(inputValues);
		feedForward();
		ArrayList<Double> hiddenVals = new ArrayList<Double>();
		for(int i = 0; i < allUnits.get(1).size(); i++) {
			hiddenVals.add(allUnits.get(1).get(i).getValue());
		}
		return hiddenVals;
	}
	
	private double squaredSum(ArrayList<Double> w1, ArrayList<Double> w2) {
		double product = 0.0;
		if (w1.size() != w2.size()) {
			System.out.println("Weight vectors are different in tests!");
			System.exit(1);
		}
		for (int i = 0; i < w1.size(); i++) {
			product += Math.pow((w1.get(i) - w2.get(i)), 2);
		}
		return product;
	}
	
	private ArrayList<Double> getWeights(int layer) {
		ArrayList<Double> weights = new ArrayList<Double>();
	
		for (int j = 0; j <allUnits.get(layer).size(); j++) {
			weights.addAll(allUnits.get(layer).get(j).getWeights());
		}
		
		return weights;
	}
	
	public ArrayList<ArrayList<Unit>> autoencoderLearn(ArrayList<ArrayList<Double>> inputValues){
		// Train the system
		
		ArrayList<Double> weights = getWeights(allUnits.size() - 2);
		ArrayList<Double> weights_old = new ArrayList<Double>();
		// Initialize weights_old to be all 0's, right now.
		for (int i = 0; i < weights.size(); i++) {
			weights_old.add(0.0);
		}
		
		int iter = 0;
		while (squaredSum(weights, weights_old) > TOLERANCE && iter < MAX_ITER) {
			for(ArrayList<Double> inst : inputValues){
				setInputs(inst);
				//printInitialWeights();
				feedForward();
				backPropagateAE(inst);
				//printGraph();
			}	
			weights_old = weights;
			weights = getWeights(allUnits.size() - 2);
			iter++;
		}

		// only return the hidden unit layer
		return allUnits;
	}
	
	public ArrayList<ArrayList<Unit>> neuralNetworkLearn(ArrayList<ArrayList<Double>> inputValues, 
				ArrayList<STRUCTURE> outputs){	
		
		// Train the system
	
		ArrayList<Double> weights = getWeights(allUnits.size() - 1);
		ArrayList<Double> weights_old = new ArrayList<Double>();
		// Initialize weights_old to be all 0's, right now.
		for (int i = 0; i < weights.size(); i++) {
			weights_old.add(0.0);
		}
		
		int iter = 0;
		while (squaredSum(weights, weights_old) > TOLERANCE && iter < MAX_ITER) {
			for(int i = 0; i < outputs.size(); i++){
				setInputs(inputValues.get(i));
				feedForward();
				backPropagateNN(outputs.get(i));
			}		
			weights_old = weights;
			weights = getWeights(allUnits.size() - 1);
			iter++;
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
			allUnits.get(0).get(i).trainWeights(null, LEARNING_RATE);
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
	
	private STRUCTURE convertOutputsToStructure(){
		double highestOutput = Double.NEGATIVE_INFINITY;
		int winner = -1;
		
		for(int i = 0; i < allUnits.get(allUnits.size() - 1).size(); i++){
			Unit output = allUnits.get(allUnits.size() - 1).get(i);
			if(output.getValue() > highestOutput){
				highestOutput = output.getValue();
				winner = i;
			}
		}
		
		switch(winner){
		case 0:
			return STRUCTURE.ALPHA;
		case 1:
			return STRUCTURE.BETA;
		case 2:
			return STRUCTURE.LOOP;
		default:
			System.err.println("THERE WAS NO WINNER");
			System.exit(1);
		}
		return null;
	}
	
	public STRUCTURE classifyNeuralNetwork(ArrayList<Double> instance){
		setInputs(instance);
		feedForward();
		
		// The classification is done winner take all
		
		return convertOutputsToStructure();
	}
	
	public boolean classifyAutoencoder(ArrayList<Double> instance){
		setInputs(instance);
		feedForward();
		
		boolean inputReproduced = true;
		System.out.println("Autoencoder input->output");
		for (int i = 0; i < allUnits.get(allUnits.size() - 1).size(); i++) {
			if (allUnits.get(allUnits.size()-3).get(i).getValue() != allUnits.get(allUnits.size()-1).get(i).getValue()) {
				inputReproduced = false;
			}
			if (allUnits.get(allUnits.size()-1).get(i).getValue() > 0.1) {
				System.out.println(i + ":   " + instance.get(i) + " -> " + allUnits.get(allUnits.size()-1).get(i).getValue());
			}
		}
		System.out.println();
		// The classification is done winner take all
		return inputReproduced;
	}
	
	public enum STRUCTURE {
		ALPHA, BETA, LOOP
	}
}
