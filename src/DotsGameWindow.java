import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class DotsGameWindow extends JFrame {
    int cellSize=DotGameConstant.gridCellSize;
    private Container cp = getContentPane(); ///top-level container
    private JPanel drawArea = new JPanel();
    private JButton changeColor = new JButton("Switch color");
    private JButton drawPolygon = new JButton("Draw polygon");
    private JButton endGame=new JButton("End game");
    int redDotCount;
    int blueDotCount;
    private JLabel redDots = new JLabel("Red " + redDotCount);
    private JLabel blueDots = new JLabel("Blue " + blueDotCount);
    private Color currentColor = DotGameConstant.RED;
    Graphics canvas;

    Color[][] dots = new Color[DotGameConstant.dimension][DotGameConstant.dimension];
    int prevX, prevY, polygonXstart, polygonYstart;
    boolean validStartForSegment = true;
    SortedSet<Integer> xx = new TreeSet<>();
    SortedSet<Integer> yy = new TreeSet<>();
    int drawnLineCount = 0;
    ArrayList<Integer> segmentsX = new ArrayList<>();
    ArrayList<Integer> segmentsY = new ArrayList<>();

    State state = State.DRAW_DOT;
    public enum State {
        DRAW_DOT, DRAW_POLYGON,END_GAME
    }

    public DotsGameWindow() {
        setMenu();
        cp.setLayout(new BorderLayout()); //default arrange components
        endGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEndGamePanel();
            }
        });
        changeColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeCurrentColor();
                if (segmentsX.size() > 0) eraseSegments();
            }
        });
        drawPolygon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = State.DRAW_POLYGON;
            }
        });
        changeColor.setBackground(Color.RED);
        drawArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = getGridCoordinate(e.getX());
                int y = getGridCoordinate(e.getY());
                switch (state) {
                    case DRAW_DOT:
                        if (isHereDot(x / cellSize, y / cellSize)) return;
                        putDot(x / cellSize, y / cellSize); //to constant
                        break;
                    case DRAW_POLYGON:
                        if (!isHereDot(x / cellSize, y / cellSize) || getDotColor(x / cellSize, y / cellSize) != currentColor) return;
                        if (drawnLineCount == 0) {
                            xx.add(x);
                            yy.add(y);
                            segmentsX.add(x);
                            segmentsY.add(y);
                            polygonXstart = x;
                            polygonYstart = y;
                            prevX = x;
                            prevY = y;
                            validStartForSegment = true;
                        } else validStartForSegment = (x == prevX && y == prevY);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (state != State.DRAW_POLYGON || !validStartForSegment) return;
                int x = getGridCoordinate(e.getX());
                int y = getGridCoordinate(e.getY());
                drawSegment(x, y);
            }
        });

        setResizable(false);
        drawArea.setPreferredSize(new Dimension(cellSize * (DotGameConstant.dimension - 1), cellSize * (DotGameConstant.dimension - 1)));
        drawArea.setBackground(Color.WHITE);
        add(drawArea);
        setTitle("Dots Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //stop execute code when user close frame window;
        pack(); //organize panels, sizes
        setVisible(true);
        revalidate();
        canvas = cp.getGraphics(); //getGraphics must be called only after at least one rendering
        canvas.setColor(currentColor);
    }

    public void setMenu(){
        JMenuBar menu=new JMenuBar();
        menu.add(changeColor);
        menu.add(new JMenu("   "));
        menu.add(drawPolygon);
        menu.add(new JMenu("   "));
        menu.add(redDots);
        menu.add(new JMenu("   "));
        menu.add(blueDots);
        menu.add(new JMenu("   "));
        menu.add(endGame);
        setJMenuBar(menu);
    }

    public void showEndGamePanel(){
        String winner;
        if (redDotCount>blueDotCount) winner="RED wins!";
        else if (blueDotCount>redDotCount) winner="BLUE wins!";
        else winner="Ничья";
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
        if (n==0) {dispose();
            new DotsGameWindow();}
        else dispose();
    }

    public boolean isHereDot(int col, int row) {
        return dots[col][row] != null;
    }

    public void eraseSegments() {
        for (int i = 0; i < segmentsX.size() - 1; i++) {
            canvas.setColor(Color.WHITE);
            canvas.drawLine(segmentsX.get(i), segmentsY.get(i), segmentsX.get(i + 1), segmentsY.get(i + 1));
        }
        canvas.setColor(currentColor);
        segmentsX.clear();
        segmentsY.clear();
        state = State.DRAW_DOT;
    }

    public void drawSegment(int x, int y) {
        if (dots[x / cellSize][y / cellSize] != currentColor || !isValidDist(x, y, prevX, prevY)) return;
        canvas.drawLine(prevX, prevY, x, y);
        drawnLineCount++;
        xx.add(x);
        yy.add(y);
        if (x == polygonXstart && y == polygonYstart) closePolygon();
        else {
            prevX = x;
            prevY = y;
            segmentsX.add(x);
            segmentsY.add(y);
        }
    }

    public void closePolygon() {
        state = State.DRAW_DOT;
        drawnLineCount = 0;
        int minCol = xx.first() / cellSize;
        int maxCol = xx.last() / cellSize;
        int minRow = yy.first() / cellSize;
        int maxRow = yy.last() / cellSize;
        for (int col = minCol + 1; col < maxCol; col++) {
            for (int row = minRow + 1; row < maxRow; row++) {
                if (isHereDot(col, row) && getDotColor(col, row) != currentColor) {
                    changeColorCount();
                    putDot(col, row);
                }
            }
        }
        xx.clear();
        yy.clear();
        segmentsX.clear();
        segmentsY.clear();
    }

    public void putDot(int col, int row) {
        dots[col][row] = currentColor;
        if (currentColor == DotGameConstant.RED) redDotCount++;
        else blueDotCount++;
        updateLabels();
        drawDot(col * DotGameConstant.gridCellSize, row * DotGameConstant.gridCellSize);
    }

    public void drawDot(int x, int y) {
        canvas.fillOval(x - DotGameConstant.dotSize / 2, y - DotGameConstant.dotSize / 2, DotGameConstant.dotSize, DotGameConstant.dotSize);
    }

    public Color getDotColor(int col, int row) {
        if (!isHereDot(col, row)) throw new IllegalArgumentException("there is no any dot");
        return dots[col][row];
    }

    public void changeCurrentColor() {
        if (currentColor == DotGameConstant.RED) currentColor = DotGameConstant.BLUE;
        else currentColor = DotGameConstant.RED;
        canvas.setColor(currentColor);
        changeColor.setBackground(currentColor);
    }

    public void changeColorCount() {
        if (currentColor == DotGameConstant.RED) {
            blueDotCount--;
        } else {
            redDotCount--;
        }
        updateLabels();
    }

    private void updateLabels() {
        redDots.setText("Red " + redDotCount);
        blueDots.setText("Blue " + blueDotCount);
    }

    public int getGridCoordinate(int i) {
        return ((i + DotGameConstant.gridCellSize / 2) / DotGameConstant.gridCellSize) * DotGameConstant.gridCellSize;
    }

    public boolean isValidDist(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) / cellSize < 2 && Math.abs(y1 - y2) / cellSize < 2;
    }
}
