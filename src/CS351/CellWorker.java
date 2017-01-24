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
    private ArrayList<Cell> livingMasterList;
    private ArrayList<Cell> dyingMasterList;

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
                animateCells();
                tick = false;
            }
            try{
                sleep(10);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void makeLocalList(ArrayList<Cell> masterList, ArrayList<Cell> localList)
    {
        for(Cell cell : masterList)
        {
            if(cell.getTranslateY() <= endY && cell.getTranslateY() >= startY)
            {
                localList.add(cell);
            }
        }
    }

    private void animateCells()
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

    public void gridUpdated(Cell[][][] grid, Xform cellXform, ArrayList<Cell> livingList, ArrayList<Cell> dyingList)
    {
        this.cellGrid = grid;
        this.cellXform = cellXform;
        livingMasterList = livingList;
        dyingMasterList = dyingList;
        makeLocalList(livingMasterList,livingCells);
        makeLocalList(dyingMasterList, dyingCells);
    }

    public void secondTick()
    {
        this.tick = true;
    }

    public void stopRunning()
    {
        this.isRunning = false;
    }

    public static void main(String[] args)
    {
        Cell[] cells = new Cell[10];
        for(int i = 0; i < 10; i ++)
        {
            Cell newCell = new Cell(1,1,1);
            cells[i] = newCell;
        }
    }
}
