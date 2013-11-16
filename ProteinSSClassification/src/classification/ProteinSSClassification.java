package classification;

import java.util.ArrayList;

import neuralnetwork.NeuralNetworkController;
import neuralnetwork.Unit;
import dataprocessing.CrossValidation;
import dataprocessing.Protein;
import dataprocessing.ProteinDataSet;

public class ProteinSSClassification {
	
	public ProteinSSClassification(String filename){
		//ArrayList<ProteinDataSet> data = CrossValidation.processData(crossValidationDegree, filename);

		//printDataSets(data);
		
		ArrayList<ArrayList<Double>> d = new ArrayList<ArrayList<Double>>();
		d.add(new ArrayList<Double>());
		d.get(0).add(1.0);
		d.get(0).add(0.0);
		d.get(0).add(1.0);
		
		NeuralNetworkController controller = new NeuralNetworkController(3, 5);
		
		ArrayList<Unit> hiddenLayer = controller.autoencoderLearn(d);
		System.out.println("Hidden layer size: " + hiddenLayer.size());
	}
	

	static int crossValidationDegree = 7;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Here is the start of our protein secondary
		// structure classification project
		
		if(args.length != 2){
			System.out.println("Usage: ./ProteinSSClassification <protein train> <protein test>");
			System.exit(1);
		}
		
		ProteinSSClassification classification = new ProteinSSClassification(args[0]);

	}
	
	public void printDataSets(ArrayList<ProteinDataSet> dataSets){
		for(ProteinDataSet ds : dataSets){
			for(Protein p : ds.getTrain()){
				System.out.println(p.getName());
			}
			System.out.println("\n");
			
			for(Protein p : ds.getTest()){
				System.out.println(p.getName());
			}
			
			System.out.println("------------------------------\n");
		}
	}

}
