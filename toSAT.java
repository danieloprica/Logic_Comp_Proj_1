import org.sat4j.specs.ISolver;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;


/**
 * Represents SAT reduction for Reverse Mastermind and implements algorithm to solve Reverse Mastermind game.
 * Requires sat4j-core-v20130525.jar library.
 */
public class toSAT {
	public static int codelen = 4;
	public static int colors = 6;
	public static ISolver solverDigOne = SolverFactory.newDefault();
	public static ISolver solverDigTwo = SolverFactory.newDefault();
	public static ISolver solverDigThree = SolverFactory.newDefault();
	public static ISolver solverDigFour = SolverFactory.newDefault();

	/**
	 * Prints an integer array.
	 * @param arr the array to be printed.
	 */
	public static void printArr(int[] arr) {
		for (int j : arr) {
			System.out.print(j);
			System.out.print(" ");
		}
    }


	/**
	 * This method converts a clause in conjunctive normal form to a Reverse Mastermind code.
	 * @param cnf the conjunctive normal form (in Dimacs format) provided by the sat solver.
	 * @return a satisfiable answer for the digit.
	 */
	public static int cnf2Code(int[] cnf) {
		int ret = 0;
		for (int i : cnf) {
			if (i > 0) {
				ret = i - 1;
				break;
			}
		}
		return ret;
    }

