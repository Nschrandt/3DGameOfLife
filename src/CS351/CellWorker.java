package CS351;

/**
 * Created by Nick on 1/24/2017.
 */
public class CellWorker extends Thread {
    private int startY;
    private int endY;
    private boolean tick = true;
    private boolean isRunning = false;
    private boolean isPaused = false;

    public CellWorker(int start, int end)
    {
        this.startY = start;
        this.endY = end;
    }

    @Override
    public void run()
    {
        while(isRunning && !isPaused)
        {

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

    }
}
