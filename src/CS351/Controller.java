package CS351;

import javafx.event.Event;
import javafx.event.EventHandler;

public class Controller implements EventHandler {

    private double r1;
    private double r2;
    private double r3;
    private double r4;
    private Main source;

    public Controller(Main source)
    {
        this.source = source;
    }

    @Override
    public void handle(Event event) {
        r1 = source.getGUI().getR1();
        r2 = source.getGUI().getR2();
        r3 = source.getGUI().getR3();
        r4 = source.getGUI().getR4();
        source.startSimulation();
    }

}
