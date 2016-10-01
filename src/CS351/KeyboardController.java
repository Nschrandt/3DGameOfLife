package CS351;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by Nick on 9/30/2016.
 */
public class KeyboardController implements EventHandler<KeyEvent>
{
    private Main source;

    public KeyboardController(Main source)
    {
        this.source = source;
    }

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
