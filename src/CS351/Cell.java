package CS351;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * Created by nick on 9/25/16.
 */
public class Cell extends Xform{

    private Box cellBox;
    private double dieTimer = 60;
    private double liveTimer = 0;
    private PhongMaterial livingColor = new PhongMaterial();
    private PhongMaterial dyingColor = new PhongMaterial();


    public Cell(double boxWidth, double boxHeight, double boxDepth)
    {
        cellBox = new Box(boxWidth,boxHeight,boxDepth);
        this.getChildren().add(cellBox);
        livingColor.setSpecularColor(Color.GREEN);
        livingColor.setDiffuseColor(Color.GREEN);
        cellBox.setMaterial(livingColor);
    }

   public double live()
   {
       this.setScale(liveTimer/60);
       setLiveColor();
       cellBox.setMaterial(livingColor);
       liveTimer++;
       return liveTimer;
   }

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

   public double die()
   {
       this.setScale(dieTimer/60);
       setDyingColor();
       cellBox.setMaterial(dyingColor);
       dieTimer--;
       return dieTimer;
   }

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
