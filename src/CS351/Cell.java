package CS351;

import javafx.scene.shape.Box;

/**
 * Created by nick on 9/25/16.
 */
public class Cell {

    private Box cellBox;

    public Box createBox(){
        cellBox = new Box(1,1,1);
        return cellBox;
    }

}
