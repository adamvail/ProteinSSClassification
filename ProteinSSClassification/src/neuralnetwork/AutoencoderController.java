package neuralnetwork;

import java.util.ArrayList;
import java.util.Collection;

import neuralnetwork.NeuralNetworkController.STRUCTURE;
import dataprocessing.Protein;
import dataprocessing.ProteinDataSet;

public class AutoencoderController {

	ProteinDataSet data;
	public ArrayList<ArrayList<Double>> processedData;
	ArrayList<STRUCTURE> structures;
	public ArrayList<ArrayList<Unit>> network = new ArrayList<ArrayList<Unit>>();
	NeuralNetworkController mostRecentLayer;
	final int windowSize = 13;
	final int NUM_AMINO_ACIDS = 20;
	
	public NeuralNetworkController getMostRecentLayer() {
		return mostRecentLayer;
	}
	
	public ArrayList<ArrayList<Double>> getProcessedData() {
		return processedData;
	}
	
	public AutoencoderController(ProteinDataSet data){
		this.data = data;
	}
	
	private void initializeInputs(){
		processedData = new ArrayList<ArrayList<Double>>();
		structures = new ArrayList<STRUCTURE>();
		for (Protein protein : data.getTrain()) {
			processedData.addAll(convertProteinToDoubles(protein));
			structures.addAll(convertProteinStructure(protein));
		}
	}
	
