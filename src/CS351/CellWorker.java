package CS351;

/**
 * Created by Nick on 1/24/2017.
 */
public class CellWorker extends Thread {
    private Cell[] cells;
    private int startY;
    private int endY;
    private boolean tick = true;
    private boolean isRunning = true;

    public CellWorker(Cell[] cells, int start, int end)
    {
        this.cells = cells;
        this.startY = start;
        this.endY = end;
    }

    @Override
    public void run()
    {
        while(isRunning)
        {
            if(tick)
            {
                for(Cell cell : cells)
                {
                    System.out.println(cell.getTranslateX());
                    tick = false;
                }
            }
        }
    }

    public static void main(String[] args)
    {
        Cell[] cells = new Cell[10];
        for(int i = 0; i < 10; i ++)
        {
            Cell newCell = new Cell(1,1,1);
            cells[i] = newCell;
        }
        CellWorker a = new CellWorker(cells, 1, 10);
        a.start();
    }
}
