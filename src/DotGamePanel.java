import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

public class DotGamePanel extends JPanel {
    private ArrayList<Drawable> objectsForDraw=new ArrayList<>();

    DotGamePanel(){
        setPreferredSize(new Dimension(DotGameConstant.gridCellSize * (DotGameConstant.dimension - 1), DotGameConstant.gridCellSize * (DotGameConstant.dimension - 1)));
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Drawable d:objectsForDraw) d.draw(g);
    }

    void addObjectForDraw(Drawable dr){
        objectsForDraw.add(dr);
    }

    void clear(){objectsForDraw.clear();}


}
