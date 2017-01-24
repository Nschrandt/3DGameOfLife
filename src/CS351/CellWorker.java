package CS351;

/**
 * Created by Nick on 1/24/2017.
 */
public class CellWorker extends Thread {
    Cell[] cells;

    public CellWorker(Cell[] cells)
    {
        this.cells = cells;
    }
}
