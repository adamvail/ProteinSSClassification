package dataprocessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CrossValidation {
	
	public static ArrayList<ProteinDataSet> processData(int degree, String filename){		
		return crossValidation(readData(filename));
	}
	
	private static ArrayList<ProteinDataSet> crossValidation(ArrayList<Protein> proteins){
		
		// TODO actually chop up the data to accomplish cross validation
		
		return null;
	}
	
	private static ArrayList<Protein> readData(String filename){
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
