/*Nick Schrandt

 */

package CS351;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
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

    private static final double CAMERA_INITIAL_DISTANCE = -150;
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

    private double deathPopLow;
    private double deathPopHigh;
    private double lifePopLow;
    private double lifePopHigh;

    private Stage primaryStage;
    private Cell[][][] cellGrid = new Cell[32][32][32];
    private Xform cellXform;
    private long time;
    private Random random = new Random();
    private final double cellWidth = 1.0;
    private final double cellHeight = 1.0;
    private final double cellDepth = 1.0;
    private ArrayList<Cell> livingCells = new ArrayList<>();
    private ArrayList<Cell> dyingCells = new ArrayList<>();

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
                            mouseDeltaX*modifier*ROTATION_SPEED);
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
                            mouseDeltaY*modifier*ROTATION_SPEED);
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
        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        redMaterial.setSpecularColor(Color.DARKRED);
        cellXform = new Xform();
        for(int i = 1; i < 31; i++)
        {
            for(int j = 1; j < 31; j++)
            {
                for(int k = 1; k < 31; k++)
                {
                    if(random.nextInt(100) > 98){
                        Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                        cellGrid[i][j][k] = newCell;
                        newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                                k * cellDepth - (15 * cellDepth));
                        cellXform.getChildren().add(newCell);
                        //System.out.println(i + " " + j + " " + k + " " + ": " + newCell.checkSurroundings());
                    }
                }
            }
        }
        world.getChildren().add(cellXform);
    }

    private void setRValues()
    {
        deathPopLow = gui.getR1();
        deathPopHigh = gui.getR2();
        lifePopLow = gui.getR3();
        lifePopHigh = gui.getR4();
    }

    public void startSimulation()
    {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        setRValues();
        buildCell();

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.BLACK);

        handleMouse(scene, world);
        //handleKeyboard(scene, world);

        primaryStage.setTitle("Molecule Sample Application");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setCamera(camera);
        time = System.nanoTime();
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

    class SimulationTimer extends AnimationTimer{
        @Override
        public void handle(long now) {
            if(now - time > 1_000_000_000)
            {
                world.getChildren().remove(cellXform);
                cellXform = updateGrid();
                world.getChildren().add(cellXform);
                time = System.nanoTime();
            }

            adjustCells();

            /*This is code taken from the molecule project but adapted to turn
              without mouse input. */
            double modifier = 0.1;
            cameraXform.ry.setAngle(cameraXform.ry.getAngle() -
                    modifier*ROTATION_SPEED);
            cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
                    modifier*ROTATION_SPEED);
        }

        private Xform updateGrid()
        {
            PhongMaterial greenMaterial = new PhongMaterial();
            greenMaterial.setDiffuseColor(Color.GREEN);
            greenMaterial.setSpecularColor(Color.DARKGREEN);
            Xform newCellXForm = new Xform();
            Cell[][][] newCellGrid = new Cell[32][32][32];
            for(int i = 1; i<31; i++)
            {
                for(int j = 1; j < 31; j++)
                {
                    for(int k = 1; k < 31; k++)
                    {
                       int neighbors = checkSurroundings(i,j,k);
                       if(cellGrid[i][j][k] == null && neighbors <= lifePopHigh && neighbors >= lifePopLow)
                       {
                           Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                           newCellGrid[i][j][k] = newCell;
                           newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                                   k * cellDepth - (15 * cellDepth));
                           newCellXForm.getChildren().add(newCell);
                           livingCells.add(newCell);
                       }
                       else if(cellGrid[i][j][k] != null && (neighbors < deathPopLow || neighbors > deathPopHigh))
                       {
                           newCellXForm.getChildren().add(cellGrid[i][j][k]);
                           dyingCells.add(cellGrid[i][j][k]);
                           newCellGrid[i][j][k] = null;
                           cellGrid[i][j][k] = null;

                       }
                       else if(cellGrid[i][j][k] != null && !(neighbors < deathPopLow || neighbors > deathPopHigh))
                       {
                           newCellXForm.getChildren().add(cellGrid[i][j][k]);
                           newCellGrid[i][j][k] = cellGrid[i][j][k];
                       }
                    }
                }
            }
            cellGrid = newCellGrid;
            return newCellXForm;
        }

        private void adjustCells()
        {
            ArrayList<Cell> livingRemovals = new ArrayList<>();
            ArrayList<Cell> dyingRemovals = new ArrayList<>();
            for(Cell cell : livingCells)
            {
                if(cell.live() >= 60)
                {
                    livingRemovals.add(cell);
                }
            }
            for(Cell cell : dyingCells)
            {
                if(cell.die() <= 0)
                {
                    cellXform.getChildren().remove(cell);
                    dyingRemovals.add(cell);
                }
            }
            for(Cell cell: livingRemovals)
            {
                livingCells.remove(cell);
            }
            for(Cell cell: dyingRemovals)
            {
                dyingCells.remove(cell);
            }
        }

        private int checkSurroundings(int x, int y, int z)
        {
            int neighbors = 0;
            for(int i = x-1; i < x+2; i++)
            {
                for(int j = y-1; j<y+2; j++)
                {
                    for(int k = z-1; k<z+2; k++)
                    {
                        //System.out.println(i + " " + j + " " + k + ": " + grid[i][j][k]);
                        if(cellGrid[i][j][k] != null && !(i == x && j == y && k ==z))
                        {
                            neighbors++;
                        }
                    }
                }
            }
            return neighbors;
        }

//        private void updateGrid(PhongMaterial greenMaterial) {
//            for(int i = 1; i<31; i++)
//            {
//                for(int j = 1; j<31; j++)
//                {
//                    for(int k = 1; k<31; k++)
//                    {
//                        Cell currentCell = cellGrid[i][j][k];
//                        int neighbors = currentCell.checkSurroundings(i, j , k);
//                        if(currentCell.isAlive() && (neighbors > deathPopHigh || neighbors < deathPopLow))
//                        {
//                            currentCell.getChildren().remove(currentCell.getBox());
//                            currentCell.setAlive(false);
//                        }
//                        else if(!currentCell.isAlive() && (neighbors >= lifePopLow && neighbors <= lifePopHigh))
//                        {
//                            currentCell.getBox().setMaterial(greenMaterial);
//                            currentCell.getChildren().add(currentCell.getBox());
//                            currentCell.setAlive(true);
//                        }
//                    }
//                }
//            }
//        }
    }
}