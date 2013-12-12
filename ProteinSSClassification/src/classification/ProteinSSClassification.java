package classification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import neuralnetwork.AutoencoderController;
import baseline.BaselineNeuralNetwork;
import dataprocessing.Protein;
import dataprocessing.ProteinDataSet;

public class ProteinSSClassification {
	
	static int crossValidationDegree = 7;
	
	public ProteinSSClassification(String filename, String testFilename, int numHiddenLayers, int windowSize,
			int hiddenLayerSize, String outputDir){
		ArrayList<ProteinDataSet> data;
		
		Calendar c = Calendar.getInstance();
		String date = c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
		String time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
		String outputFilenameBase = date + "_" + time + "_" + numHiddenLayers + "hl_" + hiddenLayerSize + "hls_" + windowSize + "ws";
		
		/*
		if(testFilename == null) {
			data = CrossValidation.processData(crossValidationDegree, filename);			
		}
		else {
			System.out.println("Using UCI data");
			data = CrossValidation.processData(filename, testFilename);
		}
		*/
		
		ProteinDataSet pdata = new ProteinDataSet();
		pdata.addProteinToTrain(new Protein("Y", "h"));
		pdata.addProteinToTrain(new Protein("F", "e"));
		pdata.addProteinToTrain(new Protein("A", "-"));
		
		pdata.addProteinToTrain(new Protein("AA", "--"));
		pdata.addProteinToTrain(new Protein("YY", "hh"));
		pdata.addProteinToTrain(new Protein("FF", "ee"));
		
		pdata.addProteinToTrain(new Protein("AAAA", "----"));
		pdata.addProteinToTrain(new Protein("YYYY", "hhhh"));
		pdata.addProteinToTrain(new Protein("FFFF", "eeee"));

		pdata.addProteinToTrain(new Protein("YYF", "hhe"));
		pdata.addProteinListToTest(pdata.getTrain());
		
		data = new ArrayList<ProteinDataSet>();
		data.add(pdata);
		
		int iter = 1;
		// Loop through all our data with the given parameters
		for(ProteinDataSet d : data) {
			AutoencoderController controller = new AutoencoderController(d, windowSize);
			controller.learnInitialLayer(hiddenLayerSize);
			for(int i = 0; i < numHiddenLayers; i++) {
				controller.learnHiddenLayer(hiddenLayerSize);
			}
			controller.learnOutputLayer();
			
			// set up the output file
			BufferedWriter outputFile = null;
			try {
				outputFile = new BufferedWriter(new FileWriter(new File(outputDir, outputFilenameBase + "_" + iter )));
			} catch (IOException e) {
				e.printStackTrace();
			}

			controller.runTestSet(outputFile);
			iter++;
		}
		 
	}
	
	public void runBaseline(ArrayList<ProteinDataSet> data) {
		for(ProteinDataSet proteins : data) {
			//System.out.println("\n" + proteins.getTrain().size());
			BaselineNeuralNetwork nn = new BaselineNeuralNetwork(proteins);
		}
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Here is the start of our protein secondary
		// structure classification project
		
		if(args.length != 6){
			System.out.println("Usage: ./ProteinSSClassification <protein train> <protein test> " + 
						"<window size> <number of hidden layers> <hidden layer size> <output directory>");
			System.exit(1);
		}
		
		String trainFilename = args[0];
		String testFilename = args[1];
		int windowSize = Integer.parseInt(args[2]);
		int numHiddenLayers = Integer.parseInt(args[3]);
		int hiddenLayerSize = Integer.parseInt(args[4]);
		String outputDir = args[5];
		
		ProteinSSClassification classification;
		
		if(testFilename.equalsIgnoreCase("none")) {
			classification = new ProteinSSClassification(trainFilename, null, windowSize, 
					numHiddenLayers, hiddenLayerSize, outputDir);
		}
		else {
			classification = new ProteinSSClassification(trainFilename, testFilename, windowSize, 
					numHiddenLayers, hiddenLayerSize, outputDir);
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
