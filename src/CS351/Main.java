package CS351;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Nick Schrandt
 *
 * This is the main class for the 3D Game of Life program from which all other aspects of the program are run.
 * The program is a 3D implementation of Conway's Game of Life, where cells that inhabit a grid-space live and die based
 * on their surroundings. In this instance, the user can control those parameters, which include the following:
 *
 * 1)Minimum number of neighbor cells for a living cell to stay alive.
 * 2)Maximum number of neighbor cells for a living cell to stay alive.
 * 3)Minimum number of neighbor cells for an empty space to become a cell.
 * 4)Maximum number of neighbor cells for an empty space to become a living cell.
 *
 * There are also five preset grids that produce interesting results.
 *
 * Users can control the camera zoom in the simulation with the up and down arrows, and can reset the simulation at
 * any time with the R key.
 *
 * This class was actually a part of the Molecule.java code from the previous project that was altered to fit the needs
 * of this project, so many aspects, such as the camera, are the same.
 */
public class Main extends Application{


    /** Root Group, created in the Molecule.java class */
    final private Group root = new Group();
    /** world Xform, also from the Molecule.java class */
    final private Xform world = new Xform();
    /** Main camera for the program. Also from the Molecule.java class */
    final private PerspectiveCamera camera = new PerspectiveCamera(true);
    final private Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();
    /** GUI class that creates the gui that allows for the user input */
    private final GUI gui = new GUI(this);
    /** Member class of the Main class that implements a*/
    private final SimulationTimer timer = new SimulationTimer();
    /** Small class that handles the keyboard input from the user for zoom and restart*/
    private final KeyboardController keyboard = new KeyboardController(this);

    /**Initial distance of camera from center. From Molecule.java*/
    private static final double CAMERA_INITIAL_DISTANCE = -150;
    /**Initial x angle of camera. From Molecule.java*/
    private static final double CAMERA_INITIAL_X_ANGLE = 50.0;
    /**Initial y angle of camera. From Molecule.java*/
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    /**Initial near clip of camera. From Molecule.java*/
    private static final double CAMERA_NEAR_CLIP = 0.1;
    /**Initial far clip of camera. From Molecule.java*/
    private static final double CAMERA_FAR_CLIP = 10000.0;
    /**Initial rotation speed camera. From Molecule.java*/
    private static final double ROTATION_SPEED = 2.0;
    /**Number of threads to split the animation.*/
    private static final int THREAD_COUNT = 6;

    /**This will store the lower bound of neighbors at which a cell dies*/
    private double deathPopLow;
    /**This will store the upper bound of neighbors at which a cell dies*/
    private double deathPopHigh;
    /**This will store the lower bound of neighbors at which a space becomes a cell*/
    private double lifePopLow;
    /**This will store the upper bound of neighbors at which a space becomes a cell*/
    private double lifePopHigh;

    /**Primary stage for the class*/
    private Stage primaryStage;
    private BorderPane pane;
    /**3D grid of Cells that is used to build the animation space.*/
    private Cell[][][] currentCellGrid = new Cell[32][32][32];
    /**Next cell grid to be updated by the CellWorker Threads*/
    private Cell[][][] nextCellGrid = new Cell[32][32][32];
    /**Xform that holds all of the graphical elements from the grid*/
    private Xform currentCellXform;
    /**Next Xform to hold the graphical elements from the next grid.*/
    private Xform nextCellXform;
    /**time variable used to measure the Animation timing*/
    private long time;
    /**Random number generator used to populate a random grid*/
    private Random random = new Random();
    /**Width of a cell*/
    private final double cellWidth = 1.0;
    /**Height of a cell*/
    private final double cellHeight = 1.0;
    /**Depth of a cell*/
    private final double cellDepth = 1.0;
    /**ArrayList of cells that are currently coming to life. Used for their animation.*/
    private ArrayList<Cell> livingCells = new ArrayList<>();
    /**ArrayList of cells that are currently dying. Used for their animation.*/
    private ArrayList<Cell> dyingCells = new ArrayList<>();
    /**Main scene for the simulation*/
    private SubScene simulationScene;
    /**Top layer scene that holds the simulation scene and the toolbar*/
    private Scene mainScene;
    /**Array of CellWorker threads*/
    private CellWorker[] workers = new CellWorker[THREAD_COUNT];
    /**Toolbar created by the gui to alter simulation during runtime*/
    private ToolBar toolBar;
    /**boolean to see if the simulation is currently paused or not*/
    private boolean isPaused = false;
    /**value of the current simulation*/
    private int currentSim;



