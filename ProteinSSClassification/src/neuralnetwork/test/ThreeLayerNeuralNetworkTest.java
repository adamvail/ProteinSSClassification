package neuralnetwork.test;

import java.util.ArrayList;

import neuralnetwork.NeuralNetworkController;
import neuralnetwork.NeuralNetworkController.STRUCTURE;
import neuralnetwork.Unit;

import org.junit.Test;

public class ThreeLayerNeuralNetworkTest {


	@Test
	public void test() {
		ArrayList<Unit> inputLayer = new ArrayList<Unit>();
		for (int i = 0; i < 6; i++) {
			inputLayer.add(new Unit(false));
		}
		NeuralNetworkController netAE = new NeuralNetworkController(inputLayer, 5, null);

		// Input: exactly 1 unit turned on
		// Teach: alpha if units 0/1 turned on, beta if units 2/3 turned on,
		// loop
		// if units 4/5 turned on.

		// Training set
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		ArrayList<STRUCTURE> labels = new ArrayList<STRUCTURE>();
		for (int i = 0; i < 6; i++) {
			ArrayList<Double> item = new ArrayList<Double>();
			for (int target = 0; target < 6; target++) {
				if (target == i) {
					item.add(1.0);
				} else {
					item.add(0.0);
				}
			}
			data.add(item);
			if (i < 2) {
				labels.add(STRUCTURE.ALPHA);
			} else if (i < 4) {
				labels.add(STRUCTURE.BETA);
			} else {
				labels.add(STRUCTURE.LOOP);
			}

		}

		// Output network
		ArrayList<ArrayList<Unit>> outAE = netAE.autoencoderLearn(data);
		ArrayList<Double> weights = new ArrayList<Double>();

		weights = new ArrayList<Double>();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				// System.out.print(out.get(0).size() + "/" +
				// out.get(1).size()
				// + "  ");
				// System.out.print((3*i+j) + " ");
				weights.add(outAE.get(1).get(j).getWeight(outAE.get(0).get(i)));
			}
		}
		// net.printGraph();

		// Test that weights are as expected.
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.println("Weight " + i + " -> " + j + " : "
						+ (weights.get(i * 3 + j)));
			}
		}
		
		
		for (int i = 0; i < 6; i++) {
			netAE.classifyAutoencoder(data.get(i));
		}
		 
		// Train the system
		NeuralNetworkController net = new NeuralNetworkController(outAE.get(1), null);
		ArrayList<ArrayList<Double>> fedData = new ArrayList<ArrayList<Double>>();
		for(ArrayList<Double> d : data) {
			fedData.add(netAE.testHiddenLayer(d));
		}
		ArrayList<ArrayList<Unit>> outNN = net.neuralNetworkLearn(fedData, labels);
		

		for (int i = 0; i < 6; i++) {
			System.out.print("Instance " + i + " is ");
			if (net.classifyNeuralNetwork(fedData.get(i)) != labels.get(i)) {
				System.out.print("NOT ");
			}
			System.out.println("labeled correctly.");
		}
		
	}

}
