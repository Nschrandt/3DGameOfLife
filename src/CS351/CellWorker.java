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
    private boolean tick = false;
    private Xform cellXform;
    private ArrayList<Cell> livingCells = new ArrayList<>();
    private ArrayList<Cell> dyingCells = new ArrayList<>();

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
            if(tick)
            {
                //animateCells();
                tick = false;
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

    private void animateCells()
    {
        ArrayList<Cell> livingRemovals = new ArrayList<>();
        ArrayList<Cell> dyingRemovals = new ArrayList<>();
        for(Cell cell : livingCells)
        {
            if(checkCell(cell) && cell.live() >= 60)
            {
                livingRemovals.add(cell);
            }
        }
        for(Cell cell : dyingCells)
        {
            if(checkCell(cell) && cell.die() <= 0)
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

    public void gridUpdated(Cell[][][] grid, Xform cellXform, ArrayList<Cell> livingList, ArrayList<Cell> dyingList)
    {
        this.cellGrid = grid;
        this.cellXform = cellXform;
        livingCells = livingList;
        dyingCells = dyingList;
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
