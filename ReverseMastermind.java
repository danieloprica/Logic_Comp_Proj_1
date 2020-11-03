import java.util.Arrays;


/** Represents a Reverse Mastermind game which containing a code and methods to provide feedback on the guess.
 */
public class ReverseMastermind {
  private final int length;
  private final int colors;
  private static int[] code;

  /**
   * Constructor for Reverse Mastermind with a randomized code.
   *
   * @param length the code length.
   * @param colors the number of colors a code can be made from.
   */
  public ReverseMastermind(int length, int colors) {
    code = new int[length];
    this.length = length;
    this.colors = colors;

    for (int i = 0; i < length; i++) {
      code[i] = (int) (colors * Math.random());
    }
  }


  /**
   * Returns the hidden code in the Reverse Mastermind game.
   *
   * @return the hidden code.
   */
  public int[] getCode() {
    return code;
  }

  /**
   * Author: Natalie Collina May 2018
   * Returns the number of occurrences of a certain color in the input array (code).
   *
   * @param arr  the code in which to determine the frequency of a color.
   * @param test the color of which to determine the frequency.
   * @return the frequency of test in the array arr.
   */
  private int freq(int[] arr, int test) {
    int ret = 0;
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == test) ret++;
    }
    return ret;
  }

  /**
   * Author: Natalie Collina May 2018
   * Returns the feedback of black and white pegs for a guess.
   *
   * @param guess a guessed code played by the codebreaker.
   * @return an array representing the feedback.
   */
  public int[] play(int[] guess) {
    // the first value represents the black pegs
    // the second value represents the white pegs
    // the third value is 0 when game is ongoing and 1 if player one wins
    int[] score = {0, 0, 0};
    if (guess.length != length) {
      System.out.println("Incorrect input format. Please try again." + guess.length + " " + length);
      return score;
    }
    // count the number of black pegs
    for (int i = 0; i < guess.length; i++) {
      if (guess[i] == code[i]) score[0]++;
    }

    // for each color, count the number of colors that each has to get the white peg value
    for (int c = 0; c < colors; c++) {
      score[1] += Math.min(freq(guess, c), freq(code, c));
    }

    // subtract black peg value to get true white peg value
    score[1] -= score[0];

    if (score[0] == length) score[2] = 1;
    return score;
  }

  /**
   * Returns the feedback in the form of a code that scores the same as the guess. If the
   * guess contains a correct digit in the correct index the feedback must reflect this.
   *
   * @param score the score of the guess.
   * @param guess the guess the codebreaker provided.
   * @return the feedback from the coder (a sequence that has the same black and white pegs).
   */
  public int[] getFeedback(int[] score, int[] guess) {
    int status = 1;
    int[] feedback = new int[length];
    //continue until valid feedback is ensured.
    while (status == 1) {
      for (int ii = 0; ii < length; ii++) {
        if (guess[ii] == code[ii]) {
          feedback[ii] = guess[ii];//constraint of containing same correct digit in position.
        } else {
          feedback[ii] = (int) (Math.random() * colors);//randomly create feedback
          while (feedback[ii] == guess[ii])
            feedback[ii] = (int) (Math.random() * colors);
        }
      }
      int[] newScore = play(feedback);
      if (Arrays.equals(score, newScore)) {
        status = 0;
      }
    }
    return feedback;
  }
}


