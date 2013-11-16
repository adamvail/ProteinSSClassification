package neuralnetwork;

import java.util.ArrayList;

import neuralnetwork.NeuralNetworkController.STRUCTURE;

import dataprocessing.Protein;
import dataprocessing.ProteinDataSet;

public class AutoencoderController {

	ProteinDataSet data;
	ArrayList<ArrayList<Double>> processedData;
	ArrayList<STRUCTURE> structures;
	ArrayList<ArrayList<Unit>> network = new ArrayList<ArrayList<Unit>>();
	final int windowSize = 13;
	
	public AutoencoderController(ProteinDataSet data){
		this.data = data;
	}
	
	public ArrayList<ArrayList<Double>> initializeInputs(){
		ArrayList<ArrayList<Double>> processedData = new ArrayList<ArrayList<Double>>();
		for (Protein protein : data.getTrain()) {
			processedData.addAll(convertProteinToDoubles(protein));
			structures.addAll(convertProteinStructure(protein));
		}
		return processedData;
	}
	
	ArrayList<STRUCTURE> convertProteinStructure(Protein protein) {
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
			case '_':
				structures.add(STRUCTURE.LOOP);
				break;	
			default:
				System.out.println("Error in structure data.  Contained character " + s);
				System.exit(1);
			}
		}
		return structures;		
	}
	
	/**
	 * Create an arraylist of arraylists where each element is
	 * a list of doubles representing an amino acid sequence of
	 * one of the windows across the entire protein
	 * 
	 * @param protein - protein to chunk using the sliding window
	 * @return
	 */
	ArrayList<ArrayList<Double>> convertProteinToDoubles(Protein protein) {
		
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
					target = ((int)aminoAcid) - 65;
				}
				ArrayList<Double> subUnits = new ArrayList<Double>();
				for(int k = 0; k < subUnits.size(); k++){
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
	
	public void clearOutputConnections(ArrayList<Unit> layer){		
		for (Unit u : layer) {
			u.getOutputs().clear();
		}
	}
	
	public void learnInitialLayer(int hiddenLayerSize){
		ArrayList<Unit> inputLayer = new ArrayList<Unit>();
		for (int i = 0; i < windowSize * 20; i++) {
			inputLayer.add(new Unit(false));
		}
		NeuralNetworkController layerController = new NeuralNetworkController(inputLayer, hiddenLayerSize);
		processedData = initializeInputs();
		
		ArrayList<ArrayList<Unit>> autoEncoder = layerController.autoencoderLearn(processedData);
		
		network.add(autoEncoder.get(0));
		network.add(autoEncoder.get(1));
		clearOutputConnections(network.get(1));
	}
	
	public void feedForward(ArrayList<Double> datum) {
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
		clearOutputConnections(autoEncoder.get(1));
		network.add(autoEncoder.get(1));	
	}
	
	public void learnOuterLayer() {
		NeuralNetworkController layerController = new 
				NeuralNetworkController(network.get(network.size() - 1));
		
		ArrayList<ArrayList<Double>> newData = feedDataThroughNetwork();
		
		ArrayList<ArrayList<Unit>> neuralNet = layerController.neuralNetworkLearn(newData, structures);
		network.add(neuralNet.get(1));	
		
	}
	
}
