import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class DotGamePanel extends JPanel {
    ArrayList<Dot> dots=new ArrayList<Dot>();
//    ArrayList<Polygon> polygons=new ArrayList<>();
    int cellSize=DotGameConstant.gridCellSize;

    public DotGamePanel(){
        setPreferredSize(new Dimension(cellSize * (DotGameConstant.dimension - 1), cellSize * (DotGameConstant.dimension - 1)));
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Dot d:dots) d.draw(g);
//        for (Polygon p:polygons) g.drawPolygon(p);
    }

    public void addDotsForDraw(Dot dot){
        dots.add(dot);
    }


}
