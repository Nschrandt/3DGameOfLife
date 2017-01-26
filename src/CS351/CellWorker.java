package CS351;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;


/**
 * Created by Nick on 1/24/2017.
 */
public class CellWorker extends Thread {
    private int startY;
    private int endY;
    private Main main;
    private Cell[][][] currentGrid;
    private Cell[][][] nextGrid;
    private Xform cellXform;
    private boolean isRunning;
    private boolean isPaused = false;
    private boolean isReady = false;
    private double lifePopHigh;
    private double lifePopLow;
    private double deathPopLow;
    private double deathPopHigh;
    /**Width of a cell*/
    private final double cellWidth = 1.0;
    /**Height of a cell*/
    private final double cellHeight = 1.0;
    /**Depth of a cell*/
    private final double cellDepth = 1.0;

    public CellWorker(Main main, int start, int end, double lowDeath, double highDeath, double lowBirth,
                      double highBirth)
    {
        this.main = main;
        this.startY = start;
        this.endY = end;
        this.lifePopHigh = highBirth;
        this.lifePopLow = lowBirth;
        this.deathPopHigh = highDeath;
        this.deathPopLow = lowDeath;
        isRunning = true;
    }

    @Override
    public void run()
    {
        while(isRunning)
        {
            if(isPaused)
            {
                try {
                    sleep(100);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!isReady)
            {
                updateGrid();
                System.out.println("THIS");
                isReady = true;
            }
            try{
                sleep(10);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private boolean checkCell(Cell cell)
    {
        if(cell.getY() >= startY && cell.getY() <= endY)
        {
            return true;
        }
        return false;
    }

    /**
     * This method is called every second. It loops through the 3D grid and checks every position against the
     * parameters passed by the user, or stored in the preset simulation. If a space has enough nieghbors to become
     * a cell, a new cell is created and placed in a new copy of the grid and a new copy of the Xform. It's also
     * added to a list of livingCells so that it can be animated.
     *
     * It also checks if a cell needs to die. If so, it removes it from the copy of the currentCellGrid and adds it to
     * the dying list so it can be animated. It does not remove it from the Xform, as it will need to finish its
     * animation.
     *
     * Finally if a cell doesn't need to die or come to life, it is simply copied into the new grid and Xform.
     * @return returns a new Xform with the updated Cells
     *
     *
     */
    private void updateGrid()
    {
        PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.DARKGREEN);
        for(int i = 1; i<31; i++)
        {
            for(int j = 1; j < 31; j++)
            {
                for(int k = startY; k <= endY; k++)
                {
                    int neighbors = checkSurroundings(i,j,k);
                    if(currentGrid[i][j][k] == null && neighbors <= lifePopHigh && neighbors >= lifePopLow)
                    {
                        Cell newCell = new Cell(cellWidth,cellHeight,cellDepth, k);
                        nextGrid[i][j][k] = newCell;
                        newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                                k * cellDepth - (15 * cellDepth));
                        //cellXform.getChildren().add(newCell);
                        //main.addLiving(newCell);
                    }
                    else if(currentGrid[i][j][k] != null && (neighbors < deathPopLow || neighbors > deathPopHigh))
                    {
                        //cellXform.getChildren().add(currentGrid[i][j][k]);
                        //main.addDying(currentGrid[i][j][k]);
                        nextGrid[i][j][k] = null;
                    }
                    else if(currentGrid[i][j][k] != null && !(neighbors < deathPopLow || neighbors > deathPopHigh))
                    {
                        //cellXform.getChildren().add(currentGrid[i][j][k]);
                        nextGrid[i][j][k] = currentGrid[i][j][k];
                    }
                }
            }
        }
    }

    /**
     * This method is called to check how many neighbors it has so it can be checked against the parameters for the
     * simulation. It will not count itself.
     *
     * @param x x value in the 3D cell grid
     * @param y y value in the 3D cell grid
     * @param z z value in the 3D cell grid
     * @return number of beighbors around the given space.
     *
     */
    private int checkSurroundings(int x, int y, int z)
    {
        int neighbors = 0;
        for(int i = x-1; i < x+2; i++)
        {
            for(int j = y-1; j<y+2; j++)
            {
                for(int k = z-1; k<z+2; k++)
                {
                    if(currentGrid[i][j][k] != null && !(i == x && j == y && k ==z))
                    {
                        neighbors++;
                    }
                }
            }
        }
        return neighbors;
    }

    public boolean isReady()
    {
        if(isReady)
        {
            return true;
        }
        return false;
    }


    public void gridUpdated(Cell[][][] currentGrid, Cell[][][] nextGrid, Xform cellXform)
    {
        this.currentGrid = currentGrid;
        this.cellXform = cellXform;
        this.nextGrid = nextGrid;
        isReady = false;
    }

//    public void secondTick()
//    {
//        this.tick = true;
//    }

    public void stopRunning()
    {
        this.isRunning = false;
    }
}
