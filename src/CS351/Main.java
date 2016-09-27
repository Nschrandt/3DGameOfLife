/*Nick Schrandt

This class is a modified version of the one presented in the JavaFX tutorial on
https://docs.oracle.com/javase/8/javafx/graphics-tutorial/sampleapp3d.htm.

In addition to creating an H2O molucule, it has two PointLight sources initially set
to red and white. One of them is behind the molucule and the other is in front of it.
By pressing the UP arrow on the keyboard, you can change one of the lights to a random
color, and by pressing the DOWN arrow on the keyboard, you can change the other light
to a random color.
 */

package CS351;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application{

    final Group root = new Group();
    final Xform world = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    final Controller handler = new Controller(this);
    final GUI gui = new GUI(handler);
    final SimulationTimer timer = new SimulationTimer();

    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 50.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double ROTATION_SPEED = 2.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double TRACK_SPEED = 0.3;
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    private Stage primaryStage;
    private Cell[][][] cellGrid = new Cell[32][32][32];
    private Xform cellXform;
    private ArrayList<Cell> livingCells = new ArrayList();
    private Random random = new Random();
    private final double cellWidth = 2.0;
    private final double cellHeight = 2.0;
    private final double cellDepth = 2.0;


    /*
    Method provided by the tutorial to build the camera node.
     */
    private void buildCamera()
    {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    private void handleMouse(Scene scene, final Node root) {

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;

                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() -
                            mouseDeltaX*modifier*ROTATION_SPEED);  //
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
                            mouseDeltaY*modifier*ROTATION_SPEED);  // -
                }
                else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                    camera.setTranslateZ(newZ);
                }
                else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() +
                            mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
                    cameraXform2.t.setY(cameraXform2.t.getY() +
                            mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
                }
            }
        }); // setOnMouseDragged
    } //handleMouse

    private void buildCell()
    {
        cellXform = new Xform();
        for(int i = 1; i < 31; i++)
        {
            for(int j = 1; j < 31; j++)
            {
                for(int k = 1; k < 31; k++)
                {
                    Cell newCell = new Cell(i,j,k, cellGrid);
                    cellGrid[i][j][k] = newCell;
                    newCell.createBox(cellWidth,cellHeight,cellDepth);
                    Xform newXform = new Xform();
                    if(random.nextInt(100) > 95) {
                        newXform.getChildren().add(newCell.getBox());
                        newXform.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                                k * cellDepth - (15 * cellDepth));
                    }
                    cellXform.getChildren().add(newXform);
                }
            }
        }
        world.getChildren().add(cellXform);
    }

    public void startSimulation()
    {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        buildCell();

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.BLACK);

        handleMouse(scene, world);
        //handleKeyboard(scene, world);

        primaryStage.setTitle("Molecule Sample Application");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setCamera(camera);
        timer.start();
    }

    public GUI getGUI()
    {
        return gui;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        this.primaryStage = primaryStage;
        Scene startScene = gui.createStartScene(root);
        primaryStage.setScene(startScene);
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}