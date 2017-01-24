package CS351;

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
                //ANIMATE
                tick = false;
            }
            try{
                sleep(10);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void gridUpdated(Cell[][][] grid)
    {
        this.cellGrid = grid;
    }

    public void secondTick()
    {
        System.out.println("THIS");
        this.tick = true;
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
