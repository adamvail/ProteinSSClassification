package stochastic_gradient;
import java.lang.Math;

public class SigmoidOutputStochasticGradient extends StochasticGradient {

	/** Calculates output of specific unit  (assumes no constant inputs!)
	 * @param x array of inputs to unit
	 * @param w array of weights of unit
	 * @return expected output of network unit
	 */
	public double network(Double[] x, Double[] w) {
		if (x.length <= 0 || x.length != w.length)
			System.out.println("Error in network calculations!");
			return -1;
		}
		double net = 0;
		for (int i = 0; i < x.length; i++) {
			net += x[i] * w[i];
		}
		return 1.0 / (1.0 + Math.exp(-1.0 * net));
	}

	/** Calculates error for a specific output unit.
	 * @param o actual output of this unit
	 * @param y expected output for this unit
	 * @return error for specific output unit
	 */
	double error(double o, double y) {
		// Error = (1/2) * (y - o)^2
		return 0.5 * (y - o) * (y - o);
	}

	/** Calculates error gradient for a specific output unit.
	 * @param o actual output of this unit
	 * @param y expected output for this unit
	 * @return error gradient for specific output unit
	 */
	Double[] error_subgrad(double o, double y, Double[] x) {
		if (x.length <= 0)
			System.out.println("Error in error subgradient calculations!");
			return -1;
		}
		Double[] grad = new Double(x.length);
		double prefix = (y - o) * o * (1 - o);
		for (int i = 0; i < x.length, i++) {
			grad[i] = prefix * x[i];
		}
		return grad;
	}

	/**  Trains the weights for one specific output unit
	 * @param x arraylist of training instances, which contains an array of x values
	 * @param y arraylist of training instances' class labels
	 * @return weights input to unit.
	 */
	Double[] trainWeights(ArrayList<ArrayList<Double> x>, ArrayList<Double> y) {
		return trainWeights(x, y);
	}
}