	private ArrayList<STRUCTURE> convertProteinStructure(Protein protein) {
		String structure = protein.getSecondaryStructure();
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
	
	private void clearOutputConnections(ArrayList<Unit> layer){		
		for (Unit u : layer) {
			u.getOutputs().clear();
		}
	}
	
	public void learnInitialLayer(int hiddenLayerSize){
		ArrayList<Unit> inputLayer = new ArrayList<Unit>();
		for (int i = 0; i < windowSize * 20; i++) {
			inputLayer.add(new Unit(false));
		}
		initializeInputs();
		NeuralNetworkController layerController = new NeuralNetworkController(inputLayer, hiddenLayerSize);
	
		ArrayList<ArrayList<Unit>> autoEncoder = layerController.autoencoderLearn(processedData);
		mostRecentLayer = layerController;
		
		network.add(autoEncoder.get(0));
		network.add(autoEncoder.get(1));
		clearOutputConnections(network.get(1));
	}
	
	private void feedForward(ArrayList<Double> datum) {
		for(int i = 0; i < datum.size(); i++) {
			network.get(0).get(i).setValue(datum.get(i));
		}
		for(int i = 1; i < network.size(); i++){
			for(Unit u : network.get(i)){
				u.calculateOutputValue();
			}
		}
	}
	
	public ArrayList<ArrayList<Double>> feedDataThroughNetwork() {	
		// Process the data through the network so far
		ArrayList<ArrayList<Double>> newData = new ArrayList<ArrayList<Double>>();
		for (ArrayList<Double> datum : processedData) {
			feedForward(datum);
			ArrayList<Double> newValues = new ArrayList<Double>();
			for (Unit u : network.get(network.size() - 1)) {
				newValues.add(u.getValue());
			}
			newData.add(newValues);
		}
		return newData;
	}
	
	public void learnHiddenLayer(int hiddenLayerSize) {
		NeuralNetworkController layerController = new 
				NeuralNetworkController(network.get(network.size() - 1), hiddenLayerSize);
		
		ArrayList<ArrayList<Double>> newData = feedDataThroughNetwork();
			
		ArrayList<ArrayList<Unit>> autoEncoder = layerController.autoencoderLearn(newData);
		//autoEncoder = layerController.autoencoderLearn(newData);
		mostRecentLayer = layerController;
		clearOutputConnections(autoEncoder.get(1));
		network.add(autoEncoder.get(1));	
	}
	
	public void learnOutputLayer() {
		NeuralNetworkController layerController = new 
				NeuralNetworkController(network.get(network.size() - 1));
		
		ArrayList<ArrayList<Double>> newData = feedDataThroughNetwork();
		
		ArrayList<ArrayList<Unit>> neuralNet = layerController.neuralNetworkLearn(newData, structures);
		mostRecentLayer = layerController;
		network.add(neuralNet.get(1));	
		
	}
	
	private STRUCTURE convertOutputsToStructure(){
		double highestOutput = Double.NEGATIVE_INFINITY;
		int winner = -1;
		
		for(int i = 0; i < network.get(network.size() - 1).size(); i++){
			Unit output = network.get(network.size() - 1).get(i);
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
	
	private void initializeTestData(ArrayList<ArrayList<Double>> procData, 
			ArrayList<STRUCTURE> structs) {
		for (Protein protein : data.getTrain()) {
			procData.addAll(convertProteinToDoubles(protein));
			structs.addAll(convertProteinStructure(protein));
		}
	}
	
	public void runTestSet() {
		ArrayList<ArrayList<Double>> procData = new ArrayList<ArrayList<Double>>();
		ArrayList<STRUCTURE> structs = new ArrayList<STRUCTURE>();
		
		initializeTestData(procData, structs);
		double correct = 0;
		double incorrect = 0;
		for(int i = 0; i < procData.size(); i++){
			STRUCTURE prediction = classifyDeepNetwork(procData.get(i));
			if(prediction == structs.get(i)){
				correct++;
				//System.out.println("CORRECT");
			}
			else {
				incorrect++;
			}
		}
		System.out.println("Percentage correct: " + (correct / procData.size()) * 100);
	}
	
	
	private double getAngle(Object[] a, Object[] b) {
		// Assume these are same size
		double lengthSquaredA = 0;
		double lengthSquaredB = 0;
		double dotProduct = 0;
		for (int i = 0; i < a.length; i++) {
			lengthSquaredA += (Double)a[i] * (Double)a[i];
			lengthSquaredB += (Double)b[i] * (Double)b[i];
			dotProduct += (Double)a[i]*(Double)b[i];
		}
		return Math.acos(dotProduct / Math.sqrt(lengthSquaredA * lengthSquaredB));
	}
	
	// These should be basically orthogonal.  If not, we've got problems!
	public void printOrthogonalityOfWeights(int layer) {
		if (layer <= 0 || layer >= network.size()){
			System.out.println("Invalid layer");			
		}
		System.out.println("cos(theta) between input weight vectors between unit _ and _ in layer " + layer);
		for (int i = 0; i < network.get(layer).size(); i++) {
			for (int j = i+1; j < network.get(layer).size(); j++) {
				System.out.println("   " + i + " and " + j + ":  " + getAngle(network.get(layer).get(i).getWeights().toArray(),
						network.get(layer).get(j).getWeights().toArray()));
			}
		}		
	}
	
	// These should be basically orthogonal.  If not, we've got problems!
	public void printOrthogonalityOfHiddenWeightsToSelectedInputUnits(Integer[] inputUnits) {
		int layer = 1;
		System.out.println("cos(theta) between input weight vectors between unit _ and _ in layer " + layer);
		for (int i = 0; i < network.get(layer).size(); i++) {
			Object[] aRaw = network.get(layer).get(i).getWeights().toArray();
			Double[] a = new Double[inputUnits.length];
			for (int k = 0; k < inputUnits.length; k++) {
				a[k] = (Double)aRaw[inputUnits[k]];				
			}
			for (int j = i+1; j < network.get(layer).size(); j++) {
				Object[] bRaw = network.get(layer).get(j).getWeights().toArray();
				Double[] b = new Double[inputUnits.length];
				for (int k = 0; k < inputUnits.length; k++) {
					b[k] = (Double)bRaw[inputUnits[k]];				
				}
				System.out.println("   " + i + " and " + j + ":  " + getAngle(a,b));
			}
		}		
	}
	
	
	public void testAllTrainingData() {
		double correct = 0;
		double incorrect = 0;
		System.out.println("\nClassify " + processedData.size() + " instances");
		for(int i = 0; i < processedData.size(); i++){
			STRUCTURE prediction = classifyDeepNetwork(processedData.get(i));
			System.out.println("Prediction: " + prediction.toString() + "  Actual: " + structures.get(i).toString());
			for (int j = 0; j < network.get(network.size() - 1).size(); j++) {
				System.out.println("   node " + j + " value: " + network.get(network.size() - 1).get(j).getValue());
			}
			System.out.println();
			if(prediction == structures.get(i)){
				correct++;
				//System.out.println("CORRECT");
			}
			else {
				incorrect++;
			}
		}
		System.out.println("Percentage correct: " + (correct / processedData.size()) * 100);
	}
	
	private STRUCTURE classifyDeepNetwork(ArrayList<Double> instance){
		feedForward(instance);
		
		// The classification is done winner take all
		
		return convertOutputsToStructure();
	}
	
	private ArrayList<Double> getWeights(int layer) {
		ArrayList<Double> weights = new ArrayList<Double>();
	
		for (int j = 0; j <network.get(layer).size(); j++) {
			for(int i = 0; i < network.get(layer - 1).size(); i++) {
				weights.add(network.get(layer).get(j).getWeight(network.get(layer - 1).get(i)));
			}
		}
		
		return weights;
	}
	
	public ArrayList<ArrayList<Double>> getAllWeights() {
		ArrayList<ArrayList<Double>> allWeights = new ArrayList<ArrayList<Double>>();
		for (int i = 1; i < network.size(); i++) {
			allWeights.add(getWeights(i));		
		}
		return allWeights;
	}
	
	public void printWeights() {
		ArrayList<ArrayList<Double>> recentlyTrainedNetwork = mostRecentLayer.getAllWeights();
		
		ArrayList<ArrayList<Double>> finalNetwork = getAllWeights();
		
		// Print the weights
		System.out.println("\nWEIGHTS of SMALL network trained for layer " + (network.size() - 1));
		for(int i = 0; i < recentlyTrainedNetwork.size(); i++) {
			System.out.print("  Layer " + i + ": [");
			for (int j = 0; j < recentlyTrainedNetwork.get(i).size(); j++) {
				System.out.print(recentlyTrainedNetwork.get(i).get(j) + ", ");
			}
			System.out.print("]\n");
		}
		// Print the weights
		System.out.println("WEIGHTS of FULL trained network so far " + (network.size() - 1));
		for(int i = 0; i < finalNetwork.size(); i++) {
			System.out.print("  Layer " + i + ": [");
			for (int j = 0; j < finalNetwork.get(i).size(); j++) {
				System.out.print(finalNetwork.get(i).get(j) + ", ");
			}
			System.out.print("]\n");
		}
	}
		
}
