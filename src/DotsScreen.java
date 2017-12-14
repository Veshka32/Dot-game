import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class DotsScreen extends JFrame {
    private DotGamePanel drawArea = new DotGamePanel();
    private JButton changeColor = new JButton("Switch color");
    private JButton endGame = new JButton("End game");
    private int redDotCount, blueDotCount;
    private int dim = DotGameConstant.dimension;
    private JLabel redDots = new JLabel("Red " + redDotCount);
    private JLabel blueDots = new JLabel("Blue " + blueDotCount);
    private Color currentColor = DotGameConstant.RED;
    private Dot[][] dots = new Dot[DotGameConstant.dimension][DotGameConstant.dimension];
    private DotGraph connections = new DotGraph(DotGameConstant.dimension * DotGameConstant.dimension, dots);

    DotsScreen() {
        setMenu();
        endGame.addActionListener(e -> showEndGamePanel());
        changeColor.setBackground(Color.RED);
        add(drawArea);
        drawArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int col = getColRow(e.getX());
                int row = getColRow(e.getY());
                if (dots[col][row] == null) putDot(col, row);
            }
        });
        setResizable(false);
        setTitle("Dots Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //stop execute code when user close frame window;
        pack(); //organize panels, sizes
        setVisible(true);
        repaint();
        revalidate();
    }

    private void setMenu() {
        JMenuBar menu = new JMenuBar();
        menu.add(changeColor);
        menu.add(new JMenu("   "));
        menu.add(redDots);
        menu.add(new JMenu("   "));
        menu.add(blueDots);
        menu.add(new JMenu("   "));
        menu.add(endGame);
        setJMenuBar(menu);
    }

    private void showEndGamePanel() {
        String winner;
        if (redDotCount > blueDotCount) winner = "RED wins!";
        else if (blueDotCount > redDotCount) winner = "BLUE wins!";
        else winner = "Draw";
        Object[] options = {"New Game",
                "Exit"};
        int n = JOptionPane.showOptionDialog(this,
                winner,
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
        if (n == 0) {
            dispose();
            new DotsScreen();
        } else dispose();
    }

    private void putDot(int col, int row) {
        int id = col + row * dim;
        dots[col][row] = new Dot(id, col, row, currentColor);
        connections.addDot();
        addConnections(col, row);
        drawArea.addObjectForDraw(dots[col][row]);
        CaptureResult result = connections.findNewCycle(id);
        if (result.size() > 0) {
            if (result.getColor() == Color.RED) redDotCount += result.size();
            else blueDotCount += result.size();
        }
        for (Path p : connections.getCycles()) {
            drawArea.addObjectForDraw(p);
        }
        updateLabels();
        repaint();
        changeCurrentColor();
    }

    private void addConnections(int col, int row) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) continue;
                try {
                    Dot dot = dots[col + i][row + j];
                    if (dot != null && dot.isAvailable && dot.getColor() == currentColor)
                        connections.addEdge(dots[col][row].id(), dot.id());
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }
    }

    private void changeCurrentColor() {
        if (currentColor == DotGameConstant.RED) currentColor = DotGameConstant.BLUE;
        else currentColor = DotGameConstant.RED;
        changeColor.setBackground(currentColor);
    }

    private int getColRow(int i) {
        return (i + DotGameConstant.gridCellSize / 2) / DotGameConstant.gridCellSize;
    }

    private void updateLabels() {
        redDots.setText("Red " + redDotCount);
        blueDots.setText("Blue " + blueDotCount);
    }
}
