package dataprocessing;

import java.util.ArrayList;

public class ProteinDataSet {

	private ArrayList<Protein> train;
	private ArrayList<Protein> test;
	
	public ProteinDataSet(){
		this.train = new ArrayList<Protein>();
		this.test = new ArrayList<Protein>();
	}
	
	public void addProteinToTrain(Protein protein){
		this.train.add(protein);
	}
	
	public void addProteinToTest(Protein protein){
		this.test.add(protein);
	}
	
	public ArrayList<Protein> getTrain(){
		return this.train;
	}
	
	public ArrayList<Protein> getTest(){
		return this.test;
	}

}
