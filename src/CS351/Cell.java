package CS351;

import javafx.scene.shape.Box;

/**
 * Created by nick on 9/25/16.
 */
public class Cell extends Xform{

    private Box cellBox;

    private boolean isAlive;

    public Cell(double boxWidth, double boxHeight, double boxDepth)
    {
        cellBox = new Box(boxWidth,boxHeight,boxDepth);
        this.getChildren().add(cellBox);
    }

    public void setAlive(boolean life)
    {
        isAlive = life;
    }

    public boolean isAlive()
    {
        return isAlive;
    }

    public Box getBox()
    {
        return cellBox;
    }
}
