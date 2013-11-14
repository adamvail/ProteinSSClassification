package neuralnetwork;

import java.util.ArrayList;

public class InputUnit {

	ArrayList<Unit> subUnits = new ArrayList<Unit>();
	
	public InputUnit(int quantity){
		
		for(int i = 0; i < quantity; i++){
			subUnits.add(new Unit(false));
		}
		
	}
	
	public ArrayList<Unit> getInputSubUnits(){
		return subUnits;
	}
	
	public void setInputUnit(char aminoAcid){
		
		int target = ((int)aminoAcid) - 65;
		if(target > subUnits.size()){
			System.out.println("Bad amino acid: " + aminoAcid);
		}
		
		for(int i = 0; i < subUnits.size(); i++){
			if(i == target){
				subUnits.get(i).setValue(1);
			}
			else {
				subUnits.get(i).setValue(0);
			}
		}
	}
	
}
