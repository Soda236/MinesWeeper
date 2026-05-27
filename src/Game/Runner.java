package Game;

public class Runner {
    static void main() {
        int rows = 20;
        int columns = 20;

        new Gui(rows, columns, new Game(rows, columns, 20)).show();
    }
}
