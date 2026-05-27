package Game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Game {
    private final int rows;
    private final int columns;
    private final int minesCount;

    private final ArrayList<Cell> mines = new ArrayList<>();
    private final ArrayList<Cell> openedCells = new ArrayList<>();
    private final ArrayList<Cell> isFlagged = new ArrayList<>();
    public final ArrayList<Cell> wrong = new ArrayList<>();

    public boolean start = true;
    private boolean isOver = false;

    private Cell explodedCell;

    public Game(int rows, int columns, int minesCount) {
        this.minesCount = minesCount;
        this.rows = rows;
        this.columns = columns;
    }

    public boolean isWrong(int row, int column) {
        return wrong.contains(new Cell(row, column));
    }

    public int getRows() {
        return rows;
    }

    public int getColomns() {
        return columns;
    }

    public int getMines() {
        return minesCount;
    }

    public void flag(int row, int column) {
        Cell cell = new Cell(row, column);
        if (!isFlagged.contains(cell) && !openedCells.contains(cell)) {
            isFlagged.add(cell);
        } else {
            isFlagged.remove(cell);
        }
    }

    public int getFlagCount() {
        return isFlagged.size();
    }

    public boolean isWin() {
        return openedCells.size() == (rows * columns - minesCount);
    }

    public boolean isOver() {
        return isOver;
    }

    public void openAround(int row, int column) {
        int mineCount = 0;
        int flagCount = 0;
        ArrayList<Cell> temp = new ArrayList<>();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                Cell cell = new Cell(i, j);
                if (openedCells.contains(cell)) continue;
                if (i < 0 || j < 0 || i >= rows || j >= columns) continue;
                if (i == row && j == column) continue;

                temp.add(cell);
                if (isFlagged.contains(cell)) {
                    flagCount++;
                    temp.remove(cell);
                }
                if (mines.contains(cell)) {
                    mineCount++;
                }
            }
        }
        if (mineCount == flagCount) {
            for (Cell cell : temp) {
                open(cell.row(), cell.colomn());
            }
        }
    }

    public void restart() {
        mines.clear();
        openedCells.clear();
        isFlagged.clear();
        wrong.clear();

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
        if (isMine(row, column)) {
            over();
            explodedCell = cell;
            return;
        }
        if (openedCells.contains(cell) || isFlagged.contains(cell)) {
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

    public boolean isFlagged(int row, int column) {
        return isFlagged.contains(new Cell(row, column));
    }

    public boolean isMine(int row, int column) {
        return mines.contains(new Cell(row, column));
    }

    public boolean isOpen(int row, int column) {
        return openedCells.contains(new Cell(row, column));
    }

    public boolean isExploded(int row, int column) {
        return explodedCell != null && explodedCell.equals(new Cell(row, column));
    }

    private void over() {
        isOver = true;
        openedCells.addAll(mines);

        for (Cell cell : isFlagged) {
            if (!mines.contains(cell)) {
                wrong.add(cell);
            }
        }
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
