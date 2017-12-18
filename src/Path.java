import java.awt.*;
import java.util.Arrays;

public class Path implements Drawable{
    private int hash;
    final int[] path;
    double area;
    private Color color;
    Polygon polygon;

    Path(Path p, int last) { //path=array+int last
        path = new int[p.length() + 1];
        System.arraycopy(p.path, 0, path, 0, p.length());
        path[path.length - 1] = last;
        this.hash = p.hashCode() ^ last * 397;
    }

    Path(int[] array) {
        path = array;
        for (int i : path)
            hash = hash ^ i * 397;
    }

    private void setPolygon(){
        int[] xs = new int[path.length];
        int[] ys = new int[path.length];
        for (int i = 0; i < path.length; i++){
            xs[i] = path[i] % DotGameConstant.dimension;
            ys[i] = path[i] / DotGameConstant.dimension;}
        polygon= new Polygon(xs,ys,path.length);
    }

    boolean containsDot(int col,int row){
        if (polygon==null) setPolygon();
        return polygon.contains(col,row) && !containsVertex(col+row*DotGameConstant.dimension);
    }

    void setColor(Color color) {
        this.color = color;
    }

    Color getColor() {
        return color;
    }

    double getArea(){ //B.X*A.Y - A.X*B.Y
        if (area!=0.0) return area;
        double sum = 0.0;
        for (int i = 0; i < path.length-1; i++) {
            sum = sum + (path[i+1]%DotGameConstant.dimension * path[i]/DotGameConstant.dimension) - (path[i]%DotGameConstant.dimension * path[i+1]/DotGameConstant.dimension);
        }
        sum+=(path[0]%DotGameConstant.dimension * path[path.length-1]/DotGameConstant.dimension) - (path[path.length-1]%DotGameConstant.dimension * path[0]/DotGameConstant.dimension);
        area=Math.abs(0.5 * sum);
        return area;
    }

    public String toString() {
        return Arrays.toString(path);
    }

    int length() {
        return path.length;
    }

    int start() {
        return path[0];
    }

    int last() {
        return path[path.length - 1];
    }

    boolean containsVertex(int a) {
        for (int i : path) {
            if (i == a) return true;
        }
        return false;
    }

    int[] limitX() {
        int[] xs = new int[path.length];
        for (int i = 0; i < path.length; i++)
            xs[i] = path[i] % DotGameConstant.dimension;
        Arrays.sort(xs);
        return new int[]{xs[0], xs[xs.length - 1]};
    }

    int[] limitY() {
        int[] ys = new int[path.length];
        for (int i = 0; i < path.length; i++)
            ys[i] = path[i] / DotGameConstant.dimension;
        Arrays.sort(ys);
        return new int[]{ys[0], ys[ys.length - 1]};
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        int x, y;
        int x1 = 0;
        int y1 = 0;
        for (int i = 0; i < path.length - 1; i++) {
            x = path[i] % DotGameConstant.dimension * DotGameConstant.gridCellSize;
            y = path[i] / DotGameConstant.dimension * DotGameConstant.gridCellSize;
            x1 = path[i + 1] % DotGameConstant.dimension * DotGameConstant.gridCellSize;
            y1 = path[i + 1] / DotGameConstant.dimension * DotGameConstant.gridCellSize;
            g.drawLine(x, y, x1, y1);
        }
        x = path[0] % DotGameConstant.dimension * DotGameConstant.gridCellSize;
        y = path[0] / DotGameConstant.dimension * DotGameConstant.gridCellSize;
        g.drawLine(x, y, x1, y1);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Path that = (Path) obj;
        return hash == that.hashCode();
    }
}
