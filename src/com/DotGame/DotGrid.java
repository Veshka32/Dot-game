import java.awt.*;

public class DotGrid implements Drawable {
    final float dots[] = {1.0f,2.0f};
    final BasicStroke dotted =
            new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dots, 0);

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        ((Graphics2D) g).setStroke(dotted);

        for (int i = DotGameConstant.gridCellSize; i < DotGameConstant.gridCellSize * DotGameConstant.dimension; i += DotGameConstant.gridCellSize) {
            g.drawLine(0, i, DotGameConstant.gridCellSize * DotGameConstant.dimension, i);
        }

        for (int i = DotGameConstant.gridCellSize; i < DotGameConstant.gridCellSize * DotGameConstant.dimension; i += DotGameConstant.gridCellSize) {
            g.drawLine(i, 0, i, DotGameConstant.gridCellSize * DotGameConstant.dimension);
        }
    }
}
