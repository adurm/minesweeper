
import java.util.Random;

/**
 * @author Candidate 164656
 * @date Apr 2017
 */
public class Minefield {

    private final Minetile[][] minefield;
    private final int rows, cols, maxMines;

    public Minefield(int rows, int cols, int maxMines) {

        this.rows = rows;
        this.cols = cols;
        this.maxMines = maxMines;
        minefield = new Minetile[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                minefield[i][j] = new Minetile();
            }
        }
    }

    /**
     * @param row location of tile
     * @param col location of tile 
     * Places a mine at this tile and increments the
     * neighbours' mined neighbour count
     */
    public void mineTile(int row, int col) {

        minefield[row][col].setMined();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i != 0 || j != 0)
                        && !isOutOfBounds(row + i, col + j)) {
                    minefield[row + i][col + j].incMinedNb();
                }
            }
        }
    }

    /**
     * @param row location of tile
     * @param col location of tile
     * @return true if tile is out of bounds of the minefield, false if within bounds
     * This method is a test called upon in the mineTile method and it
     * checks to see that the neighbour of the current mine being looked at is
     * within the boundaries of the grid.
     */
    public boolean isOutOfBounds(int row, int col) {

        return row < 0
                || row >= rows
                || col < 0
                || col >= cols;
    }

    /**
     * Generates a random x and y coordinate inside the grid and 
     * puts coordinates into method mineTile to place a mine in the field
     */
    public void populate() {

        Random random = new Random();
        int currentMines = 0;
        //keeps making mines while maxMines hasnt been met
        //or until the board has been filled up with mines other than (0,0)
        while ((currentMines < maxMines) && currentMines < (rows * cols) - 1) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);
            //Mine can't be placed in the first tile in grid, or if there is already a mine there
            if ((x != 0 || y != 0) && !minefield[x][y].isMined()) {
                mineTile(x, y);
                currentMines++;
            }
        }
    }

    /**
     * @param row location of tile
     * @param col location of tile 
     * Toggles the marked status of a tile
     */
    public void mark(int row, int col) {

        minefield[row][col].toggleMarked();
    }

    /**
     * @param row location of tile
     * @param col location of tile 
     * Reveals the tile and calls stepNeighbour to
     * reveal all surrounding tiles if the tile has 0 mined neighbours
     */
    public void step(int row, int col) {

        if (minefield[row][col].isMined()) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (minefield[i][j].isMined()) {
                        minefield[i][j].reveal();
                    }
                }
            }
        } else if (minefield[row][col].getMinedNeighbours() > 0) {
            minefield[row][col].reveal();
        } else if (minefield[row][col].getMinedNeighbours() == 0) {
            minefield[row][col].reveal();
            stepNeighbour(row, col);
        }
    }

    /**
     * @param row location of tile
     * @param col location of tile 
     * Recursively reveals all neighbours which have 0 mined neighbours
     */
    public void stepNeighbour(int row, int col) {

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!isOutOfBounds(row + i, col + j)
                        && !minefield[row + i][col + j].isMarked()) {
                    if (minefield[row + i][col + j].getMinedNeighbours() > 0) {
                        minefield[row + i][col + j].reveal();
                    } else if (minefield[row + i][col + j].getMinedNeighbours() == 0
                            && !minefield[row + i][col + j].isRevealed()) {
                        minefield[row + i][col + j].reveal();
                        stepNeighbour(row + i, col + j);

                    }
                }
            }
        }
    }

    /**
     * @return true if all mines are marked, otherwise false 
     * Checks if all mines have been marked and no tiles are incorrectly marked
     */
    public boolean areAllMinesMarked() {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (minefield[i][j].isMined() && !minefield[i][j].isMarked()
                        || minefield[i][j].isMarked() && !minefield[i][j].isMined()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return true if all non mines are revealed, otherwise false 
     * Checks if all tiles which are not mines have been revealed
     */
    public boolean areAllNonMinesRevealed() {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!minefield[i][j].isMined() && !minefield[i][j].isRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return returns the minefield
     */
    public Minetile[][] getMinefield() {
        return minefield;
    }

    /**
     * @return rows in minefield
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return columns in minefield
     */
    public int getCols() {
        return cols;
    }

    /**
     * @return mines in minefield
     */
    public int getMaxMines() {
        return maxMines;
    }

}
