package CS351;

import javafx.scene.shape.Box;

/**
 * Created by nick on 9/25/16.
 */
public class Cell extends Xform{

    private Box cellBox;
    private Cell[][][] grid;
    private boolean isAlive;

    public Cell(int x, int y, int z, Cell[][][] cellGrid)
    {
        grid = cellGrid;
    }

    public void setAlive(boolean life)
    {
        isAlive = life;
    }

    public boolean isAlive()
    {
        return isAlive;
    }

    public void createBox(double boxWidth, double boxHeight, double boxDepth)
    {
        cellBox = new Box(boxWidth,boxHeight,boxDepth);
    }

    public Box getBox()
    {
        return cellBox;
    }

    public int checkSurroundings(int x, int y, int z)
    {
        int neighbors = 0;
        for(int i = x-1; i < x+2; i++)
        {
            for(int j = y-1; j<y+2; j++)
            {
                for(int k = z-1; k<z+2; k++)
                {
                    //System.out.println(i + " " + j + " " + k + ": " + grid[i][j][k]);
                    if(grid[i][j][k] != null && grid[i][j][k].isAlive()
                            && !(i == x && j == y && k ==z))
                    {
                        neighbors++;
                    }
                }
            }
        }
        return neighbors;
    }

}
