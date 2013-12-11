package classification;

import java.util.ArrayList;

import baseline.BaselineNeuralNetwork;
import dataprocessing.CrossValidation;
import dataprocessing.Protein;
import dataprocessing.ProteinDataSet;

public class ProteinSSClassification {
	
	public ProteinSSClassification(String filename, String testFilename){
		ArrayList<ProteinDataSet> data;
		if(testFilename == null) {
			data = CrossValidation.processData(crossValidationDegree, filename);
		}
		else {
			System.out.println("Using UCI data");
			data = CrossValidation.processData(filename, testFilename);
		}
		
		runBaseline(data);

		//printDataSets(data);
/*		
		ArrayList<ArrayList<Double>> d1 = new ArrayList<ArrayList<Double>>();
		d1.add(new ArrayList<Double>());
		d1.get(0).add(1.0);
		d1.get(0).add(0.0);
		d1.get(0).add(1.0);
		
	//	NeuralNetworkController controller = new NeuralNetworkController(3, 5);
		
		ArrayList<ArrayList<Double>> d2 = new ArrayList<ArrayList<Double>>();
		d2.add(new ArrayList<Double>());
		d2.get(0).add(1.0);
		d2.get(0).add(0.0);
		d2.get(0).add(0.0);
		
	//	ArrayList<Unit> hiddenLayer1 = controller.autoencoderLearn(d1);
	//	ArrayList<Unit> hiddenLayer2 = controller.autoencoderLearn(d1);
		ProteinDataSet reformedData = new ProteinDataSet();
		reformedData.addProteinToTrain(data.get(0).getTrain().get(0));
		reformedData.addProteinToTest(data.get(0).getTrain().get(0));
		ArrayList<ProteinDataSet> d = new ArrayList<ProteinDataSet>();
		d.add(reformedData);
		
		//runBaseline(d);
*/	 
	}
	
	public void runBaseline(ArrayList<ProteinDataSet> data) {
		for(ProteinDataSet proteins : data) {
			//System.out.println("\n" + proteins.getTrain().size());
			BaselineNeuralNetwork nn = new BaselineNeuralNetwork(proteins);
		}
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
		
		ProteinSSClassification classification;
		
		if(args[1].equalsIgnoreCase("none")) {
			classification = new ProteinSSClassification(args[0], null);
		}
		else {
			classification = new ProteinSSClassification(args[0], args[1]);
		}
		
		
	}
	
	public void printDataSets(ArrayList<ProteinDataSet> dataSets){
		for(ProteinDataSet ds : dataSets){
			for(Protein p : ds.getTrain()){
				System.out.println(p.getSequence());
			}
			System.out.println("\n");
			
			for(Protein p : ds.getTest()){
				System.out.println(p.getSequence());
			}
			
			System.out.println("------------------------------\n");
		}
	}

}
