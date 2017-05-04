package Logic;

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
	private static int determinedCount;

	public static void main(String[] args) {
        System.out.println("This is my Sudoku-Solving Program!!!");

        // Initialize Board
        board = new Spot[BOARD_DIMENSION][BOARD_DIMENSION];
        nPotentialValues = new ArrayList[3][3];
		rPotentialValues = new ArrayList[BOARD_DIMENSION];
		cPotentialValues = new ArrayList[BOARD_DIMENSION];
		determinedCount = 0;

		// Populate each nontant with its potential values.
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				nPotentialValues[x][y] = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
			}
		}

		// Populate each row and column with its potential values.
		for (int i = 0; i < BOARD_DIMENSION; i++) {
			rPotentialValues[i] = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
			cPotentialValues[i] = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
		}

		// Initializing the board with new spots.
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

//		setBoardValue(0, 1, 6);
//		setBoardValue(0, 5, 8);
//		setBoardValue(0, 6, 4);
//
//		setBoardValue(1, 1, 3);
//		setBoardValue(1, 4, 7);
//
//		setBoardValue(2, 5, 3);
//		setBoardValue(2,7,8);
//		setBoardValue(2,8,5);
//
//		setBoardValue(3,5,4);
//		setBoardValue(3, 6,2);
//		setBoardValue(3, 8,1);
//
//		setBoardValue(4, 0, 1);
//		setBoardValue(4,8,3);
//
//		setBoardValue(5,0,7);
//		setBoardValue(5, 2, 2);
//		setBoardValue(5, 3, 9);
//
//		setBoardValue(6, 0, 2);
//		setBoardValue(6, 1, 5);
//		setBoardValue(6, 3, 1);
//
//		setBoardValue(7, 4, 8);
//		setBoardValue(7, 7, 1);
//
//		setBoardValue(8, 2, 8);
//		setBoardValue(8,3, 7);
//		setBoardValue(8, 7, 5);


        printBoard();


		boolean progressMade = true;

		while (progressMade && determinedCount < (BOARD_DIMENSION * BOARD_DIMENSION)) {

			progressMade = false;

			boolean updatedPVs = true;
			while (updatedPVs) {

				updatedPVs = false;
				// Update Potential Values
				for (int i = 0; i < BOARD_DIMENSION; i++) {
					for (int j = 0; j < BOARD_DIMENSION; j++) {
						updatedPVs |= updateSpot(i,j);
						progressMade |= updatedPVs;
					}
				}
			}

			/**
			 *
			 *
			 * 	Do logic on nontants
			 *
			 *
			 */

			// Iterate through nontants
			for (int x = 0; x < 3; x++) {
				for (int y = 0; y < 3; y++) {

					/**
					 * Find any values only in one box
					 * and set that box as determined
					 */
					// Iterate through potential values for given nontant
					for (int k = 0; k < nPotentialValues[x][y].size(); k++) {
						int val = nPotentialValues[x][y].get(k);
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
							determinedCount++;
							progressMade = true;

							nPotentialValues[i / 3][j / 3].remove((Integer) val);
							rPotentialValues[i].remove((Integer) val);
							cPotentialValues[j].remove((Integer) val);
						}
					}

				}
			}

			/**
			 *
			 *
			 * 	Do logic on rows
			 *
			 *
			 */
			for (int i = 0; i < BOARD_DIMENSION; i++) {

				/**
				 * Find any values only in one box
				 * and set that box as determined
				 */
				// Iterate through potential values for given row
				for (int k = 0; k < rPotentialValues[i].size(); k++) {
					int val = rPotentialValues[i].get(k);
					int rowCount = 0;
					int J = 0;
					// Iterate through spots in row
					for (int j = 0; j < BOARD_DIMENSION; j++) {
						if (board[i][j].potentialValues.contains(val)) {
							rowCount++;
						}
					}

					// If there is only one spot with some potential value
					// Set it as actually HAVING that value
					if (rowCount == 1) {
						board[i][J].value = val;
						board[i][J].isDetermined = true;
						determinedCount++;
						progressMade = true;
						nPotentialValues[i / 3][J / 3].remove((Integer) val);
						rPotentialValues[i].remove((Integer) val);
						cPotentialValues[J].remove((Integer) val);
					}
				}
			}

			/**
			 *
			 *
			 * 	Do logic on columns
			 *
			 *
			 */
			for (int j = 0; j < BOARD_DIMENSION; j++) {

				/**
				 * Find any values only in one box
				 * and set that box as determined
				 */
				// Iterate through potential values for given column
				for (int k = 0; k < cPotentialValues[j].size(); k++) {
					int val = cPotentialValues[j].get(k);
					int colCount = 0;
					int I = 0;
					// Iterate through spots in row
					for (int i = 0; i < BOARD_DIMENSION; i++) {
						if (board[i][j].potentialValues.contains(val)) {
							colCount++;
						}
					}

					// If there is only one spot with some potential value
					// Set it as actually HAVING that value
					if (colCount == 1) {
						board[I][j].value = val;
						board[I][j].isDetermined = true;
						determinedCount++;
						progressMade = true;
						nPotentialValues[I / 3][j / 3].remove((Integer) val);
						rPotentialValues[I].remove((Integer) val);
						cPotentialValues[j].remove((Integer) val);
					}
				}
			}

		}



		printBoard();
		System.out.println();

    }
    private static boolean updateSpot(int spotI, int spotJ) {
    	boolean changed = false;

        // Loop through row
        for (int j = 0; j < BOARD_DIMENSION; j++) {
            if (board[spotI][j].isDetermined) {
                if(board[spotI][spotJ].potentialValues.remove((Integer) board[spotI][j].value)) {
                	changed |= true;
				}
            }
        }

        // Loop through column
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            if (board[i][spotJ].isDetermined) {
            	if (board[spotI][spotJ].potentialValues.remove((Integer) board[i][spotJ].value)) {
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
                	if (board[spotI][spotJ].potentialValues.remove((Integer) board[startI + i][startJ + j].value)) {
                		changed |= true;
					}
                }
            }
        }

        // If only has one potential value....set it as determined value
        if (board[spotI][spotJ].potentialValues.size() == 1) {
        	board[spotI][spotJ].value = board[spotI][spotJ].potentialValues.get(0);
        	board[spotI][spotJ].isDetermined = true;
        	determinedCount++;
        	changed |= true;

        	// Update nontant potential values
			nPotentialValues[spotI / 3][spotJ / 3].remove((Integer)board[spotI][spotJ].value);

			// Update row potential values
			rPotentialValues[spotI].remove((Integer)board[spotI][spotJ].value);

			// Update column potential values
			cPotentialValues[spotJ].remove((Integer)board[spotI][spotJ].value);

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
        Spot() {
            this.isDetermined = false;
            this.value = 0; //TBD
            this.potentialValues = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        }

        public void setValue(int value) {
            this.isDetermined = true;
            this.value = value;
            this.potentialValues = new ArrayList<>();
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
    }

}
