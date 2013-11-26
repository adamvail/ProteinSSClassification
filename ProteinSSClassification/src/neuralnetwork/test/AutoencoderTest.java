package neuralnetwork.test;

import java.util.ArrayList;

import neuralnetwork.AutoencoderController;

import org.junit.Test;

import dataprocessing.Protein;
import dataprocessing.ProteinDataSet;

public class AutoencoderTest {


	@Test
	public void test() {
		ProteinDataSet pdata = new ProteinDataSet();
		pdata.addProteinToTrain(new Protein("Y", "h"));
		pdata.addProteinToTrain(new Protein("F", "e"));
		pdata.addProteinToTrain(new Protein("A", "-"));
		pdata.addProteinToTrain(new Protein("YY", "hh"));
		pdata.addProteinToTrain(new Protein("YYF", "hhe"));
				
		AutoencoderController controller = new AutoencoderController(pdata);
	
		controller.learnInitialLayer(26);
		System.out.println("\nHidden layer 1");
		for (ArrayList<Double> d: controller.getProcessedData()) {
			controller.getMostRecentLayer().classifyAutoencoder(d);
		}
		
		ArrayList<ArrayList<Double>> newData = controller.feedDataThroughNetwork();
		controller.learnHiddenLayer(26);
		System.out.println("\nHidden layer 2");
		for (ArrayList<Double> d: newData) {
			controller.getMostRecentLayer().classifyAutoencoder(d);
		}
		controller.learnOutputLayer();
		
		controller.testAllTrainingData();
		
	}	
}
