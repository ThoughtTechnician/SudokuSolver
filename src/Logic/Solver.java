package Logic;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by connor on 2/11/17.
 */
public class Solver {

    private static final int BOARD_DIMENSION = 9;
    private Spot[][] board;
    private ArrayList<Integer>[][] nPotentialValues;
	private ArrayList<Integer>[] rPotentialValues;
	private ArrayList<Integer>[] cPotentialValues;
	private int determinedCount;

	public Solver() {
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
	}
	public void solve() {

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
			 * 	Do logic on nontants
			 */

			// Iterate through nontants
			for (int x = 0; x < 3; x++) {
				for (int y = 0; y < 3; y++) {

					// Iterate through potential values for given nontant
					for (int k = 0; k < nPotentialValues[x][y].size(); k++) {
						progressMade |= nontantSingleValueCheck(x, y, k);
					}
				}
			}

			/**
			 * 	Do logic on rows
			 */
			for (int i = 0; i < BOARD_DIMENSION; i++) {

				// Iterate through potential values for given row
				for (int k = 0; k < rPotentialValues[i].size(); k++) {
					progressMade |= rowSingleValueCheck(i, k);
				}
			}

			/**
			 * 	Do logic on columns
			 */
			for (int j = 0; j < BOARD_DIMENSION; j++) {

				// Iterate through potential values for given column
				for (int k = 0; k < cPotentialValues[j].size(); k++) {
					progressMade |= columnSingleValueCheck(j, k);
				}
			}

		}
	}

    /**
     * method to iterate through all spots and eliminate potential values based on the values
     * that are present on the board
     * @param spotI - row of the spot to check
     * @param spotJ - column of the spot to check
     * @return - boolean value detecting if progress was made in any respect
     */
    private boolean updateSpot(int spotI, int spotJ) {
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

    /**
     * method which detects if a particular potential value exists in only one box of a nontant
     * @param row - row of nontant
     * @param col - column on nontant
     * @param value - value being checked within potential values list
     * @return boolean detecting if progress was made
     */
    private boolean nontantSingleValueCheck(int row, int col, int value) {
        int val = nPotentialValues[row][col].get(value);
        int count = 0;
        int i = 0;
        int j = 0;
        boolean progress = false;

        // Iterate through spots in nontant
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                if (board[row * 3 + m][col * 3 + n].potentialValues.contains(val)) {
                    count++;
                    i = row * 3 + m;
                    j = col * 3 + n;
                }
            }
        }

        // If there is only one spot with some potential value
        // Set it as actually HAVING that value
        if (count == 1) {
            board[i][j].value = val;
            board[i][j].isDetermined = true;
            determinedCount++;
            progress = true;

            nPotentialValues[i / 3][j / 3].remove((Integer) val);
            rPotentialValues[i].remove((Integer) val);
            cPotentialValues[j].remove((Integer) val);
        }

        return progress;
    }

    /**
     * method which detects if a particular potential value exists in only one box of a row
     * @param row - row to iterate through
     * @param value - value being checked within the potential values list
     * @return - boolean detecting if progress was made
     */
    private boolean rowSingleValueCheck(int row, int value) {
        int val = rPotentialValues[row].get(value);
        int rowCount = 0;
        int J = 0;
        boolean progress = false;

        // Iterate through spots in row
        for (int j = 0; j < BOARD_DIMENSION; j++) {
            if (board[row][j].potentialValues.contains(val)) {
                rowCount++;
            }
        }

        // If there is only one spot with some potential value
        // Set it as actually HAVING that value
        if (rowCount == 1) {
            board[row][J].value = val;
            board[row][J].isDetermined = true;
            determinedCount++;
            progress = true;
            nPotentialValues[row / 3][J / 3].remove((Integer) val);
            rPotentialValues[row].remove((Integer) val);
            cPotentialValues[J].remove((Integer) val);
        }
        return progress;
    }

    /**
     * method which detects if a particular potential value exists in only one box of a row
     * @param col - column to iterate through
     * @param value - value being checked within the potential values list
     * @return - boolean detecting if progress was made
     */
    private boolean columnSingleValueCheck(int col, int value) {
        int val = cPotentialValues[col].get(value);
        int colCount = 0;
        int I = 0;
        boolean progress = false;

        // Iterate through spots in row
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            if (board[i][col].potentialValues.contains(val)) {
                colCount++;
            }
        }

        // If there is only one spot with some potential value
        // Set it as actually HAVING that value
        if (colCount == 1) {
            board[I][col].value = val;
            board[I][col].isDetermined = true;
            determinedCount++;
            progress = true;
            nPotentialValues[I / 3][col / 3].remove((Integer) val);
            rPotentialValues[I].remove((Integer) val);
            cPotentialValues[col].remove((Integer) val);
        }
        return progress;
    }

    /**
     * method to print out the layout of the board
     */
    private void printBoard() {
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

    /**
     * Class which contains the spot
     * Spots contain:
     * isDetermined - boolean which identifies if a spot has a value
     * value - the actual value of the spot, undefined if not determined
     * potentialValues - list of possible values for this spot
     */
    public class Spot {
        boolean isDetermined;
        public int value;
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

    private void setBoardValue(int iPos, int jPos, int value) {
        board[iPos][jPos].setValue(value);
    }

    public Spot[][] getBoard() {
    	return board;
	}

}
