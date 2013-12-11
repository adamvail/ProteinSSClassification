package baseline;

import java.text.DecimalFormat;
import java.util.ArrayList;

import neuralnetwork.Unit;
import dataprocessing.Protein;
import dataprocessing.ProteinDataSet;

public class BaselineNeuralNetwork {

	ProteinDataSet data;
	
	// handle to our network
	private ArrayList<ArrayList<Unit>> allUnits = new ArrayList<ArrayList<Unit>>();
	
	ArrayList<ArrayList<Double>> processedData;
	ArrayList<STRUCTURE> structures;
	
	final int aminoAcidLibrary = 20;
	final double LEARNING_RATE = .1;
	final int OUTPUT_LAYER_SIZE = 3;
	
	final double TOLERANCE = 1e-7;
	final int MAX_ITER = 500;
	
	int windowSize = 13;
	final int NUM_AMINO_ACIDS = 20;
	int NUM_HIDDEN_UNITS = 75;
	
	
	public BaselineNeuralNetwork(ProteinDataSet data) {
		this.data = data;
		runNN();
	}
	
	private void runNN() {
		// create a layer of input units that are 
		// the window size * the number of possible amino
		// acids.
		createLayer(windowSize * NUM_AMINO_ACIDS, false);
		
		// create a layer of hidden units of desired size.
		createLayer(NUM_HIDDEN_UNITS, false);
		
		// create the layer of output units
		createLayer(3, true);
		
		// connect all units in the graph, randomly assigning
		// starting weights
		connectGraph();
		
		// Initialize the 
		initializeInputs();
		
		neuralNetworkLearn(processedData, structures);
		
		// Print out the accuracy of this neural network
		classifyNeuralNetwork();
	}
	
	private void initializeInputs(){
		processedData = new ArrayList<ArrayList<Double>>();
		structures = new ArrayList<STRUCTURE>();
		for (Protein protein : data.getTrain()) {
			processedData.addAll(convertProteinToDoubles(protein));
			structures.addAll(convertProteinStructure(protein));
		}
	}
	
	private int convertAminoAcidToDouble(char c) {	
		switch (c) {
		case 'A': return 0;
		case 'R': return 1;
		case 'N': return 2;
		case 'D': return 3;
		case 'C': return 4;
		case 'Q': return 5;
		case 'E': return 6;
		case 'G': return 7;
		case 'H': return 8;
		case 'I': return 9;
		case 'L': return 10;
		case 'K': return 11;
		case 'M': return 12;
		case 'F': return 13;
		case 'P': return 14;
		case 'S': return 15;
		case 'T': return 16;
		case 'W': return 17;
		case 'Y': return 18;
		case 'V': return 19;	
		default: return -1;
		}
		
	}
	
	/**
	 * Create an arraylist of arraylists where each element is
	 * a list of doubles representing an amino acid sequence of
	 * one of the windows across the entire protein
	 * 
	 * @param protein - protein to chunk using the sliding window
	 * @return
	 */
	private ArrayList<ArrayList<Double>> convertProteinToDoubles(Protein protein) {
		
		int halfWindow = windowSize / 2;
		
		String sequence = protein.getSequence();
		// add spacing so that we can treat the first and last
		// amino acids as the center of a residue
		sequence = "      " + sequence + "      ";
		ArrayList<ArrayList<Double>> inputs = new ArrayList<ArrayList<Double>>();
		for (int i = halfWindow; i < sequence.length() - halfWindow; i++) {
			int startIndex = i - halfWindow;
			int endIndex = i + halfWindow;
			
			String aminoAcidWindow = sequence.substring(startIndex, endIndex + 1);
			ArrayList<Double> sequenceForInput = new ArrayList<Double>();
			
			for(char aminoAcid : aminoAcidWindow.toCharArray()){
				int target;
				if (aminoAcid == ' '){
					target = -1;
				}
				else {
					target = convertAminoAcidToDouble(aminoAcid);
				}
				ArrayList<Double> subUnits = new ArrayList<Double>();
				for(int k = 0; k < NUM_AMINO_ACIDS; k++){
					if(k == target){
						subUnits.add(1.0);
					}
					else {
						subUnits.add(0.0);
					}
				}
				sequenceForInput.addAll(subUnits);
			}
			inputs.add(sequenceForInput);
		}
		return inputs;
	}
	
	private ArrayList<STRUCTURE> convertProteinStructure(Protein protein) {
		String structure = protein.getSecondaryStructure();
		//System.out.println(structure);
		ArrayList<STRUCTURE> structures = new ArrayList<STRUCTURE>();
		for (char s : structure.toCharArray()) {
			switch(s) {
			case 'h': 
				structures.add(STRUCTURE.ALPHA);
				break;
			case 'e':
				structures.add(STRUCTURE.BETA);
				break;
			case '-':
				structures.add(STRUCTURE.LOOP);
				break;	
			default:
				System.out.println("Error in structure data.  Contained character " + s);
				System.exit(1);
			}
		}
		return structures;		
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
	
	public ArrayList<ArrayList<Unit>> neuralNetworkLearn(ArrayList<ArrayList<Double>> inputValues, 
				ArrayList<STRUCTURE> outputs){	
		
		// Train the system
	
		ArrayList<Double> weights = getWeights(allUnits.size() - 1);
		ArrayList<Double> weights_old = new ArrayList<Double>();
		// Initialize weights_old to be all 0's, right now.
		for (int i = 0; i < weights.size(); i++) {
			weights_old.add(0.0);
		}
		
		System.out.println(outputs.size());
		long curTime = System.currentTimeMillis();
		
		int iter = 0;
		while (squaredSum(weights, weights_old) > TOLERANCE && iter < MAX_ITER) {
			for(int i = 0; i < outputs.size(); i++){
			//for(int i = 0; i < 1000; i++){
				setInputs(inputValues.get(i));
				feedForward();
				backPropagateNN(outputs.get(i));
			}
			weights_old = weights;
			weights = getWeights(allUnits.size() - 1);
			iter++;
		}
		System.out.println(iter);
		System.out.println((double)(System.currentTimeMillis() - curTime) / 1000);
		return allUnits;
	}
	
	private void printWeights(ArrayList<Double> weights) {
		for(int i = 0; i < weights.size(); i++) {
			System.out.print(weights.get(i) + " ");
		}
		System.out.println();
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
		
		// handle hidden layer
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
	
	public void classifyNeuralNetwork(){
		double correct = 0.0;
		
		ArrayList<ArrayList<Double>> processedTestData = new ArrayList<ArrayList<Double>>();
		ArrayList<STRUCTURE> testStructures = new ArrayList<STRUCTURE>();
		
		for (Protein protein : data.getTest()) {
			processedTestData.addAll(convertProteinToDoubles(protein));
			testStructures.addAll(convertProteinStructure(protein));
		}
		
		for(int i = 0; i < processedTestData.size(); i++) {
			setInputs(processedTestData.get(i));
			feedForward();
			STRUCTURE output = convertOutputsToStructure();
			if(output == testStructures.get(i)) {
				correct++;
			}
		}
		
		// The classification is done winner take all
		System.out.println("Accuracy: " + (correct / processedTestData.size()) * 100);		
	}
	
	public enum STRUCTURE {
		ALPHA, BETA, LOOP
	}
}
