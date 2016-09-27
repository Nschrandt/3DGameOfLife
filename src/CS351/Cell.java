package CS351;

import javafx.scene.shape.Box;

/**
 * Created by nick on 9/25/16.
 */
public class Cell {

    private Box cellBox;
    private Cell[][][] grid;
    private int xPosition;
    private int yPosition;
    private int zPosition;
    private boolean isAlive;

    public Cell(int x, int y, int z, Cell[][][] cellGrid)
    {
        xPosition = x;
        yPosition = y;
        zPosition = z;
        grid = cellGrid;
    }

    public void setAlive(boolean life)
    {
        isAlive = life;
    }

    public void createBox(double boxWidth, double boxHeight, double boxDepth)
    {
        cellBox = new Box(boxWidth,boxHeight,boxDepth);
    }

    public Box getBox()
    {
        return cellBox;
    }

    public int checkSurroundings()
    {
        int neighbors = 0;
        for(int i = xPosition-1; i < xPosition+2; i++)
        {
            for(int j = yPosition-1; j<yPosition+2; j++)
            {
                for(int k = zPosition-1; k<zPosition+2; k++)
                {
                    if(grid[i][j][k] != null)
                    {
                        neighbors++;
                    }
                }
            }
        }
        return neighbors;
    }

}
