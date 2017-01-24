package CS351;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * @author Nick Schrandt
 *
 * This is the class for the cells that inhabit the 3D grid for the Game of Life Simulation.
 */
public class Cell extends Xform{

    /**JavaFX Box shape that is actually displayed*/
    private Box cellBox;
    /**Timer to control the animation of a cell coming to life*/
    private double dieTimer = 60;
    /**Timer to control the animation of a dying cell*/
    private double liveTimer = 0;
    /**Material used to coat the cells as they come to life*/
    private PhongMaterial livingColor = new PhongMaterial();
    /**Material used to coat the cells as they die*/
    private PhongMaterial dyingColor = new PhongMaterial();

    private int y;

    /**
     * This is the Constructor for the Cell class. It creates a Box and adds the box to itself, as well as sets its
     * default color.
     * @param boxWidth width of the cell
     * @param boxHeight height of the cell
     * @param boxDepth depth of the cell
     *

     */
    public Cell(double boxWidth, double boxHeight, double boxDepth, int y)
    {
        cellBox = new Box(boxWidth,boxHeight,boxDepth);
        this.getChildren().add(cellBox);
        livingColor.setSpecularColor(Color.GREEN);
        livingColor.setDiffuseColor(Color.GREEN);
        cellBox.setMaterial(livingColor);
        this.y = y;
    }

    /**
     * This is called when a cell has just been created during the AnimationTimer. It calls the setLiveColor() to adjust
     * the color and also scales itself so that it appears to grow over the course of its animation.
     * @return value of the liveTimer. In the Main.java class, it checks this to determine if the cell has finished
     * its animation.
     *
     */
   protected double live()
   {
       this.setScale(liveTimer/60);
       setLiveColor();
       cellBox.setMaterial(livingColor);
       liveTimer++;
       return liveTimer;
   }

    /**
     *
     * This method changes the diffuse color of the living material based on the current state of animation of the
     * cell.
     */
   private void setLiveColor(){
       if(liveTimer < 20)
       {
           livingColor.setDiffuseColor(Color.LIGHTGREEN);
       }
       else if(liveTimer < 30)
       {
           livingColor.setDiffuseColor(Color.LIMEGREEN);
       }
       else if(liveTimer < 40)
       {
           livingColor.setDiffuseColor(Color.GREEN);
       }
       else if(liveTimer < 50)
       {
           livingColor.setDiffuseColor(Color.FORESTGREEN);
       }
       else
       {
           livingColor.setDiffuseColor(Color.DARKGREEN);
       }
   }

   public int getY()
   {
       return y;
   }

    /**
     * Behaves in the same fashion as does the live() method, except that the cell shrinks in its animation.
     * @return value of the dyingTimer. In the Main.java class, it checks this to determine if the cell has finished
     * its animation.
     *
     *
     */
   public double die()
   {
       this.setScale(dieTimer/60);
       setDyingColor();
       cellBox.setMaterial(dyingColor);
       dieTimer--;
       return dieTimer;
   }

    /**
     * Same implementation as setLivingColor()
     */
    private void setDyingColor(){
        if(liveTimer < 20)
        {
            dyingColor.setDiffuseColor(Color.FIREBRICK);
        }
        else if(liveTimer < 30)
        {
            dyingColor.setDiffuseColor(Color.RED);
        }
        else if(liveTimer < 40)
        {
            dyingColor.setDiffuseColor(Color.CRIMSON);
        }
        else if(liveTimer < 50)
        {
            dyingColor.setDiffuseColor(Color.DARKRED);
        }
        else
        {
            dyingColor.setDiffuseColor(Color.DARKRED);
        }
    }
}
