package stochastic_gradient;
import java.lang.Math;
import java.util.ArrayList;

public abstract class StochasticGradient {
	double eta;  // Learning rate  (step size)
	int m;  // Max iters

	StochasticGradient() {
		eta = 0.1;
		m = 100;
	}

	/** Calculates output of specific unit  (assumes no constant inputs!)
	 * @param x array of inputs to unit
	 * @param w array of weights of unit
	 * @return expected output of network unit
	 */
	public abstract double network(Double[] x, Double[] w);

	/** Calculates error for a specific output unit.
	 * @param o actual output of this unit
	 * @param y expected output for this unit
	 * @return error for specific output unit
	 */
	public abstract double error(double o, double y);

	/** Calculates error gradient for a specific output unit.
	 * @param o actual output of this unit
	 * @param y expected output for this unit
	 * @return error gradient for specific output unit
	 */
	public abstract Double[] error_subgrad(double o, double y, Double[] x);

	/**  Trains the weights for one specific output unit
	 * @param x arraylist of training instances, which contains an array of x values
	 * @param y arraylist of training instances' class labels
	 * @return weights input to unit.
	 */
	Double[] trainWeights(ArrayList<ArrayList<Double>x>, ArrayList<Double> y) {
		// TODO: more intelligent stopping criteria
		// TODO:  Allow weights to be initialized more rationally?
		if (x.length <= 0) {
			System.out.println("No training instances!");
			return null;
		}
		// Assume each training instance has same length.
		Double[] w = new Double(x[0].length);
		for (int i = 0; i < w.length; i++) {
			w[i] = Math.random();
		}

		for (int k = 0; k < m; k++) {
			for (int d = 0; d < x.length(); d++) {
				double o = network(x[d].toArray(), w);
				double err = error(o, y[d]);  // Use this for a better stopping criterion
				Double[] grad_err = error_subgrad(o, y[d], x[d].toArray());
				for (int i = 0; i < w.length; i++) {
					w[i] -= eta * grad_err[i];
				}
			}
		}
	}
}
