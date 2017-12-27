import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

class DotsScreen implements Serializable {
    transient JFrame frame=new JFrame();
    transient private DotGamePanel drawArea = new DotGamePanel();
    transient private JLabel colorFlag = new JLabel("Switch color");
    transient private JButton endGame = new JButton("End game");
    private int redDotCount, blueDotCount;
    transient private JLabel redDots = new JLabel("Red " + redDotCount);
    transient private JLabel blueDots = new JLabel("Blue " + blueDotCount);
    private Color currentColor = DotGameConstant.RED;
    private Dot[][] dots = new Dot[DotGameConstant.dimension][DotGameConstant.dimension];
    private DotGraph connections = new DotGraph(dots);

    public void buildGUI(){
        setMenu();
        JPanel emptyPanel=new JPanel();
        frame.add(emptyPanel);
        emptyPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20)); //add space between border and content elements;
        emptyPanel.add(drawArea);
        endGame.addActionListener(e -> showEndGamePanel());
        colorFlag.setBackground(DotGameConstant.RED);
        colorFlag.setOpaque(true); //make label color visible
        redDots.setForeground(DotGameConstant.RED);
        blueDots.setForeground(DotGameConstant.BLUE);
        drawArea.addObjectForDraw(connections);
        drawArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int col = getColRow(e.getX());
                int row = getColRow(e.getY());
                if (dots[col][row] == null) putDot(col, row);
            }
        });
        frame.setResizable(false);
        frame.setTitle("Dots Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //stop execute code when user close frame window;
        frame.pack(); //organize panels, sizes
        frame.setVisible(true);
    }

    private void setMenu() {
        JMenuBar menu = new JMenuBar();
        menu.add(colorFlag);
        menu.add(new JMenu("   "));
        menu.add(redDots);
        menu.add(new JMenu("   "));
        menu.add(blueDots);
        menu.add(new JMenu(" | "));
        menu.add(endGame);
        menu.add(new JMenu("   "));
        JButton save=new JButton("Save game");
        save.addActionListener(e->save());
        menu.add(save);
        JButton load=new JButton("Load game");
        load.addActionListener(e->load());
        menu.add(load);
        frame.setJMenuBar(menu);
    }

    void save(){
        try{
        FileOutputStream file=new FileOutputStream("LastGame.ser");
            ObjectOutputStream os=new ObjectOutputStream(file);
            os.writeObject(this);
    } catch (Exception ex){ex.printStackTrace();}}

    void load(){
        try{
            FileInputStream file=new FileInputStream("LastGame.ser");
            ObjectInputStream os=new ObjectInputStream(file);
            DotsScreen saver=(DotsScreen) os.readObject();
            resetGameField(saver.redDotCount,saver.blueDotCount,saver.currentColor,saver.dots,saver.connections);
            for (Dot[] array:dots)
                for (Dot d:array){
                if (d!=null) drawArea.addObjectForDraw(d);
                }
        } catch (Exception ex){ex.printStackTrace();}
    }

    private void resetGameField(int redDot,int blueDot,Color color,Dot[][] dot,DotGraph graph){
        redDotCount=redDot;
        blueDotCount=blueDot;
        currentColor=color;
        dots=dot;
        connections=graph;

        colorFlag.setBackground(currentColor);
        updateLabels();
        drawArea.clear();
        drawArea.addObjectForDraw(connections);
        frame.repaint();
    }

    private void showEndGamePanel() {
        String winner;
        if (redDotCount > blueDotCount) winner = "RED wins!";
        else if (blueDotCount > redDotCount) winner = "BLUE wins!";
        else winner = "Draw";
        Object[] options = {"New Game",
                "Exit"};
        int n = JOptionPane.showOptionDialog(frame,
                winner,
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
        if (n == 0) {
            resetGameField(0,0,DotGameConstant.RED,new Dot[DotGameConstant.dimension][DotGameConstant.dimension],new DotGraph(dots));
        } else frame.dispose();
    }

    void putDot(int col, int row) {
        dots[col][row] = new Dot(col, row, currentColor);
        connections.addDot();
        addConnections(col, row);
        drawArea.addObjectForDraw(dots[col][row]);
        int result = connections.findNewCycle(dots[col][row].id(), currentColor);
        if (result > 0) {
            if (currentColor.equals(DotGameConstant.RED)) redDotCount += result;
            else blueDotCount += result;
        }
        updateLabels();
        frame.repaint();
        changeCurrentColor();
        if (connections.isFull()) showEndGamePanel();
    }

    private void addConnections(int col, int row) {
        //-1,0,1 - closet dot on grid
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i == 0 && j == 0)) {
                    continue;
                } else if (col + i < 0 || col + i > DotGameConstant.dimension-1 || row + j < 0 || row + j > DotGameConstant.dimension-1) {
                    continue;
                }
                Dot dot = dots[col + i][row + j];
                if (dot != null && dot.notCaptured && dot.getColor().equals(currentColor))
                    connections.addEdge(dots[col][row].id(), dot.id());
            }
        }
    }

    private void changeCurrentColor() {
        if (currentColor.equals(DotGameConstant.RED)) currentColor = DotGameConstant.BLUE;
        else currentColor = DotGameConstant.RED;
        colorFlag.setBackground(currentColor);
    }

    private int getColRow(int i) {
        return (i + DotGameConstant.gridCellSize / 2) / DotGameConstant.gridCellSize;
    }

    private void updateLabels() {
        redDots.setText("Red " + redDotCount);
        blueDots.setText("Blue " + blueDotCount);
    }
}