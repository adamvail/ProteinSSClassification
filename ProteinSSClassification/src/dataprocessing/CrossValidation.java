package dataprocessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CrossValidation {
	
	public static ArrayList<ProteinDataSet> processData(int degree, String filename){		
		return crossValidation(degree, readData(filename));
	}
	
	public static ArrayList<ProteinDataSet> processData(String trainName, String testName) {
		ArrayList<ProteinDataSet> data = new ArrayList<ProteinDataSet>();
		ProteinDataSet dataSet = new ProteinDataSet();
		
//		ArrayList<Protein> train = new ArrayList<Protein>();
//		train.addAll(readData(trainName).subList(0, 50));
		
		dataSet.addProteinListToTrain(readData(trainName));
		dataSet.addProteinListToTest(readData(testName));
		
		data.add(dataSet);
		return data;
	}
	
	private static ArrayList<ProteinDataSet> crossValidation(int degree, ArrayList<Protein> proteins){
		
		ArrayList<ProteinDataSet> fullDataSet = new ArrayList<ProteinDataSet>();
		ArrayList<ArrayList<Protein>> chunks = new ArrayList<ArrayList<Protein>>();
		int group_size;
		int remainder = proteins.size() % degree;
		int group_index = 1;
		
		if(remainder == 0){
			group_size = proteins.size() / degree;
		}
		else {
			group_size = proteins.size() / (degree - 1);
		}
		
		// make sure the last group isn't going to be too small
		if((group_size - remainder) > 0){
			// There is room to give every group one less so that
			// the final group will have a closer number to all other groups
			group_size = group_size - ((group_size - remainder) / degree);
		}
		
		// Chunk the data into separate sets
		for(int i = 1; i <= proteins.size(); i++){
			if(chunks.size() < group_index){
				chunks.add(new ArrayList<Protein>());
			}
			
			chunks.get(group_index - 1).add(proteins.get(i - 1));
			
			if(i % group_size == 0){
				group_index++;
				if(group_index > degree + 1){
					System.out.println("Too many chunks: " + group_index);
				}
			}
		}
		
		// Create the separate groups for the cross validation
		for(int i = 0; i < degree; i++){
			ProteinDataSet currDataSet = new ProteinDataSet();
			for(int k = 0; k < chunks.size(); k++){
				if(k == i){
					currDataSet.addProteinListToTest(chunks.get(k));
				}
				else {
					currDataSet.addProteinListToTrain(chunks.get(k));
				}
			}
			fullDataSet.add(currDataSet);
		}
		return fullDataSet;
	}
	
	public static ArrayList<Protein> readData(String filename){
		ArrayList<Protein> proteins = new ArrayList<Protein>();
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while(line != null){
				
				String[] tokens = line.split("[ ]+");
				if(tokens.length == 3){
					proteins.add(new Protein(tokens[0], tokens[1], tokens[2]));
				}
				else if(tokens.length == 2){
					proteins.add(new Protein(tokens[0], tokens[1]));
				}
				else {
					System.out.println("DO NOT KNOW HOW TO HANDLE PROTEIN:\n" + line);
				}
				
				line = reader.readLine();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file: " + filename);
		} catch (IOException e) {
			System.out.println("Could not read from file: " + filename);
		}
		
		try {
			reader.close();
		} catch (IOException e) {
			System.out.println("Could not close input file");
		}
		
		return proteins;
	}
	

}
