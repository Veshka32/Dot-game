import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

public class DotGamePanel extends JPanel {
    ArrayList<Drawable> objectsForDraw=new ArrayList<>();
    int cellSize=DotGameConstant.gridCellSize;

    public DotGamePanel(){
        setPreferredSize(new Dimension(cellSize * (DotGameConstant.dimension - 1), cellSize * (DotGameConstant.dimension - 1)));
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Drawable d:objectsForDraw) d.draw(g);
    }

    public void addObjectForDraw(Drawable dr){
        objectsForDraw.add(dr);
    }


}
