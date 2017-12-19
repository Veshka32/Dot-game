import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DotGamePanel extends JPanel {
    private ArrayList<Drawable> objectsForDraw=new ArrayList<>();

    DotGamePanel(){
        setPreferredSize(new Dimension(DotGameConstant.gridCellSize * (DotGameConstant.dimension - 1), DotGameConstant.gridCellSize * (DotGameConstant.dimension - 1)));
        setBackground(Color.WHITE);
        //setBorder(BorderFactory.createLineBorder(Color.black));
        setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f)));
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
