import java.awt.*;
import java.util.Arrays;

public class Path {
    private int hash;
    private final int[] path;
    private Color color;

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

    void draw(Graphics g) {
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
