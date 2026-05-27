package Game;

public class Runner {
    static void main() {
        int rows = 9;
        int columns = 9;
        int mines = 10;

        new Gui(rows, columns, new Game(rows, columns, mines)).show();
    }
}
