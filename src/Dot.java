import java.awt.*;

public class Dot implements Drawable {
    private int x;
    private int y;
    private Color color;
    boolean notCaptured = true;

    Dot(int col, int row, Color color) {
        this.x = col * DotGameConstant.gridCellSize;
        this.y = row * DotGameConstant.gridCellSize;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - DotGameConstant.dotSize / 2, y - DotGameConstant.dotSize / 2, DotGameConstant.dotSize, DotGameConstant.dotSize);
        if (notCaptured) g.drawString("" + id(), x, y);
    }

    Color getColor() {
        return color;
    }

    boolean isNotCaptured() {
        return notCaptured;
    }

    void capture() {
        notCaptured = false;
    }

    int id() {
        return x/DotGameConstant.gridCellSize+y*DotGameConstant.dimension/DotGameConstant.gridCellSize;
    }
}
