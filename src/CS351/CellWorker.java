package CS351;

import java.util.ArrayList;

/**
 * Created by Nick on 1/24/2017.
 */
public class CellWorker extends Thread {
    private int startY;
    private int endY;
    private Cell[][][] cellGrid;
    private boolean isRunning;
    private boolean isPaused = false;
    private boolean isReady = false;
    private boolean tick = false;
    private Xform cellXform;

    public CellWorker(int start, int end)
    {
        this.startY = start;
        this.endY = end;
        isRunning = true;
    }

    @Override
    public void run()
    {
        while(isRunning)
        {
            if(isPaused)
            {
                try {
                    sleep(100);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!isReady)
            {
                System.out.println("THIS");
                isReady = true;
            }
            try{
                sleep(10);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private boolean checkCell(Cell cell)
    {
        if(cell.getY() >= startY && cell.getY() <= endY)
        {
            return true;
        }
        return false;
    }


    public void gridUpdated(Cell[][][] grid, Xform cellXform)
    {
        this.cellGrid = grid;
        isReady = false;
        //this.cellXform = cellXform;
    }

    public void secondTick()
    {
        this.tick = true;
    }

    public void stopRunning()
    {
        this.isRunning = false;
    }
}
