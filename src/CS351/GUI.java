package CS351;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;

/**
 * @author Nick Schrandt
 *
 * This class creates the GUI at the start of the animation that allows the user to select a simulation type.
 */
public class GUI
{
    /**Scene to hold the GUI elements*/
    private Scene startScene;
    /**Button to start the random Simulation*/
    private Button randomStartButton;
    /**layout for the startScene*/
    private BorderPane layout;
    /**Member class that implements EventHandler to react to user input*/
    private Controller controller = new Controller();
    /**slider to select the parameter for lower death bound*/
    private Slider lowPopDeathSlider;
    /**slider to select the parameter for upper death bound*/
    private Slider highPopDeathSlider;
    /**slider to select the parameter for lower life bound*/
    private Slider lowPopLifeSlider;
    /**slider to select the parameter for upper life bound*/
    private Slider highPopLifeSlider;
    /**Array of buttons that allow the user to select one of the preset simulations*/
    private Button[] presetButtons = new Button[5];
    /**Reference to the Main.java that created this GUI*/
    private Main source;

    /**
     * Constructor method. Sets the source object that created it.
     * @param source the Main class that created this GUI
     */
    public GUI(Main source)
    {
        this.source = source;
    }

    /**
     * Method that creates the opening scene for the program. Sets the layout, the scene, calls all of the GUI
     * creationg methods and then returns the Scene back to the Main class to be displayed.
     * @return Scene for the initial GUI
     */
    public Scene createStartScene()
    {
        layout = new BorderPane();
        startScene = new Scene(layout, 1024, 768, true);

        randomStartButton = new Button("Start Random Simulation");
        randomStartButton.setOnAction(controller);
        layout.setTop(randomStartButton);

        GridPane sliderWindow = createWindow();
        layout.setCenter(sliderWindow);
        GridPane presetWindow = createPresets();
        layout.setBottom(presetWindow);

        return startScene;
    }

    /**
     * Allows the Main to retrieve whatever value the slider is on.
     * @return the slider value.
     */
    public double getLowPopDeath()
    {
        return lowPopDeathSlider.getValue();
    }

    /**
     * Allows the Main to retrieve whatever value the slider is on.
     * @return the slider value.
     */
    public double getHighPopDeath()
    {
        return highPopDeathSlider.getValue();
    }

    /**
     * Allows the Main to retrieve whatever value the slider is on.
     * @return the slider value.
     */
    public double getLowPopLife()
    {
        return lowPopLifeSlider.getValue();
    }

    /**
     * Allows the Main to retrieve whatever value the slider is on.
     * @return the slider value.
     */
    public double getHighPopLife()
    {
        return highPopLifeSlider.getValue();
    }

    /**
     * Creates a GridPane with all of the sliders that allow the user to select the parameters for the random
     * simulation.
     * @return The GridPane with all of the preset buttons
     */
    private GridPane createPresets()
    {
        GridPane presetWindow = new GridPane();
        for(int i = 0; i < 5; i++)
        {
            presetButtons[i] = new Button();
            Button newButton = presetButtons[i];
            newButton.setOnAction(controller);
            GridPane.setConstraints(newButton,i, 0);
            presetWindow.getChildren().add(newButton);
        }
        presetButtons[0].setText("Tower of Life and Death");
        presetButtons[1].setText("Pillars of Creation");
        presetButtons[2].setText("The Cubic Cycle");
        presetButtons[3].setText("The Multi-Cross");
        presetButtons[4].setText("The Eye of Zeus");
        return presetWindow;
    }

    /**
     * Method that creates and populates the window for all of the sliders as well as the instructions.
     * @return the GridPane with the sliders.
     */
    private GridPane createWindow()
    {
        GridPane window = new GridPane();
        lowPopDeathSlider = createLowSlider();
        lowPopDeathSlider.setValue(2);
        highPopDeathSlider = createHighSlider();
        lowPopLifeSlider = createLowSlider();
        lowPopLifeSlider.setValue(4);
        highPopLifeSlider = createHighSlider();
        Label label1 = new Label("Low Population Death: ");
        Label label2 = new Label("Over Population Death: ");
        Label label3 = new Label("Low Population Birth: ");
        Label label4 = new Label("High Population Birth: ");

        GridPane.setConstraints(label1, 0, 0);
        window.getChildren().add(label1);
        GridPane.setConstraints(lowPopDeathSlider, 1, 0);
        window.getChildren().add(lowPopDeathSlider);

        GridPane.setConstraints(label2,0,1);
        window.getChildren().add(label2);
        GridPane.setConstraints(highPopDeathSlider, 1, 1);
        window.getChildren().add(highPopDeathSlider);

        GridPane.setConstraints(label3, 0, 2);
        window.getChildren().add(label3);
        GridPane.setConstraints(lowPopLifeSlider, 1, 2);
        window.getChildren().add(lowPopLifeSlider);

        GridPane.setConstraints(label4, 0, 3);
        window.getChildren().add(label4);
        GridPane.setConstraints(highPopLifeSlider, 1, 3);
        window.getChildren().add(highPopLifeSlider);

        Label instructions = new Label("Press R during simulation to restart and press Up or Down to zoom.");
        GridPane.setConstraints(instructions, 0, 6);
        window.getChildren().add(instructions);

        return window;
    }

    /**
     * Creates and sets up a slider with a lower value.
     * @return lower-value slider.
     */
    private Slider createLowSlider()
    {
        Slider newSlider = new Slider(1,26,3);
        newSlider.setMajorTickUnit(1);
        newSlider.setMinorTickCount(0);
        newSlider.setSnapToTicks(true);
        newSlider.setShowTickMarks(true);
        newSlider.setShowTickLabels(true);
        newSlider.setPrefWidth(400);
        return newSlider;
    }

    /**
     * Creates and sets up a slider with a higher initial value.
     * @return Slider
     */
    private Slider createHighSlider()
    {
        Slider newSlider = new Slider(1,26,6);
        newSlider.setMajorTickUnit(1);
        newSlider.setMinorTickCount(0);
        newSlider.setSnapToTicks(true);
        newSlider.setShowTickMarks(true);
        newSlider.setShowTickLabels(true);
        newSlider.setPrefWidth(400);
        return newSlider;
    }

    /**
     * This is a member class that implements and EventHandler. When any of the buttons are pressed in the GUI window,
     * it calls the startSimulation() method in the Main class with the correct parameter.
     */
    class Controller implements EventHandler
    {
        @Override
        public void handle(Event event) {
            if(event.getSource() == randomStartButton)
            {
                source.startSimulation(1);
            }
            if(event.getSource() == presetButtons[0])
            {
                source.startSimulation(2);
            }
            if(event.getSource() == presetButtons[1])
            {
                source.startSimulation(3);
            }
            if(event.getSource() == presetButtons[2])
            {
                source.startSimulation(4);
            }
            if(event.getSource() == presetButtons[3])
            {
                source.startSimulation(5);
            }
            if(event.getSource() == presetButtons[4])
            {
                source.startSimulation(6);
            }
        }
    }
}
