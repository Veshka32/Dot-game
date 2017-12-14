import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class DotGamePanel extends JPanel {
    ArrayList<Dot> dots=new ArrayList<>();
    ArrayList<Path> cycles=new ArrayList<>();
    int cellSize=DotGameConstant.gridCellSize;

    public DotGamePanel(){
        setPreferredSize(new Dimension(cellSize * (DotGameConstant.dimension - 1), cellSize * (DotGameConstant.dimension - 1)));
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Dot d:dots) d.draw(g);
        for (Path p:cycles) p.draw(g);
    }

    public void addDotsForDraw(Dot dot){
        dots.add(dot);
    }
    public void addPathForDraw(Path path){cycles.add(path);}


}
