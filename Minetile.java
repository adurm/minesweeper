
/**
 * @author Candidate 164656
 * @date Apr 2017
 */
public class Minetile {

    private boolean mined = false;
    private boolean revealed = false;
    private boolean marked = false;
    private int minedNeighbours = 0;

    /**
     * @return whether or not the tile contains a mine
     */
    public boolean isMined() {
        return mined;
    }

    /**
     * Inserts a mine in that tile
     */
    public void setMined() {
        mined = true;
    }

    /**
     * @return whether or not the tile is revealed
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Reveals the contents of the tile
     */
    public void reveal() {
        
        revealed = true;
    }
    
    /**
     * @return whether or not the tile is marked
     */
    public boolean isMarked() {
        return marked;
    }

    /**
     * Toggles if the tile is marked or not
     */
    public void toggleMarked() {
        marked = !marked;
    }

    /**
     * Increases the number of mined neighbours the tile has
     */
    public void incMinedNb() {
        minedNeighbours++;
    }

    /**
     * @return the number of mines next to the tile
     */
    public int getMinedNeighbours() {
        return minedNeighbours;
    }
    
}
