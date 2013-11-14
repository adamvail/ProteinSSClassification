package neuralnetwork;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class Controller {

	private Collection<Collection<Unit>> allUnits;

	// Input is size of each layer of units.  Ex: 17 5 3
	public Controller(ArrayList<int> size){
		for(int i = 0; i < size.size(); i++) {
			Collection<Unit> layer = new Collection<Units>();
			for (int j = 0; j < size.get(i); j++) { 
				if (i < size.size() - 1) {
					layer.add(new Unit(false));
				}
				else {
					layer.add(new Unit(true));
				}
			}
			allUnits.add(layer);
		}

		// Need to set up connections
	}

	// Train network on a piece of data
	public void trainOnInstance(ArrayList<double> x, y) {
		if (x.size() != inputs.size()) {
			// Error
			System.out.println("Error in training instances");
			return;
		}
		// Set the values of each element of the network, starting with first
		// layer of network and working forward
		for (int i = 0; i < x.size(); i++) {
			inputs.get(i).setValue(x.get(i));
		}

		// Train weights, starting with back row of network and working backward.
		trainWeights(y,learningRate);
	}

	private void setNetworkValues() {
		// Initialize Input layer
		for (int i = 0; i < x.size(); i++) {
			allUnits.get(0).get(i).setValue(x.get(i));
		}
		// Initialize other layers
		for (Collection<Unit> layer : allUnits) {
			for (Unit unit : layer) {
			}
		}

	}
}
