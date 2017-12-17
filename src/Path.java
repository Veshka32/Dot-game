import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Path implements Drawable{
    private int hash;
    private final int[] path;
    private Color color;
    private Polygon polygon;
    private Polygon polygonForDraw;

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
        int[] xsScaled=new int[path.length];
        int[] ysScaled=new int[path.length];
        for (int i = 0; i < path.length; i++){
            xs[i] = path[i] % DotGameConstant.dimension;
            ys[i] = path[i] / DotGameConstant.dimension;
            xsScaled[i] = xs[i]*DotGameConstant.gridCellSize;
            ysScaled[i] = ys[i]*DotGameConstant.gridCellSize;
        }
        polygon= new Polygon(xs,ys,path.length);
        polygonForDraw=new Polygon(xsScaled,ysScaled,path.length);
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
        ((Graphics2D)g).setStroke(new BasicStroke(3.0f));
        if (polygonForDraw==null) setPolygon();
        g.drawPolygon(polygonForDraw);
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

    public int compareTo(Path o) {
        if (o==null) return 1;
        return Integer.compare(this.length(), o.length());
    }
}
