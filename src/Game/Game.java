package Game;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final int rows;
    private final int columns;
    private final int minesCount;

    private final ArrayList<Cell> mines = new ArrayList<>();
    private final ArrayList<Cell> openedCells = new ArrayList<>();

    private boolean start = true;
    public boolean isOver = false;

    public Game(int rows, int columns, int minesCount) {
        this.minesCount = minesCount;
        this.rows = rows;
        this.columns = columns;
    }

    public void over() {
        isOver = true;
        openedCells.addAll(mines);
    }

    public void restart() {
        mines.clear();
        openedCells.clear();
        start = true;
        isOver = false;
    }

    public int minesCount(int row, int column) {
        int count = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if (mines.contains(new Cell(i, j))) {
                    count++;
                }
            }
        }
        return count;
    }

    public void open(int row, int column) {
        if (isOver) {
            return;
        }

        Cell cell = new Cell(row, column);
        if (openedCells.contains(cell)) {
            return;
        }
        openedCells.add(cell);

        if (start) {
            generateMines(row, column);
            start = false;
        }

        if (minesCount(row, column) > 0) return;

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {

                if (i < 0 || j < 0 || i >= rows || j >= columns) continue;
                if (i == row && j == column) continue;

                open(i, j);
            }
        }
    }

    public boolean isMine(int row, int column) {
        return mines.contains(new Cell(row, column));
    }

    public boolean toOpen(int row, int column) {
        return openedCells.contains(new Cell(row, column));
    }

    private void generateMines(int startRow, int startCol) {
        Random rnd = new Random();

        while (!(mines.size() == minesCount)) {
            int row = rnd.nextInt(rows);
            int col = rnd.nextInt(columns);
            //Cell mine = new Cell(rnd.nextInt(rows), rnd.nextInt(columns));
            if (Math.abs(row - startRow) <= 1 && Math.abs(col - startCol) <= 1) {
                continue;
            }

            Cell mine = new Cell(row, col);
            if (!mines.contains(mine)) {
                mines.add(mine);
            }
        }
    }
}
