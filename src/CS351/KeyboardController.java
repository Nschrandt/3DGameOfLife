package CS351;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author Nick Schrandt
 *
 * Very simple class created in the Main class that allows for the user to zoom in and out, as well as restart the
 * simulation.
 */
public class KeyboardController implements EventHandler<KeyEvent>
{
    /**Main object that created this object*/
    private Main source;

    /**
     * Constructor that simply sets its source.
     * @param source Main object from where this was created.
     */
    public KeyboardController(Main source)
    {
        this.source = source;
    }

    /**
     * Override of the handle method. Calls zoom() with the proper parameters or stopSimulation() in the Main object
     * @param event key pressed by user.
     */
    @Override
    public void handle(KeyEvent event) {
        if(event.getCode() == KeyCode.UP)
        {
            source.zoom(5);
        }
        if(event.getCode() == KeyCode.DOWN)
        {
            source.zoom(-5);
        }
        if(event.getCode() == KeyCode.R)
        {
            source.stopSimulation();
        }
    }
}
