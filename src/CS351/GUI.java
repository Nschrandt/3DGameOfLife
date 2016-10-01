package CS351;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;

/**
 * Created by nick on 9/27/16.
 */
public class GUI
{
    private Scene startScene;
    private Button randomStartButton;
    private BorderPane layout;
    private Controller controller = new Controller();
    private Slider lowPopDeathSlider;
    private Slider highPopDeathSlider;
    private Slider lowPopLifeSlider;
    private Slider highPopLifeSlider;
    private Button[] presetButtons = new Button[5];
    private Main source;

    public GUI(Main source)
    {
        this.source = source;
    }

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

    public double getLowPopDeath()
    {
        return lowPopDeathSlider.getValue();
    }

    public double getHighPopDeath()
    {
        return highPopDeathSlider.getValue();
    }

    public double getLowPopLife()
    {
        return lowPopLifeSlider.getValue();
    }

    public double getHighPopLife()
    {
        return highPopLifeSlider.getValue();
    }

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
