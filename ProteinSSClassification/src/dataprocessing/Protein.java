package dataprocessing;

public class Protein {

	private String name = null;
	private String sequence;
	private String secondaryStructure;
	
	public Protein(String sequence, String secondaryStructure){
		this.sequence = sequence;
		this.secondaryStructure = secondaryStructure;
	}
	
	public Protein(String name, String sequence, String secondaryStructure){
		this.name = name;
		this.sequence = sequence;
		this.secondaryStructure = secondaryStructure;
	}
	
	public String getName(){
		return name;
	}
	
	public String getSequence(){
		return this.sequence;
	}
	
	public String getSecondaryStructure(){
		return this.secondaryStructure;
	}
}