	/**
	 * Provides the initial constraints for each digit.
	 * Namely, that it contains only one color.
	 *
	 * @param s the solver for a digit to constrain.
	 */
	public static void defineInitConstraints(ISolver s) {
		try {
			s.addClause(new VecInt(new int[] {-1, -2}));
			s.addClause(new VecInt(new int[] {-1, -3}));
			s.addClause(new VecInt(new int[] {-1, -4}));
			s.addClause(new VecInt(new int[] {-1, -5}));
			s.addClause(new VecInt(new int[] {-1, -6}));
			s.addClause(new VecInt(new int[] {-2, -3}));
			s.addClause(new VecInt(new int[] {-2, -4}));
			s.addClause(new VecInt(new int[] {-2, -5}));
			s.addClause(new VecInt(new int[] {-2, -6}));
			s.addClause(new VecInt(new int[] {-3, -4}));
			s.addClause(new VecInt(new int[] {-3, -5}));
			s.addClause(new VecInt(new int[] {-3, -6}));
			s.addClause(new VecInt(new int[] {-4, -5}));
			s.addClause(new VecInt(new int[] {-4, -6}));
			s.addClause(new VecInt(new int[] {-5, -6}));
			s.addClause(new VecInt(new int[] {1,2,3,4,5,6}));
		} catch (ContradictionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constrains the sat solvers for each digit according to the rule that the feedback must match the index of the
	 * correct code when guessed.
	 *
	 * @param guess the array representing the codebreaker's guess.
	 * @param feedback the feedback code.
	 */
	public static void sameColorSamePosConstraint(int[] guess, int[] feedback) {
		if (guess[0] == feedback[0]) {
			try {
				//solverDigOne.reset();
				//defineInitConstraints(solverDigOne);
				solverDigOne.addClause(new VecInt(new int[]{guess[0] + 1}));
			} catch (ContradictionException e) {
				e.printStackTrace();
			}
		}
		if (guess[1] == feedback[1]) {
			try {
				//solverDigTwo.reset();
				//defineInitConstraints(solverDigTwo);
				solverDigTwo.addClause(new VecInt(new int[]{guess[1] + 1}));
			} catch (ContradictionException e) {
				e.printStackTrace();
			}
		}
		if (guess[2] == feedback[2]) {
			try {
				//solverDigThree.reset();
				//defineInitConstraints(solverDigThree);
				solverDigThree.addClause(new VecInt(new int[]{guess[2] + 1}));
			} catch (ContradictionException e) {
				e.printStackTrace();
			}
		}
		if (guess[3] == feedback[3]) {
			try {
				//solverDigFour.reset();
				//defineInitConstraints(solverDigFour);
				solverDigFour.addClause(new VecInt(new int[]{guess[3] + 1}));
			} catch (ContradictionException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Constrains the sat solvers based on the feedback code. That they must contain the same number of correct colors.
	 *
	 * @param guess the code guessed by the codebreaker.
	 * @param feedback the feedback code.
	 */
	public static void otherFeedbackConstraint(int[] guess, int[] feedback) {
		for(int ii = 0; ii < feedback.length; ii++) {
			if(guess[ii] != feedback[ii]) {
				int res = 1 + guess[ii];
				switch (ii) {
					case (0):
						try {
							solverDigOne.addClause(new VecInt(new int[] {- res}));
						} catch (ContradictionException e) {
							e.printStackTrace();
						}
						break;
					case (1):
						try {
							solverDigTwo.addClause(new VecInt(new int[] {- res}));
						} catch (ContradictionException e) {
							e.printStackTrace();
						}
						break;
					case (2):
						try {
							solverDigThree.addClause(new VecInt(new int[] {- res}));
						} catch (ContradictionException e) {
							e.printStackTrace();
						}
						break;
					case (3):
						try {
							solverDigFour.addClause(new VecInt(new int[] { - res}));
						} catch (ContradictionException e) {
							e.printStackTrace();
						}
						break;
					default:
						break;
				}
			}
		}

	}


	/**
	 * The main method for toSAT, which instantiates a ReverseMastermind Game
	 * and solves it. Prints out the hidden code, guesses, feedback, and turn count.
	 *
	 * @param args input to main.
	 */

	public static void main(String[] args) {

		codelen = 4;
        colors = 6;
        ReverseMastermind game = new ReverseMastermind(codelen,colors);

        System.out.println("Correct code:");
        int[] code = game.getCode();
        printArr(code);
        System.out.println();
        System.out.println("Guess:\t\tFeedback:");

		// initialize output from opponent 
		int[] x;
		// start turn count
		int count = 1;
		// initialize first guess 
		int[] g = new int[codelen];
		int dig1 = 0;
		int dig2 = 0;
		int dig3 = 0;
		int dig4 = 0;
		for (int i = 0; i < codelen/2; i++) g[i] = 1;
		g[codelen-1] = colors-1;
		// play that guess
		printArr(g);
		x = game.play(g);
		System.out.print("\t");
		int[] feedback;
		feedback = game.getFeedback(x, g);
		printArr(feedback);
		System.out.println();

		// Initial Constrains
		defineInitConstraints(solverDigOne);
		defineInitConstraints(solverDigTwo);
		defineInitConstraints(solverDigThree);
		defineInitConstraints(solverDigFour);

	    while(x[2] != 1 && count < 8) {
			otherFeedbackConstraint(g, feedback);
			sameColorSamePosConstraint(g, feedback);

			count++;
			// check for satisfying assignment
			try {
            	if (solverDigOne.isSatisfiable() && solverDigTwo.isSatisfiable()
								&& solverDigThree.isSatisfiable() && solverDigFour.isSatisfiable()) {
            		dig1 = cnf2Code(solverDigOne.modelWithInternalVariables());
					dig2 = cnf2Code(solverDigTwo.modelWithInternalVariables());
					dig3 = cnf2Code(solverDigThree.modelWithInternalVariables());
					dig4 = cnf2Code(solverDigFour.modelWithInternalVariables());

            } else {
                System.out.println("Unsatisfiable! ");
                x[2] = 1;
            	}
        	}	 
        	catch (TimeoutException e) {
            	System.out.println("Timeout, sorry!");      
        	}
        	// play next turn
			g[0] = dig1;
			g[1] = dig2;
			g[2] = dig3;
			g[3] = dig4;
			printArr(g);
        	x = game.play(g);
			System.out.print("\t");
			feedback = game.getFeedback(x, g);
			printArr(feedback);
			System.out.println();
		}
		System.out.println("Total number of turns: " + count);
	}
}

	