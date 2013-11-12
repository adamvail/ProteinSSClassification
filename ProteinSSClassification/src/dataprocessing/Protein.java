package dataprocessing;

public class Protein {

	private String sequence;
	private String secondaryStructure;
	
	public Protein(String sequence, String secondaryStructure){
		this.sequence = sequence;
		this.secondaryStructure = secondaryStructure;
	}
	
	public String getSequence(){
		return this.sequence;
	}
	
	public String getSecondaryStructure(){
		return this.secondaryStructure;
	}
}
