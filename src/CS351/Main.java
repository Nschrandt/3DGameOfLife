/*Nick Schrandt

 */

package CS351;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
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
    final GUI gui = new GUI(this);
    final SimulationTimer timer = new SimulationTimer();
    final KeyboardController keyboard = new KeyboardController(this);

    private static final double CAMERA_INITIAL_DISTANCE = -150;
    private static final double CAMERA_INITIAL_X_ANGLE = 50.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double ROTATION_SPEED = 2.0;

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
    private Scene simulationScene;

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

    private void buildRandomGrid()
    {
        for(int i = 1; i < 31; i++)
        {
            for(int j = 1; j < 31; j++)
            {
                for(int k = 1; k < 31; k++)
                {
                    if(random.nextInt(100) > 96){
                        Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                        cellGrid[i][j][k] = newCell;
                        newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                                k * cellDepth - (15 * cellDepth));
                        cellXform.getChildren().add(newCell);
                    }
                }
            }
        }
        world.getChildren().add(cellXform);
    }

    private void buildPreset1()
    {
        for(int i = 15; i < 17; i++)
        {
            for(int j = 15; j < 17; j++)
            {
                for(int k = 1; k < 31; k++)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    cellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    cellXform.getChildren().add(newCell);
                }
            }
        }
        deathPopLow = 4;
        deathPopHigh = 9;
        lifePopLow = 5;
        lifePopHigh = 7;
        world.getChildren().add(cellXform);
    }

    private void buildPreset2()
    {
        for(int i = 1; i < 31; i+=9)
        {
            for(int j = 1; j < 31; j++)
            {
                for(int k = 1; k < 31; k+=9)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    cellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    cellXform.getChildren().add(newCell);
                }
            }
        }
        deathPopLow = 1;
        deathPopHigh = 4;
        lifePopLow = 2;
        lifePopHigh = 2;
        world.getChildren().add(cellXform);
    }

    private void buildPreset3()
    {
        for(int i = 1; i < 31; i+=12)
        {
            for(int j = 1; j < 31; j+=12)
            {
                for(int k = 1; k < 31; k+=12)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    cellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    cellXform.getChildren().add(newCell);

                    Cell newCell2 = new Cell(cellWidth,cellHeight,cellDepth);
                    cellGrid[i+1][j+1][k+1] = newCell2;
                    newCell2.setTranslate(i * cellWidth - (15 * cellWidth)+1, j * cellHeight - (15 * cellHeight)+1,
                            k * cellDepth - (15 * cellDepth)+1);
                    cellXform.getChildren().add(newCell2);
                }
            }
        }
        deathPopLow = 1;
        deathPopHigh = 1;
        lifePopLow = 1;
        lifePopHigh = 1;
        world.getChildren().add(cellXform);
    }

    private void buildPreset4()
    {
        for(int i = 1; i < 31; i++)
        {
            for(int j = 15; j < 17; j++)
            {
                for(int k = 15; k < 17; k++)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    cellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    cellXform.getChildren().add(newCell);
                }
            }
        }

        for(int i = 15; i < 17; i++)
        {
            for(int j = 1; j < 31; j++)
            {
                for(int k = 15; k < 17; k++)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    cellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    cellXform.getChildren().add(newCell);
                }
            }
        }
        deathPopLow = 2;
        deathPopHigh = 4;
        lifePopLow = 2;
        lifePopHigh = 3;
        world.getChildren().add(cellXform);
    }

    private void buildPreset5()
    {
        for(int i = 14; i < 21; i+=2)
        {
            for(int j = 14; j < 21; j+=2)
            {
                for(int k = 14; k < 21; k+=2)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    cellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    cellXform.getChildren().add(newCell);
                }
            }
        }
        deathPopLow = 2;
        deathPopHigh = 3;
        lifePopLow = 4;
        lifePopHigh = 4;
        world.getChildren().add(cellXform);
    }

    private void setRValues()
    {
        deathPopLow = gui.getLowPopDeath();
        deathPopHigh = gui.getHighPopDeath();
        lifePopLow = gui.getLowPopLife();
        lifePopHigh = gui.getHighPopLife();
    }

    public void startSimulation(int selection)
    {
        cellXform = new Xform();
        switch (selection)
        {
            case 1:
                setRValues();
                buildRandomGrid();
                break;
            case 2:
                buildPreset1();
                break;
            case 3:
                buildPreset2();
                break;
            case 4:
                buildPreset3();
                break;
            case 5:
                buildPreset4();
                break;
            case 6:
                buildPreset5();
                break;
        }

        primaryStage.setScene(simulationScene);
        primaryStage.show();
        simulationScene.setCamera(camera);
        time = System.nanoTime();
        timer.start();
    }

    public void stopSimulation(){
        timer.stop();
        primaryStage.setScene(gui.createStartScene());
        world.getChildren().remove(cellXform);
        cellXform = null;
        cellGrid = new Cell[32][32][32];
    }

    public void zoom(int direction)
    {
        timer.zoomCamera(direction);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        simulationScene = new Scene(root, 1024, 768, true);
        simulationScene.setFill(Color.BLACK);
        simulationScene.addEventHandler(KeyEvent.KEY_PRESSED, (keyboard));
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Molecule Sample Application");
        Scene startScene = gui.createStartScene();
        primaryStage.setScene(startScene);
        primaryStage.show();
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

        private void zoomCamera(int direction)
        {
            if(direction > 0 && camera.getTranslateZ() < -30)
            {
                double z = camera.getTranslateZ();
                double newZ = z + direction;
                camera.setTranslateZ(newZ);
            }
            if(direction < 0 && camera.getTranslateZ() > -250)
            {
                double z = camera.getTranslateZ();
                double newZ = z + direction;
                camera.setTranslateZ(newZ);
            }
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
                           //cellGrid[i][j][k] = null;

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
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}