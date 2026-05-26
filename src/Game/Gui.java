package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Gui {

    private static final int CELL_SIZE = 30;

    private static final Image MINE_IMAGE =
        new ImageIcon(Gui.class.getResource("/mine.png")).getImage();

    private final int columns;
    private final int rows;
    private final Game game;

    private Board board;

    private final JFrame frame = new JFrame();

    public Gui(int columns, int rows, Game game) {
        this.columns = columns;
        this.rows = rows;
        this.game = game;
    }

    public void show() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");
        frame.setResizable(false);

        int width = this.columns * CELL_SIZE;
        int height = this.rows * CELL_SIZE;

        board = new Board();
        board.setPreferredSize(new Dimension(width, height));

        board.addMouseListener(new MouseListener(this));
        board.addMouseMotionListener(new MouseListener(this));

        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(0, 40));

        JButton button = new JButton("Restart");
        button.setBounds(0, 0, 50, 20);
        button.setFocusable(false);
        button.addActionListener(e -> restart());
        header.add(button);

        frame.add(board, BorderLayout.CENTER);
        frame.add(header, BorderLayout.NORTH);

        frame.pack();

        frame.setVisible(true);
    }

    private void restart() {
        game.restart();
        board.repaint();
    }

    private final class Board extends JPanel {

        private int rowHover = -1;
        private int columnHover = -1;

        @Override
        public void paintComponent(Graphics graphics) {
            if (!(graphics instanceof Graphics2D g)) {
                throw new IllegalArgumentException("should be Graphics2D");
            }

            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    boolean isOpen = game.toOpen(row, column);

                    drawCell(row, column, isOpen, g);
                }
            }
        }

        private void drawCell(int row, int column, boolean isOpen, Graphics2D g) {

            Cell coordinates = cellToPixels(row, column);

            boolean isHover = row == rowHover && column == columnHover;

            int x = coordinates.x();
            int y = coordinates.y();
            int s = CELL_SIZE;

            if (!isOpen) {

                g.setPaint(isHover ? new Color(200, 200, 200) : Color.LIGHT_GRAY);
                g.fillRect(x, y, s, s);

                g.setPaint(Color.WHITE);
                g.drawLine(x, y, x + s - 1, y);
                g.drawLine(x, y, x, y + s - 1);

                g.setPaint(Color.DARK_GRAY);
                g.drawLine(x, y + s - 1, x + s - 1, y + s - 1);
                g.drawLine(x + s - 1, y, x + s - 1, y + s - 1);

            } else {
                g.setPaint(Color.GRAY);
                g.fillRect(x, y, s, s);

                g.setPaint(Color.DARK_GRAY);
                g.drawRect(x, y, s, s);

                if (game.isMine(row, column)) {
                    g.setColor(Color.RED);
                    g.fillRect(x, y, s, s);
                    g.drawImage(MINE_IMAGE, x, y, s, s, null);

                    game.over();
                    board.repaint();
                } else {
                    int count = game.minesCount(row, column);
                    switch (count) {
                        case 1:
                            g.setColor(Color.BLUE);
                            break;
                        case 2:
                            g.setColor(Color.GREEN);
                            break;
                        case 3:
                            g.setColor(Color.RED);
                            break;
                        case 4:
                            g.setColor(new Color(120, 75, 205));
                            break;
                        case 5:
                            g.setColor(Color.yellow);
                            break;
                        case 6:
                            g.setColor(Color.pink);
                            break;
                        case 7:
                            g.setColor(new Color(255, 168, 40));
                            break;
                        case 8:
                            g.setColor(Color.BLACK);
                            break;
                        default:
                            g.setColor(new Color(0, 0, 0, 0));
                    }
                    g.drawString(Integer.toString(count), x + 10, y + 20);
                    g.setFont(new Font("Arial", Font.BOLD, 18));
                }
            }
        }

        private Cell cellToPixels(int row, int column) {
            int offsetX = column * CELL_SIZE;
            int offsetY = row * CELL_SIZE;

            return new Cell(offsetX, offsetY);
        }
    }

    private static class MouseListener extends MouseAdapter {

        private final Gui gui;

        public MouseListener(Gui gui) {
            this.gui = gui;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = (e.getY() / CELL_SIZE);
            int column = (e.getX() / CELL_SIZE);

            gui.game.open(row, column);
            gui.board.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            gui.board.columnHover = (e.getX() / CELL_SIZE);
            gui.board.rowHover = (e.getY() / CELL_SIZE);
            gui.board.repaint();
        }


        @Override
        public void mouseExited(MouseEvent e) {
            gui.board.rowHover = -1;
            gui.board.columnHover = -1;
            gui.board.repaint();
        }
    }
}


