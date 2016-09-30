package CS351;

import javafx.scene.Parent;
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
    private Button startButton;
    private BorderPane layout;
    private Controller controller;
    private Slider slider1;
    private Slider slider2;
    private Slider slider3;
    private Slider slider4;

    public GUI(Controller controller)
    {
        this.controller = controller;
    }

    public Scene createStartScene(Parent root)
    {
        layout = new BorderPane();
        startScene = new Scene(layout, 1024, 768, true);
        startButton = new Button("Start Simulation");
        startButton.setOnAction(controller);
        layout.setTop(startButton);
        GridPane sliderWindow = createWindow();
        layout.setCenter(sliderWindow);
        return startScene;
    }

    public double getR1()
    {
        return slider1.getValue();
    }

    public double getR2()
    {
        return slider2.getValue();
    }

    public double getR3()
    {
        return slider3.getValue();
    }

    public double getR4()
    {
        return slider4.getValue();
    }


    private GridPane createWindow()
    {
        GridPane window = new GridPane();
        slider1 = createLowSlider();
        slider2 = createHighSlider();
        slider3 = createLowSlider();
        slider4 = createHighSlider();
        Label label1 = new Label("Low Population Death: ");
        Label label2 = new Label("Over Population Death: ");
        Label label3 = new Label("Low Population Birth: ");
        Label label4 = new Label("High Population Birth: ");

        GridPane.setConstraints(label1, 0, 0);
        window.getChildren().add(label1);
        GridPane.setConstraints(slider1, 1, 0);
        window.getChildren().add(slider1);

        GridPane.setConstraints(label2,0,1);
        window.getChildren().add(label2);
        GridPane.setConstraints(slider2, 1, 1);
        window.getChildren().add(slider2);

        GridPane.setConstraints(label3, 0, 2);
        window.getChildren().add(label3);
        GridPane.setConstraints(slider3, 1, 2);
        window.getChildren().add(slider3);

        GridPane.setConstraints(label4, 0, 3);
        window.getChildren().add(label4);
        GridPane.setConstraints(slider4, 1, 3);
        window.getChildren().add(slider4);

        return window;
    }

    private Slider createLowSlider()
    {
        Slider newSlider = new Slider(1,26,1);
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
        Slider newSlider = new Slider(1,26,10);
        newSlider.setMajorTickUnit(1);
        newSlider.setMinorTickCount(0);
        newSlider.setSnapToTicks(true);
        newSlider.setShowTickMarks(true);
        newSlider.setShowTickLabels(true);
        newSlider.setPrefWidth(400);
        return newSlider;
    }
}
