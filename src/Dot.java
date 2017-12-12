import java.awt.*;

public class Dot {
    int id;
    int x;
    int y;
    Color color;
    boolean isAvailable=true;

    public Dot(int id,int col,int row,Color color){
        this.id=id;
        this.x=col*DotGameConstant.gridCellSize;
        this.y=row*DotGameConstant.gridCellSize;
        this.color=color;
    }

    public void draw(Graphics g){
        g.setColor(color);
        g.fillOval(x - DotGameConstant.dotSize / 2, y - DotGameConstant.dotSize / 2, DotGameConstant.dotSize, DotGameConstant.dotSize);
        g.drawString(""+id,x,y);
    }

    public Color getColor(){return color;}
    public boolean isAvailable(){return isAvailable;}
    public int getX(){return x;}
    public int getY() {
        return y;
    }
    public int id(){return id;}
}
