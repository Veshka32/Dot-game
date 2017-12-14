import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DotsScreen extends JFrame {
    private DotGamePanel drawArea = new DotGamePanel();
    private JButton changeColor = new JButton("Switch color");
    private JButton endGame = new JButton("End game");
    int redDotCount, blueDotCount;
    int dim = DotGameConstant.dimension;
    private JLabel redDots = new JLabel("Red " + redDotCount);
    private JLabel blueDots = new JLabel("Blue " + blueDotCount);
    public Color currentColor = DotGameConstant.RED;

    Dot[][] dots = new Dot[DotGameConstant.dimension][DotGameConstant.dimension];
    DotGraph connections = new DotGraph(DotGameConstant.dimension * DotGameConstant.dimension);

    public DotsScreen() {
        setMenu();
        endGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEndGamePanel();
            }
        });
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

    public void setMenu() {
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

    public void showEndGamePanel() {
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

    public int getPointNumber(int col,int row){
        return col+row*dim;
    }

    public void putDot(int col, int row) {
        int id = col + row * DotGameConstant.dimension;
        dots[col][row] = new Dot(id, col, row, currentColor);
        connections.addDot();
        addConnections(col, row);
        drawArea.addDotsForDraw(dots[col][row]);
        if (currentColor == DotGameConstant.RED) redDotCount++;
        else blueDotCount++;
        updateLabels();
        //changeCurrentColor();
        connections.findAllCycles(getPointNumber(col,row));
        for (Path p : connections.getCycles()) {
            drawArea.addPathForDraw(p);
        }
        repaint();
    }

    public void addConnections(int col, int row) {
        int n=getPointNumber(col,row);
        int[] surrounded = {(0 - dim), dim, -1, 1, (-1 - dim), (1 - dim), (dim - 1), (dim + 1)};
        for (int i : surrounded) {
            int m=n+i;
            try {
                Dot dot = dots[m%dim][m/dim];
                if (dot != null && dot.isAvailable && dot.getColor() == currentColor)
                    connections.addEdge(dots[col][row].id(), dot.id());
            } catch (IndexOutOfBoundsException e) {
            }
        }
    }

    public void changeCurrentColor() {
        if (currentColor == DotGameConstant.RED) currentColor = DotGameConstant.BLUE;
        else currentColor = DotGameConstant.RED;
        changeColor.setBackground(currentColor);
    }

    public int getColRow(int i) {
        return (i + DotGameConstant.gridCellSize / 2) / DotGameConstant.gridCellSize;
    }

    private void updateLabels() {
        redDots.setText("Red " + redDotCount);
        blueDots.setText("Blue " + blueDotCount);
    }
}