    /**
     * This function was from the Molecule.java project in the last lab and was not authored by me.
     */
    private void buildCamera()
    {
        root.getChildren().add(cameraXform3);
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

    /**
     *
     * This function creates a random grid. In each Cell location there is a 4% chance that a Cell will populate that
     * space. If this happens, the cell is added to the currentCellGrid and its translate is set. It's then added to the
     * currentCellXform to be displayed. At the end, the currentCellXform is added to the world.
     */
    private void buildRandomGrid()
    {
        for(int i = 1; i < 31; i++)
        {
            for(int j = 1; j < 31; j++)
            {
                for(int k = 1; k < 31; k++)
                {
                    if(random.nextInt(100) > 97){
                        Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                        currentCellGrid[i][j][k] = newCell;
                        newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                                k * cellDepth - (15 * cellDepth));
                        currentCellXform.getChildren().add(newCell);
                    }
                }
            }
        }
        world.getChildren().add(currentCellXform);
    }

    /**
     *
     * Similar to the previous method except this method builds a specific shape that creates an interesting
     * pattern in simulation. The parameters normally input by the user are automatically set.
     */
    private void buildPreset1()
    {
        for(int i = 15; i < 17; i++)
        {
            for(int j = 1; j < 31; j++)
            {
                for(int k = 15; k < 17; k++)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    currentCellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    currentCellXform.getChildren().add(newCell);
                }
            }
        }
        deathPopLow = 2;
        deathPopHigh = 2;
        lifePopLow = 3;
        lifePopHigh = 3;
        world.getChildren().add(currentCellXform);
    }

    /**
     *
     * Second preset method. Behaves the same as the previous.
     */
    private void buildPreset2()
    {
        for(int i = 1; i < 31; i+=9)
        {
            for(int j = 1; j < 31; j++)
            {
                for(int k = 1; k < 31; k+=9)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    currentCellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    currentCellXform.getChildren().add(newCell);
                }
            }
        }
        deathPopLow = 2;
        deathPopHigh = 2;
        lifePopLow = 2;
        lifePopHigh = 2;
        world.getChildren().add(currentCellXform);
    }

    /**
     *
     * Third preset method. Behaves the same as the previous.
     */
    private void buildPreset3()
    {
        for(int i = 1; i < 31; i+=12)
        {
            for(int j = 1; j < 31; j+=12)
            {
                for(int k = 1; k < 31; k+=12)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    currentCellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    currentCellXform.getChildren().add(newCell);

                    Cell newCell2 = new Cell(cellWidth,cellHeight,cellDepth);
                    currentCellGrid[i+1][j+1][k+1] = newCell2;
                    newCell2.setTranslate(i * cellWidth - (15 * cellWidth)+1, j * cellHeight - (15 * cellHeight)+1,
                            k * cellDepth - (15 * cellDepth)+1);
                    currentCellXform.getChildren().add(newCell2);
                }
            }
        }
        deathPopLow = 1;
        deathPopHigh = 1;
        lifePopLow = 1;
        lifePopHigh = 1;
        world.getChildren().add(currentCellXform);
    }

    /**
     *
     * Fourth preset method. Behaves the same as the previous.
     */
    private void buildPreset4()
    {
        for(int i = 1; i < 31; i++)
        {
            for(int j = 15; j < 17; j++)
            {
                for(int k = 15; k < 17; k++)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    currentCellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    currentCellXform.getChildren().add(newCell);
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
                    currentCellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    currentCellXform.getChildren().add(newCell);
                }
            }
        }
        deathPopLow = 5;
        deathPopHigh = 2;
        lifePopLow = 2;
        lifePopHigh = 3;
        world.getChildren().add(currentCellXform);
    }

    /**
     *
     * Fifth preset method. Behaves the same as the previous.
     */
    private void buildPreset5()
    {
        for(int i = 14; i < 21; i+=2)
        {
            for(int j = 14; j < 21; j+=2)
            {
                for(int k = 14; k < 21; k+=2)
                {
                    Cell newCell = new Cell(cellWidth,cellHeight,cellDepth);
                    currentCellGrid[i][j][k] = newCell;
                    newCell.setTranslate(i * cellWidth - (15 * cellWidth), j * cellHeight - (15 * cellHeight),
                            k * cellDepth - (15 * cellDepth));
                    currentCellXform.getChildren().add(newCell);
                }
            }
        }
        deathPopLow = 2;
        deathPopHigh = 3;
        lifePopLow = 4;
        lifePopHigh = 4;
        world.getChildren().add(currentCellXform);
    }

    /**
     *
     * This method is called when the random simulation is selected and it retrieves the parameter values from the
     * GUI class where the user set them on the sliders.
     */
    private void setRValues()
    {
        deathPopLow = gui.getLowPopDeath();
        deathPopHigh = gui.getHighPopDeath();
        lifePopLow = gui.getLowPopLife();
        lifePopHigh = gui.getHighPopLife();
    }

    /**
     ** This is called from the GUI class when a selection has been made by the user.
     * @param selection Determined by the button pressed by the user. 1 is a random simulation, 2-6 are the preset
     *                  buttons.
     *
     */
    protected void startSimulation(int selection)
    {
        currentCellXform = new Xform();
        nextCellXform = new Xform();
        chooseSimulation(selection);
        gui.setComboBoxValues(deathPopHigh,deathPopLow,lifePopHigh,lifePopLow);
        setUpThreads();
        primaryStage.setScene(mainScene);
        primaryStage.show();
        simulationScene.setCamera(camera);
        time = System.nanoTime();
        for(CellWorker worker : workers)
        {
            worker.start();
        }
        timer.start();
    }

    /*
    Moved this chunk of code out of the startSimulation method so it could be used by the resetSimulation method
     */
    private void chooseSimulation(int selection) {
        switch (selection)
        {
            case 1:
                setRValues();
                buildRandomGrid();
                currentSim = 1;
                break;
            case 2:
                buildPreset1();
                currentSim = 2;
                break;
            case 3:
                currentSim = 3;
                buildPreset2();
                break;
            case 4:
                currentSim = 4;
                buildPreset3();
                break;
            case 5:
                currentSim = 5;
                buildPreset4();
                break;
            case 6:
                currentSim = 6;
                buildPreset5();
                break;
        }
    }

    /**
     * This method simply pauses the simulation if it is running, or resumes it if it is paused. Called when the
     * pause/resume button is pressed
     * @param pauseButton button that is pressed
     */
    protected void pauseSimulation(Button pauseButton)
    {
        if(!isPaused)
        {
            timer.stop();
            pauseButton.setText("Resume");
            isPaused = true;
        }
        else
        {
            timer.start();
            pauseButton.setText("Pause Simulation");
            isPaused = false;
        }
    }

    /**
     * Resets the current preset or random simulation but with altered R values.
     * @param lowD low dead pop
     * @param highD high death pop
     * @param lowL low life pop
     * @param highL high life pop
     */
    protected void resetSimulation(Double lowD, Double highD, Double lowL, Double highL)
    {
        timer.stop();
        for(CellWorker worker: workers)
        {
            worker.stopRunning();
        }
        world.getChildren().remove(currentCellXform);
        currentCellXform = new Xform();
        currentCellGrid = new Cell[32][32][32];
        nextCellGrid = new Cell[32][32][32];
        chooseSimulation(currentSim);
        deathPopHigh = highD;
        deathPopLow = lowD;
        lifePopLow = lowL;
        lifePopHigh = highL;
        setUpThreads();
        for(CellWorker worker : workers)
        {
            worker.setRValues(lifePopLow,lifePopHigh,deathPopLow,deathPopHigh);
            worker.start();
        }
        timer.start();
    }

    /**
     *
     * Function called by the KeyboardController when the reset button is pressed. It stops the AnimationTimer,
     * resets the scene to the GUI start screen, and resets the fields so that a new simulation can begin.
     */
    protected void stopSimulation(){
        timer.stop();
        for(CellWorker worker : workers)
        {
            worker.stopRunning();
        }
        primaryStage.setScene(gui.createStartScene());
        world.getChildren().remove(currentCellXform);
        currentCellXform = null;
        currentCellGrid = new Cell[32][32][32];
        nextCellGrid = new Cell[32][32][32];
    }

    /**
     * Simple function called from the KeyboardController class when the up or down arrows are pressed by the user.
     * @param direction direction in which the camera will move.
     *
     * Simple function called from the KeyboardController class when the up or down arrows are pressed by the user.
     */
    protected void zoom(int direction)
    {
        timer.zoomCamera(direction);
    }

    /**
     * This is the overwite of the start method for the Animation class and does all the primary setup for the
     * simulation to run, including creating the simulation scene, and calling the GUI class to create a start
     * scene, which it then displays.
     * @param primaryStage main stage upon which the animation takes place.
     * @throws Exception exceptiong
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        simulationScene = new SubScene(root, 1024,768,true, SceneAntialiasing.BALANCED);
        simulationScene.setFill(Color.BLACK);
        simulationScene.addEventHandler(KeyEvent.KEY_PRESSED, (keyboard));
        toolBar = gui.createToolBar();
        pane = new BorderPane();
        pane.setCenter(simulationScene);
        pane.setTop(toolBar);
        mainScene = new Scene(pane, 1024, 768, true);
        this.primaryStage = primaryStage;
        primaryStage.setTitle("3D Game of Life");
        Scene startScene = gui.createStartScene();
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    /*
    This method sets up the CellWorker threads and passes them the first cell grid.
     */
    private void setUpThreads()
    {
        for(int i = 1; i <= 30; i+=(30/THREAD_COUNT))
        {
            CellWorker newWorker = new CellWorker(i, i + (30/THREAD_COUNT-1), deathPopLow, deathPopHigh, lifePopLow, lifePopHigh);
            newWorker.setDaemon(true);
            newWorker.gridUpdated(currentCellGrid, nextCellGrid);
            workers[i/(30/THREAD_COUNT)] = newWorker;
        }
    }

    /*
     *
     * This is a member class for the Main class that extends AnimationTImer and serves as the main timer for the
     * simulation.
     */
    private class SimulationTimer extends AnimationTimer{

        /**
         * * This is the override for the handle method, which is called every frame. The first thing it does is check to
         * see if a second has passed and if the threads have finished processing the new grid. If so, it uses the new
         * grid to update the next Xform and swaps it in. It then resets the next grid and Xform and passes them to the
         * threads to begin working on the next update.
         *
         * It also calls the adjustCells method each frame, which controls the animation for the cells living and dying.
         *
         * Finally, it controls the rotation of the camera, which was an adapted block of code from the Molecule.java
         * class.
         * @param now Current timestamp in nanoseconds.
         *
         *
         */
        @Override
        public void handle(long now) {
            if(now - time > 1_000_000_000 && areThreadsReady())
            {
                updateXform();
                world.getChildren().remove(currentCellXform);
                world.getChildren().add(nextCellXform);
                currentCellXform = nextCellXform;
                currentCellGrid = nextCellGrid;
                nextCellGrid = new Cell[32][32][32];
                nextCellXform = new Xform();
                time = System.nanoTime();
                for(CellWorker worker : workers)
                {
                    worker.gridUpdated(currentCellGrid,nextCellGrid);
                }
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

        /*
        This method checks to see if all of the CellWorker threads have finished updating their assigned section of
        the grid.
         */
        private boolean areThreadsReady()
        {
            for(CellWorker worker : workers)
            {
                if(!worker.isReady())
                {
                    return false;
                }
            }
            return true;
        }

        /*
         * This method is called from the Main when it receives the event from the KeyboardController class. It
         * zooms the camera either in or out, depending on the key pressed. The zoom is bounded.
         * @param direction direction in which the camera will zoom.
         *
         */
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

        /*
         * This method uses the new grid given to the main thread by the worker threads to update the next Xform. If the
         * cell exists in both the old and new grids, it is simply added to the new Xform. If it exists in the new one,
         * but not the old one, it is added to the Xform and the livingCells list, so it can be animated. If it exists
         * in the old one but not the new one, it is still added to the Xform, as well as the dyingCells list so it can
         * be animated and removed.
         *
         */
        private void updateXform()
        {
            for(int i = 1; i<31; i++)
            {
                for(int j = 1; j < 31; j++)
                {
                    for(int k = 1; k < 31; k++)
                    {
                       Cell oldCell = currentCellGrid[i][j][k];
                       Cell newCell = nextCellGrid[i][j][k];
                       if(newCell != null && oldCell != null)
                       {
                           nextCellXform.getChildren().add(newCell);
                       }
                       else if(newCell != null && oldCell == null)
                       {
                           nextCellXform.getChildren().add(newCell);
                           livingCells.add(newCell);
                       }
                       else if(newCell == null && oldCell != null)
                       {
                           nextCellXform.getChildren().add(oldCell);
                           dyingCells.add(oldCell);
                       }
                    }
                }
            }
        }

        /*
         *
         * This is the method that controls the animation of the cells as they come to life or die. It loops through
         * the livingCells and dyingCells lists and calls the Cell.live() or Cell.die() methods respectively. If either
         * return a value specifying that their animation is complete, it adds them to a separate list.
         *
         * It then loops through the list of cells that have completed their animation and removes them from their
         * respective lists.
         */
        private void adjustCells()
        {
            ArrayList<Cell> livingRemovals = new ArrayList<>();
            ArrayList<Cell> dyingRemovals = new ArrayList<>();
            for (Cell cell : livingCells) {
                if (cell.live() >= 60) {
                    livingRemovals.add(cell);
                }
            }
            for (Cell cell : dyingCells) {
                if (cell.die() <= 0) {
                    currentCellXform.getChildren().remove(cell);
                    dyingRemovals.add(cell);
                }
            }
            for (Cell cell : livingRemovals) {
                livingCells.remove(cell);
            }
            for (Cell cell : dyingRemovals) {
                dyingCells.remove(cell);
            }
        }
    }

    /**
     * main method. Simply launches the program.
     * @param args command line args
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}