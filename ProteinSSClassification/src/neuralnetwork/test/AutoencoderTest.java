package neuralnetwork.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import neuralnetwork.AutoencoderController;
import neuralnetwork.Unit;

import org.junit.Test;

import dataprocessing.Protein;
import dataprocessing.ProteinDataSet;

public class AutoencoderTest {


	@Test
	public void test() {
		
		testFeedForward();
		
		System.out.println("Test 2-layer autoencoder controller");
		ProteinDataSet pdata = new ProteinDataSet();
		pdata.addProteinToTrain(new Protein("Y", "h"));
		pdata.addProteinToTrain(new Protein("F", "e"));
		pdata.addProteinToTrain(new Protein("A", "-"));
		/*
		pdata.addProteinToTrain(new Protein("F", "e"));
		pdata.addProteinToTrain(new Protein("Y", "h"));
		pdata.addProteinToTrain(new Protein("A", "-"));
		pdata.addProteinToTrain(new Protein("YY", "hh"));
		pdata.addProteinToTrain(new Protein("FF", "ee"));
		pdata.addProteinToTrain(new Protein("AA", "--"));
		pdata.addProteinToTrain(new Protein("F", "e"));
		pdata.addProteinToTrain(new Protein("A", "-"));
		pdata.addProteinToTrain(new Protein("Y", "h"));
		pdata.addProteinToTrain(new Protein("AAAA", "----"));
		pdata.addProteinToTrain(new Protein("YYYY", "hhhh"));
		pdata.addProteinToTrain(new Protein("FFFF", "eeee"));
		pdata.addProteinToTrain(new Protein("Y", "h"));
		pdata.addProteinToTrain(new Protein("A", "-"));
		pdata.addProteinToTrain(new Protein("F", "e"));*/
		//pdata.addProteinToTrain(new Protein("YYF", "hhe"));
				
		AutoencoderController controller = new AutoencoderController(pdata);
	
		System.out.println("\nHidden layer 1");
		controller.learnInitialLayer(10);
		for (ArrayList<Double> d: controller.getProcessedData()) {
			controller.getMostRecentLayer().classifyAutoencoder(d);
		}
		controller.getMostRecentLayer().printHiddenWeightsFromInput(120);
		controller.getMostRecentLayer().printHiddenWeightsFromInput(133);
		controller.getMostRecentLayer().printHiddenWeightsFromInput(138);
		controller.getMostRecentLayer().printOutputWeightsFromHiddenLayerToOutput(138) ;

		Integer[] inputUnits = new Integer[3];
		inputUnits[0] = 120;
		inputUnits[1] = 133;
		inputUnits[2] = 138;
		//controller.printOrthogonalityOfHiddenWeightsToSelectedInputUnits(inputUnits);

		
		
		ArrayList<ArrayList<Double>> newData = controller.feedDataThroughNetwork();  // Outputs of hidden layer

		System.out.println("\nHidden layer 10");		
		controller.learnHiddenLayer(2);
		
		//controller.getMostRecentLayer().printHiddenWeightsFromInput(0);
		//controller.getMostRecentLayer().printHiddenWeightsFromInput(1);
		//controller.getMostRecentLayer().printHiddenWeightsFromInput(2);
		//controller.printOrthogonalityOfWeights(2);
		
		for (ArrayList<Double> d: newData) {
			controller.getMostRecentLayer().classifyAutoencoder(d);
		}
		
		System.out.println("\nOutput layer");
		controller.learnOutputLayer();
		
		controller.getMostRecentLayer().printOutputWeightsFromHiddenLayer();
		
		controller.printOrthogonalityOfWeights(3);
		
		controller.testAllTrainingData();
		
	}
	
	
	public void testOneLayer() {
		System.out.println("Test 1 layer autoencoder controller");
		ProteinDataSet pdata = new ProteinDataSet();
		pdata.addProteinToTrain(new Protein("Y", "h"));
		pdata.addProteinToTrain(new Protein("F", "e"));
		pdata.addProteinToTrain(new Protein("A", "-"));
		pdata.addProteinToTrain(new Protein("F", "e"));
		pdata.addProteinToTrain(new Protein("Y", "h"));
		pdata.addProteinToTrain(new Protein("A", "-"));
		pdata.addProteinToTrain(new Protein("YY", "hh"));
		pdata.addProteinToTrain(new Protein("FF", "ee"));
		pdata.addProteinToTrain(new Protein("AA", "--"));
		pdata.addProteinToTrain(new Protein("F", "e"));
		pdata.addProteinToTrain(new Protein("A", "-"));
		pdata.addProteinToTrain(new Protein("Y", "h"));
		pdata.addProteinToTrain(new Protein("AAAA", "----"));
		pdata.addProteinToTrain(new Protein("YYYY", "hhhh"));
		pdata.addProteinToTrain(new Protein("FFFF", "eeee"));
		pdata.addProteinToTrain(new Protein("Y", "h"));
		pdata.addProteinToTrain(new Protein("A", "-"));
		pdata.addProteinToTrain(new Protein("F", "e"));
				
		AutoencoderController controller = new AutoencoderController(pdata);
	
		System.out.println("\nHidden layer 1");
		controller.learnInitialLayer(20);
		for (ArrayList<Double> d: controller.getProcessedData()) {
			controller.getMostRecentLayer().classifyAutoencoder(d);
		}
		//controller.getMostRecentLayer().printHiddenWeightsFromInput(120);
		//controller.getMostRecentLayer().printHiddenWeightsFromInput(133);
		//controller.getMostRecentLayer().printHiddenWeightsFromInput(138);

		Integer[] inputUnits = new Integer[3];
		inputUnits[0] = 120;
		inputUnits[1] = 133;
		inputUnits[2] = 138;
		//controller.printOrthogonalityOfHiddenWeightsToSelectedInputUnits(inputUnits);
		
		
		ArrayList<ArrayList<Double>> newData = controller.feedDataThroughNetwork();
				
		System.out.println("\nOutput layer");
		controller.learnOutputLayer();
		
		//controller.getMostRecentLayer().printOutputWeightsFromHiddenLayer();
		
		//controller.printOrthogonalityOfWeights(2);
		
		controller.testAllTrainingData();		
	}
	
