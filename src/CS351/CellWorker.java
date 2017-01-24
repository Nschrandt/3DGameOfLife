package CS351;

/**
 * Created by Nick on 1/24/2017.
 */
public class CellWorker extends Thread {
    private int startY;
    private int endY;
    private Cell[][][] cellGrid;
    private boolean isPaused = false;
    private boolean tick = false;

    public CellWorker(int start, int end)
    {
        this.startY = start;
        this.endY = end;
    }

    @Override
    public void run()
    {
        while(!isPaused)
        {
            if(tick)
            {
                System.out.println(startY);
                tick = false;
            }
        }
    }

    public void gridUpdated(Cell[][][] grid)
    {
        this.cellGrid = grid;
    }

    public void secondTick()
    {
        tick = true;
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
