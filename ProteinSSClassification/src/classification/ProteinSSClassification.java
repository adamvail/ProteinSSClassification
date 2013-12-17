package classification;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import neuralnetwork.AutoencoderController;
import baseline.BaselineNeuralNetwork;
import dataprocessing.CrossValidation;
import dataprocessing.Protein;
import dataprocessing.ProteinDataSet;
import ensemble.NeuralNetEnsemble;

public class ProteinSSClassification {
	
	static int crossValidationDegree = 5;
	BufferedWriter outputFile = null;
	
	public ProteinSSClassification(String trainFilename, String testFilename,  int windowSize, int numHiddenLayers,
			int hiddenLayerSize, int iterations, String outputDir, boolean baseline, boolean traditionalOutput, double decayBy,
			boolean ensemble, int numEnsembles){

		if(ensemble){
			System.out.println("Creating ensemble");
			NeuralNetEnsemble NNEnsemble = new NeuralNetEnsemble(trainFilename, testFilename, hiddenLayerSize, windowSize, numEnsembles);
		}
		else if(baseline) {
			runBaseline(trainFilename, testFilename, hiddenLayerSize, windowSize, outputDir);
		}
		else {
			runDeepNetwork(trainFilename, testFilename, windowSize, numHiddenLayers, hiddenLayerSize, 
					iterations, outputDir, traditionalOutput, decayBy);
		}
		 
	}
	
	public void runDeepNetwork(String trainFilename, String testFilename,  int windowSize, int numHiddenLayers,
			int hiddenLayerSize, int iterations, String outputDir, boolean traditionalOutput, double decayBy) {
		ArrayList<ProteinDataSet> data;
		
		Calendar c = Calendar.getInstance();
		String date = c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
		String time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
		String outputFilenameBase = date + "_" + time + "_" + numHiddenLayers + "hl_" + hiddenLayerSize + "hls_" + windowSize + "ws";

		if(testFilename == null) {
			data = CrossValidation.processData(crossValidationDegree, trainFilename);			
		}
		else {
			System.out.println("Using UCI data");
			data = CrossValidation.processData(trainFilename, testFilename);
		}
		
		/*
		data = new ArrayList<ProteinDataSet>();
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
		data.add(pdata);	
		*/
		
		int iter = 1;
		// Loop through all our data with the given parameters
		for(ProteinDataSet d : data) {
			/*
			try {
				outputFile = new BufferedWriter(new FileWriter(new File(outputDir, outputFilenameBase + "_" + iter )));
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
			
			AutoencoderController controller = new AutoencoderController(d, windowSize, outputFile);
			controller.learnInitialLayer(hiddenLayerSize, 25);
			int decayAmt = (int)(hiddenLayerSize * decayBy);
			for(int i = 0; i < numHiddenLayers - 1; i++) {
				writeOutput("Begin: learn Hidden layer of size " + (hiddenLayerSize - i * decayAmt));
				System.out.println("Begin: learn Hidden layer of size " + (hiddenLayerSize - i * decayAmt));
				controller.learnHiddenLayer(hiddenLayerSize - i * decayAmt, 10);
			}
			if(traditionalOutput) {
				controller.learnOutputLayer(iterations);
			}
			else {
				controller.learnConnectedOutputLayer(10);
			}

			controller.runTestSet();
			iter++;
			/*
			try {
				outputFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
	}
	
	public void writeOutput(String output) {
		if(outputFile != null) {
			try {
				outputFile.write(output);
			} catch (IOException e) {
				
			}
		}
	}
	
	public void runBaseline(String trainFilename, String testFilename, int hiddenLayerSize, int windowSize, String outputDir) {
		
		ArrayList<ProteinDataSet> data;
		
		if(testFilename == null) {
			data = CrossValidation.processData(crossValidationDegree, trainFilename);			
		}
		else {
			System.out.println("Using UCI data");
			data = CrossValidation.processData(trainFilename, testFilename);
		}
		
		Calendar c = Calendar.getInstance();
		String date = c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
		String time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
		String outputFilenameBase = date + "_" + time + "_Baseline_" + hiddenLayerSize + "hls_" + windowSize + "ws";
		
		int iter = 1;
		for(ProteinDataSet proteins : data) {
			/*try {
			//	outputFile = new BufferedWriter(new FileWriter(new File(outputDir, outputFilenameBase + "_" + iter )));
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
					
			//System.out.println("\n" + proteins.getTrain().size());
			BaselineNeuralNetwork nn = new BaselineNeuralNetwork(proteins, outputFile, hiddenLayerSize, windowSize);
			iter++;
			/*try {
				outputFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Here is the start of our protein secondary
		// structure classification project
		

		if(!(args.length == 12)){
			System.out.println("Usage: ./ProteinSSClassification <protein train> <protein test> " + 
						"<window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline> <traditional output>");
			System.exit(1);
		}
		
		String trainFilename = args[0];
		String testFilename = args[1];
		int windowSize = Integer.parseInt(args[2]);
		int numHiddenLayers = Integer.parseInt(args[3]);
		int hiddenLayerSize = Integer.parseInt(args[4]);
		int iterations = Integer.parseInt(args[5]);
		String outputDir = args[6];
		boolean baseline = false;
		if(args[7].equalsIgnoreCase("yes") || args[7].equalsIgnoreCase("y")) {
			baseline = true;
		}

		boolean traditionalOutput = true;
		if(args[8].equalsIgnoreCase("no") || args[8].equalsIgnoreCase("n")) {
			traditionalOutput = false;
		}

		double decayBy = 0.0;
		if (args.length == 9) {
			decayBy = Double.parseDouble(args[9]);
		}
		
		boolean ensemble = false;
		if(args[10].equalsIgnoreCase("yes") || args[10].equalsIgnoreCase("y")) {
			ensemble = true;
		}
		
		if(testFilename.equalsIgnoreCase("none")) {
			testFilename = null;
		}
		int numEnsembles = Integer.parseInt(args[11]);
		
		ProteinSSClassification classification = new ProteinSSClassification(trainFilename, testFilename, windowSize, 
					numHiddenLayers, hiddenLayerSize, iterations, outputDir, baseline,traditionalOutput, decayBy, ensemble, numEnsembles);		
		
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