	public void testFeedForward() {
		ProteinDataSet pdata = new ProteinDataSet();
		AutoencoderController controller = new AutoencoderController(pdata);
		
		ArrayList<Unit> inputs = new ArrayList<Unit>();		
		inputs.add(new Unit(false));
		inputs.add(new Unit(false));
		inputs.add(new Unit(false));
		inputs.add(new Unit(false));
		inputs.add(new Unit(false));
		
		
		ArrayList<Unit> layer1 = new ArrayList<Unit>();
		layer1.add(new Unit(false));
		layer1.add(new Unit(false));
		layer1.add(new Unit(false));

		layer1.get(0).addInput(inputs.get(0), 0);
		layer1.get(0).addInput(inputs.get(1), 1);
		layer1.get(0).addInput(inputs.get(2), 0);
		layer1.get(0).addInput(inputs.get(3), 0);
		layer1.get(0).addInput(inputs.get(4), 0);
		layer1.get(1).addInput(inputs.get(0), 0);
		layer1.get(1).addInput(inputs.get(1), 0);
		layer1.get(1).addInput(inputs.get(2), 0);
		layer1.get(1).addInput(inputs.get(3), 0.3);
		layer1.get(1).addInput(inputs.get(4), 0.7);
		layer1.get(2).addInput(inputs.get(0), 0);
		layer1.get(2).addInput(inputs.get(1), 0);
		layer1.get(2).addInput(inputs.get(2), 0);
		layer1.get(2).addInput(inputs.get(3), 0.5);
		layer1.get(2).addInput(inputs.get(4), 0.5);
		
		ArrayList<Unit> layer2 = new ArrayList<Unit>();
		layer2.add(new Unit(false));
		layer2.add(new Unit(false));
		layer2.get(0).addInput(inputs.get(0), 0);
		layer2.get(0).addInput(inputs.get(1), 1);
		layer2.get(0).addInput(inputs.get(2), 0);
		layer2.get(1).addInput(inputs.get(0), 1);
		layer2.get(1).addInput(inputs.get(1), 0);
		layer2.get(1).addInput(inputs.get(2), 1);
		
		controller.network.add(inputs);
		controller.network.add(layer1);
		//controller.network.add(layer2);
		
		
		ArrayList<Double> input1 = new ArrayList<Double>();
		input1.add(1.0);
		input1.add(1.0);
		input1.add(1.0);
		input1.add(0.2);
		input1.add(0.8);
		controller.processedData = new ArrayList<ArrayList<Double>>();
		controller.processedData.add(input1);
		
		ArrayList<ArrayList<Double>> newData = controller.feedDataThroughNetwork();
		assertTrue(Math.abs(newData.get(0).get(0) - sigmoid(0.3*0.2+0.7*0.8)) > 0.001);
		assertTrue(Math.abs(newData.get(0).get(1) - sigmoid(0.5*0.2+0.5*0.8)) > 0.001);
		
		System.out.println("Passed feed through network test\n\n");	
	}
	
	private double sigmoid(double in) {
		return 1.0 / (1.0 + Math.exp(-1 * in));
	}
	
}
