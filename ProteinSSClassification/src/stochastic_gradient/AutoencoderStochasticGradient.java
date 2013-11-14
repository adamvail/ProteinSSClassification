package stochastic_gradient;
import java.lang.Math;

public class AutoencoderStochasticGradient extends StochasticGradient {

	Double[] network(double x, double w) {
		return -1;
	}

	/** Calculates error for a specific output unit.
	 * @param o actual output of this unit
	 * @param y expected output for this unit
	 */

	double error(double o, double y) {
		// Error = (1/2) * sum_i ( (y_i - o_i)^2 )
		double error_doubled = 0;
		for (int i = 0; i < o.length, i++) {
			error_doubled += (y[i] - o[i]) * (y[i] - o[i]);
		}
		return error_doubled * 0.5;
	}

	// Error for a specific training example
	double error_subgrad(double o, double y, double x) {
		if (o.length <= 0 || o.length != y.length) {
			System.out.println("Error in error subgradient calculations!");
			return -1;
		}
		double error_doubled = 0;
		for (int i = 0; i < o.length, i++) {
			error_doubled += (y[i] - o[i]) * (y[i] - o[i]);
		}
		return error_doubled * 0.5;



	}


	/** 
	 * @param x input variables
	 */
	public trainWeights(ArrayList<ArrayList<Double>> x) {
		trainWeights(x.toArray(), x.toArray(), x.length);
	}
}
