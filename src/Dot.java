import java.awt.*;

public class Dot implements Drawable {
    int id;
    int x;
    int y;
    Color color;
    boolean isAvailable = true;

    public Dot(int id, int col, int row, Color color) {
        this.id = id;
        this.x = col * DotGameConstant.gridCellSize;
        this.y = row * DotGameConstant.gridCellSize;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        ((Graphics2D)g).setStroke(new BasicStroke(3.0f));
        g.fillOval(x - DotGameConstant.dotSize / 2, y - DotGameConstant.dotSize / 2, DotGameConstant.dotSize, DotGameConstant.dotSize);
        g.drawString("" + id, x, y);
    }

    public Color getColor() {
        return color;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void disable() {
        isAvailable = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int id() {
        return id;
    }
}
