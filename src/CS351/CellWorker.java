package CS351;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;


/**This class is created from the main class to make a number of CellWorker threads to manage the logic
 * of the game while the main thread deals with animation using the Observer Pattern. Every second, the main
 * thread alerts any instances of this class that there is a new grid. Each CellWorker then goes through that
 * grid and updates a preset section of the grid. When it is finished, it sets isReady to true and at the start
 * of the next second, the main thread swaps in the newly updated thread and the process is repeated.
 *
 * Created by Nick on 1/24/2017.
 */
public class CellWorker extends Thread {
    /**
     * The y position in the grid where the thread begins updating, inclusive
     */
    private int startY;
    /**
     * the y position in the grid where the thread stops updating, inclusive
     */
    private int endY;
    /**
     * The cell grid that is currently active and animating. This is not changed by the CellWorker threads
     */
    private Cell[][][] currentGrid;
    /**
     * The next grid to be shown. This is the one updated by the CellWorker threads.
     */
    private Cell[][][] nextGrid;
    /**
     * Boolean to control the main loop
     */
    private boolean isRunning;
    /**
     * Checks if the simulation is paused. NOT YET IMPLEMENTED
     */
    private boolean isPaused = false;
    /**
     * Boolean to indicate if the thread is done updating the next grid
     */
    private boolean isReady = false;
    /**
     * Parameter for checking the cells for the new grid.
     */
    private double lifePopHigh;
    /**
     * Parameter for checking the cells for the new grid.
     */
    private double lifePopLow;
    /**
     * Parameter for checking the cells for the new grid.
     */
    private double deathPopLow;
    /**
     * Parameter for checking the cells for the new grid.
     */
    private double deathPopHigh;
    /**
     * Width of a cell
     */
    private final double cellWidth = 1.0;
    /**
     * Height of a cell
     */
    private final double cellHeight = 1.0;
    /**
     * Depth of a cell
     */
    private final double cellDepth = 1.0;

    /**
     * Constructor for the CellWorker thread.
     *
     * @param start     Y position where this thread starts updating.
     * @param end       Y position where this thread stops updating
     * @param lowDeath  low population death
     * @param highDeath high population death
     * @param lowBirth  low population birth
     * @param highBirth high population birth
     */
    public CellWorker(int start, int end, double lowDeath, double highDeath, double lowBirth,
                      double highBirth) {
        this.startY = start;
        this.endY = end;
        this.lifePopHigh = highBirth;
        this.lifePopLow = lowBirth;
        this.deathPopHigh = highDeath;
        this.deathPopLow = lowDeath;
        isRunning = true;
    }

    public void setRValues(double lowL, double highL, double lowD, double highD)
    {
        lifePopLow = lowL;
        lifePopHigh = highL;
        deathPopHigh = highD;
        deathPopLow = lowD;
    }

    /**
     * Main run method. Checks to see if the main thread set isReady to false and then updates it's section
     * of the new grid. Otherwise, waits.
     */
    @Override
    public void run() {
        while (isRunning) {
            if (isPaused) {
                try {
                    sleep(100);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!isReady) {
                updateGrid();
                isReady = true;
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to check if the CellWorker thread has finished updating it's part of the grid. Called by main method at
     * the start of each second tick.
     * @return boolean
     */
    public boolean isReady()
    {
        if(isReady)
        {
            return true;
        }
        return false;
    }

    /**Method called by the main thread to let the CellWorker know that it needs to update its section of the new
     * grid. Sets isReady to false and passes the current grid being animated as well as a blank new grid.
     *
     * @param currentGrid the current grid in the simulation
     * @param nextGrid a blank grid to be updated by the threads.
     */
    public void gridUpdated(Cell[][][] currentGrid, Cell[][][] nextGrid)
    {
        this.currentGrid = currentGrid;
        this.nextGrid = nextGrid;
        isReady = false;
    }

    /**
     * Stops the thread.
     */
    public void stopRunning()
    {
        this.isRunning = false;
    }

    /*This method is called every second when the main thread tells the CellWorker thread that a new grid needs
     * to be generated. It uses the old grid, and the parameters set in the main thread to update each position
     * in it's section of the grid.
     *
     * A live cell with a population above deathPopHigh or below deathPopLow will not be added to the next grid. A dead
     * cell that is surrounded by at least lifePopLow and at most lifePopHigh will come to life in the next grid.
     *
     * This method does NOT add anything to the cell xform, which is what's actually rendered. This grid is used by the
     * main thread to update the Xform.
     *
     */
    private void updateGrid() {
        PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.DARKGREEN);
        for (int i = 1; i < 31; i++) {
            for (int j = 1; j < 31; j++) {
                for (int k = startY; k <= endY; k++) {
                    int neighbors = checkSurroundings(i, j, k);
                    if (currentGrid[i][j][k] == null && neighbors <= lifePopHigh && neighbors >= lifePopLow) {
                        Cell newCell = new Cell(cellWidth, cellHeight, cellDepth);
                        nextGrid[i][j][k] = newCell;
                        newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                                k * cellDepth - (15 * cellDepth));
                    } else if (currentGrid[i][j][k] != null && (neighbors < deathPopLow || neighbors > deathPopHigh)) {
                        nextGrid[i][j][k] = null;
                    } else if (currentGrid[i][j][k] != null && !(neighbors < deathPopLow || neighbors > deathPopHigh)) {
                        nextGrid[i][j][k] = currentGrid[i][j][k];
                    }
                }
            }
        }
    }

    /*
     * This method is called to check how many neighbors each cell has so it can be checked against the parameters for the
     * simulation. A cell will not count itself.
     *
     * @param x x value in the 3D cell grid
     * @param y y value in the 3D cell grid
     * @param z z value in the 3D cell grid
     * @return number of neighbors around the given space.
     *
     */
    private int checkSurroundings(int x, int y, int z) {
        int neighbors = 0;
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                for (int k = z - 1; k < z + 2; k++) {
                    if (currentGrid[i][j][k] != null && !(i == x && j == y && k == z)) {
                        neighbors++;
                    }
                }
            }
        }
        return neighbors;
    }
}
