package ensemble;

import java.util.ArrayList;

import baseline.BaselineNeuralNetwork;
import baseline.BaselineNeuralNetwork.STRUCTURE;
import dataprocessing.CrossValidation;
import dataprocessing.Protein;
import dataprocessing.ProteinDataSet;

public class NeuralNetEnsemble {
	
	String trainName;
	String testName;
	int hiddenLayerSize;
	int windowSize;
	int ensembleSize = 3;
	ArrayList<ProteinDataSet> data = new ArrayList<ProteinDataSet>();
	ArrayList<BaselineNeuralNetwork> networks = new ArrayList<BaselineNeuralNetwork>();
	ArrayList<Protein> testSet;
	
	public NeuralNetEnsemble(String trainName, String testName, int hiddenLayerSize, int windowSize, int ensembleSize) {
		this.trainName = trainName;
		this.testName = testName;
		this.hiddenLayerSize = hiddenLayerSize;
		this.windowSize = windowSize;
		this.ensembleSize = ensembleSize;
		testSet = CrossValidation.readData(testName);
		
		bagProteins();
		createNeuralNetworks();
		
	}
	
	public void classifyTestInstances() {
		double correct = 0;
		double total = 0;
		for(Protein protein : testSet) {
			ArrayList<ArrayList<Double>> processedTestData = new ArrayList<ArrayList<Double>>();
			ArrayList<STRUCTURE> testStructures = new ArrayList<STRUCTURE>();
			processedTestData.addAll(networks.get(0).convertProteinToDoubles(protein));
			testStructures.addAll(networks.get(0).convertProteinStructure(protein));
			
			for(int i = 0; i < processedTestData.size(); i++) {
				ArrayList<STRUCTURE> outputs = new ArrayList<STRUCTURE>();
				for(BaselineNeuralNetwork n : networks) {
					outputs.add(n.classifySingleInstance(processedTestData.get(i)));
				}
				// Determine who was the winner
				STRUCTURE winner = findWinner(outputs);
				System.out.println("Prediction: " + winner + " Actual: " + testStructures.get(i));
				total++;
				if(testStructures.get(i) == winner) {
					correct++;
				}
			}
		}
		System.out.println("Accuracy: " + (correct / total) * 100);
	}
	
	public STRUCTURE findWinner(ArrayList<STRUCTURE> outputs) {
		int alpha = 0;
		int beta = 0;
		int loop = 0;
		for(STRUCTURE s : outputs) {
			if(s == STRUCTURE.ALPHA) {
				alpha++;
			}
			else if(s == STRUCTURE.BETA) {
				beta++;
			}
			else if(s == STRUCTURE.LOOP) {
				loop++;
			}
		}
		
		if(alpha > beta && alpha > loop) {
			return STRUCTURE.ALPHA;
		}
		else if (beta > alpha && beta > loop) {
			return STRUCTURE.BETA;
		}
		else if(loop > alpha && loop > beta) {
			return STRUCTURE.LOOP;
		}
		else if( alpha == loop && alpha == beta) {
			// All three are the same, loop is most common so return that
			return STRUCTURE.LOOP;
		}
		else if (alpha == beta) {
			return STRUCTURE.ALPHA;
		}
		else if(alpha == loop) {
			return STRUCTURE.LOOP;
		}
		else if(beta == loop) {
			return STRUCTURE.LOOP;
		}
		else {
			// default to loop
			return STRUCTURE.LOOP;
		}
	}
	
	public void createNeuralNetworks() {
		for(int i = 0; i < data.size(); i++) {
			// Create a neural net for this data
			BaselineNeuralNetwork net = new BaselineNeuralNetwork(data.get(i), null, hiddenLayerSize, windowSize);
			networks.add(net);
		}
	}
	
	public void bagProteins() {
		ArrayList<Protein> allData = CrossValidation.readData(trainName);
		for(int i = 0; i < ensembleSize; i++) {
			ProteinDataSet baggedData = new ProteinDataSet();
			// Add training data without replacement
			while(baggedData.getTrain().size() < allData.size()) {
				// Get a random integer between 0 and the size of our data
				int index = (int)(Math.random() * allData.size());
				baggedData.addProteinToTrain(allData.get(index));
			}
			// Hold on to this for later
			data.add(baggedData);
		}
	}
}
