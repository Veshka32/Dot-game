import java.awt.*;
import java.util.Arrays;

public class Path implements Drawable{
    private int hash;
    private final int[] path;
    private Color color;
    private Polygon polygon;
    private Polygon polygonForDraw;

    Path(Path p, int last) { //path=array+last
        path = new int[p.size() + 1];
        System.arraycopy(p.path, 0, path, 0, p.size());
        path[path.length - 1] = last;
        this.hash = p.hashCode() ^ last * 397;
    }

    Path(int[] array) {
        path = array;
        for (int i : path)
            hash = hash ^ i * 397;
    }

    void setPolygon(){
        int[] xs = new int[path.length];
        int[] ys = new int[path.length];
        int[] xsScaled=new int[path.length];
        int[] ysScaled=new int[path.length];
        for (int i = 0; i < path.length; i++){
            xs[i] = path[i] % DotGameConstant.dimension;
            ys[i] = path[i] / DotGameConstant.dimension;
            xsScaled[i] = path[i] % DotGameConstant.dimension*DotGameConstant.gridCellSize;
            ysScaled[i] = path[i] / DotGameConstant.dimension*DotGameConstant.gridCellSize;
        }
        polygon= new Polygon(xs,ys,path.length);
        polygonForDraw=new Polygon(xsScaled,ysScaled,path.length);
    }

    boolean containsDot(int col,int row){
        if (polygon==null) setPolygon();
        return polygon.contains(col,row);
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

    int size() {
        return path.length;
    }

    int start() {
        return path[0];
    }

    int last() {
        return path[path.length - 1];
    }

    boolean contains(int a) {
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
}
