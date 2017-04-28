import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by connor on 2/11/17.
 */
public class Solver {

    private static final int BOARD_DIMENSION = 9;
    private static Spot[][] board;
    private static ArrayList<Integer>[][] nPotentialValues;
	private static ArrayList<Integer>[] rPotentialValues;
	private static ArrayList<Integer>[] cPotentialValues;

	public static void main(String[] args) {
        System.out.println("This is my Sudoku-Solving Program!!!");
        // Initialize Board
        board = new Spot[BOARD_DIMENSION][BOARD_DIMENSION];
        nPotentialValues = new ArrayList[3][3];
		rPotentialValues = new ArrayList[BOARD_DIMENSION];
		cPotentialValues = new ArrayList[BOARD_DIMENSION];
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				nPotentialValues[x][y] = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
			}
		}
		for (int i = 0; i < BOARD_DIMENSION; i++) {
			rPotentialValues[i] = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
			cPotentialValues[i] = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
		}

		for (int i = 0 ; i < BOARD_DIMENSION; i++) {
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                board[i][j] = new Spot();
            }
        }
        setBoardValue(0,2,7);
        setBoardValue(0,7,1);
        setBoardValue(0,8,5);

        setBoardValue(1,3,3);
        setBoardValue(1,4,9);
        setBoardValue(1,5,7);

        setBoardValue(2,1,6);
        setBoardValue(2,2,2);
        setBoardValue(2,4,1);
        setBoardValue(2,6,4);
        setBoardValue(2,8,9);

        setBoardValue(3,1,2);
        setBoardValue(3,5,1);
        setBoardValue(3,6,5);
        setBoardValue(3,7,4);
        setBoardValue(3,8,3);

        setBoardValue(4,0,7);
        setBoardValue(4,3,4);
        setBoardValue(4,5,9);
        setBoardValue(4,8,1);

        setBoardValue(5,0,4);
        setBoardValue(5,1,8);
        setBoardValue(5,2,1);
        setBoardValue(5,3,2);
        setBoardValue(5,7,6);

        setBoardValue(6,0,9);
        setBoardValue(6,2,6);
        setBoardValue(6,4,2);
        setBoardValue(6,6,7);
        setBoardValue(6,7,3);

        setBoardValue(7,3,9);
        setBoardValue(7,4,8);
        setBoardValue(7,5,4);

        setBoardValue(8,0,1);
        setBoardValue(8,1,5);
        setBoardValue(8,6,2);


        printBoard();

        boolean progressMade = true;

        while (progressMade) {

        	progressMade = false;
			// Update Potential Values
			for (int i = 0; i < BOARD_DIMENSION; i++) {
				for (int j = 0; j < BOARD_DIMENSION; j++) {
					progressMade |= updateSpot(i,j);
				}
			}
		}



        // Set values on nontant


		// Iterate through nontants
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {


				/**
				 * Find any boxes with only one potential value
				 * and set them as determined
				 */
				// Iterate through spots in nontant
				for (int m = 0; m < 3; m++) {
					for (int n = 0; n < 3; n++) {
						if (board[x * 3 + m][y * 3 + n].potentialValues.size() == 1) {
							board[x * 3 + m][y * 3 + n].value = board[x * 3 + m][y * 3 + n].potentialValues.get(0);
							board[x * 3 + m][y * 3 + n].isDetermined = true;


							nPotentialValues[x][y].remove(board[x * 3 + m][y * 3 + n].value);
							rPotentialValues[x * 3 + m].remove(board[x * 3 + m][y * 3 + n].value);
							cPotentialValues[y * 3 + n].remove(board[x * 3 + m][y * 3 + n].value);
						}
					}
				}

				/**
				 * Find any values only in one box
				 * and set that box as determined
				 */
				// Iterate through potential values for given nontant
				for (int val: nPotentialValues[x][y]) {
					int count = 0;
					int i = 0;
					int j = 0;
					// Iterate through spots in nontant
					for (int m = 0; m < 3; m++) {
						for (int n = 0; n < 3; n++) {
							if (board[x * 3 + m][y * 3 + n].potentialValues.contains(val)) {
								count++;
								i = x * 3 + m;
								j = y * 3 + n;
							}
						}
					}

					// If there is only one spot with some potential value
					// Set it as actually HAVING that value
					if (count == 1) {
						board[i][j].value = val;
						board[i][j].isDetermined = true;

						nPotentialValues[i / 3][j / 3].remove(val);
						rPotentialValues[i].remove(val);
						cPotentialValues[j].remove(val);
					}
				}

			}
		}

    }
    private static boolean updateSpot(int spotI, int spotJ) {
    	boolean changed = false;
        // Loop through row
        for (int j = 0; j < BOARD_DIMENSION; j++) {
            if (board[spotI][j].isDetermined) {
                if(board[spotI][spotJ].potentialValues.remove(board[spotI][j].value) != null) {
                	changed |= true;
				}
            }
        }

        // Loop through column
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            if (board[i][spotJ].isDetermined) {
            	if (board[spotI][spotJ].potentialValues.remove(board[i][spotJ].value) != null) {
            		changed |= true;
				}
            }
        }

        // Loop through nontant
        int startI = (spotI / 3) * 3;
        int startJ = (spotJ / 3) * 3;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (board[startI + i][startJ + j].isDetermined) {
                	if (board[spotI][spotJ].potentialValues.remove(board[startI + i][startJ + j].value) != null) {
                		changed |= true;
					}
                }
            }
        }

        // If only has one potential value....set it as determined value
        if (board[spotI][spotJ].potentialValues.size() == 1) {
        	board[spotI][spotJ].value = board[spotI][spotJ].potentialValues.get(0);
        	board[spotI][spotJ].isDetermined = true;
        	changed |= true;

        	// Update nontant potential values
			nPotentialValues[spotI / 3][spotJ / 3].remove(board[spotI][spotJ].value);

			// Update row potential values
			rPotentialValues[spotI].remove(board[spotI][spotJ].value);

			// Update column potential values
			cPotentialValues[spotJ].remove(board[spotI][spotJ].value);

		}
		return changed;
    }


    private static void printBoard() {
        System.out.println("╔═════╦═════╦═════╗");
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            if (i == 3 || i == 6)
                System.out.println("╠═════╬═════╬═════╣");
            System.out.print("║");

            for (int j = 0; j < BOARD_DIMENSION; j++) {
                if (j != 0)
                    if (j == 3 || j == 6)
                        System.out.print("║");
                    else
                        System.out.print("|");
                System.out.print(board[i][j]);

            }
            System.out.println("║");
        }
        System.out.println("╚═════╩═════╩═════╝");
    }


    private static class Spot {
        boolean isDetermined;
        int value;
        ArrayList<Integer> potentialValues;
        Spot (int value) {
            this.isDetermined = true;
            this.value = value;
            this.potentialValues = null;
        }
        Spot() {
            this.isDetermined = false;
            this.value = 0; //TBD
            this.potentialValues = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        }

        public void setValue(int value) {
            this.isDetermined = true;
            this.value = value;
            this.potentialValues = null;
        }

        @Override
        public String toString() {
            if (isDetermined)
                return value + "";
            else
                return " ";
        }
    }

    private static void setBoardValue(int iPos, int jPos, int value) {
        board[iPos][jPos].setValue(value);
//        for (int i = 0; i < BOARD_DIMENSION; i++) {
//            if (!board[i][jPos].isDetermined)
//                board[i][jPos].potentialValues.remove(value);
//        }
//        for (int j = 0; j < BOARD_DIMENSION; j++) {
//            if (!board[iPos][j].isDetermined)
//                board[iPos][j].potentialValues.remove(value);
//        }
    }

}
