package neuralnetwork.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import neuralnetwork.NeuralNetworkController;
import neuralnetwork.Unit;
import neuralnetwork.NeuralNetworkController.STRUCTURE;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PerceptronTest {


	@Test
	public void test() {
		ArrayList<Unit> inputLayer = new ArrayList<Unit>();
		for (int i = 0; i < 6; i++) {
			inputLayer.add(new Unit(false));
		}
		NeuralNetworkController net = new NeuralNetworkController(inputLayer);

		// Input: exactly 1 unit turned on
		// Teach: alpha if units 0/1 turned on, beta if units 2/3 turned on, loop
		// if units 4/5 turned on.
		
		// Training set
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		ArrayList<STRUCTURE> labels = new ArrayList<STRUCTURE>();
		for (int i = 0; i < 6; i++) {
			ArrayList<Double> item = new ArrayList<Double>();
			for (int target = 0; target < 6; target++) {
				if (target == i){
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
		ArrayList<ArrayList<Unit>> out = net.neuralNetworkLearn(data, labels);

		//net.printGraph();
		
		ArrayList<Double> weights = new ArrayList<Double>();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				//System.out.print(out.get(0).size() + "/" + out.get(1).size()
				//		+ "  ");
				//System.out.print((3*i+j) + " ");
				weights.add(out.get(1).get(j).getWeight(
						out.get(0).get(i)));
			}
		}
		
		// Test that weights are as expected.
		boolean testPassed = true;
		for (int i = 0; i < 6; i++) {
			int target;
			if (i < 2) {
				target = 0;
			} else if (i < 4) {
				target = 1;
			} else {
				target = 2;
			}
			for (int j = 0; j < 3; j++) {
				if (target == j) {
					System.out.println("Weight " + (i*3 + j) + ": " + (weights.get(i*3+j)) + " should be > .5");
					if (weights.get(i*3 + j) < .5) {
						testPassed = false;
					}
				} else {
					System.out.println("Weight " + (i*3 + j) + ": " + (weights.get(i*3+j)) + " should be < .5");
					if (weights.get(i*3 + j) > .5) {
						testPassed = false;
					}
				}
			}
		}
		
		for (int i = 0; i < 6; i++) {
			System.out.print("Instance " + i + " is ");
			if (net.classifyNeuralNetwork(data.get(i)) != labels.get(i)) {
				System.out.print("NOT ");
			}
			System.out.println("labeled correctly.");
		}
		
		assertTrue(testPassed);
	}

}
