package com.DotGame;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;

public class Path implements Drawable,Serializable {
    private int hash;
    private final int[] path;
    private int xs[];
    private int ys[];
    private double area;
    private Color color;

    Path(Path p, int last) { //path=array+int last
        path = new int[p.length() + 1];
        System.arraycopy(p.path, 0, path, 0, p.length());
        path[path.length - 1] = last;
        this.hash = p.hashCode() ^ last * 397;
    }

    Path(int[] array) {
        if (array==null) throw new IllegalArgumentException();
        path = array;
        for (int i : path)
            hash = hash ^ i * 397;
    }

    private void setXsYs() {
        xs = new int[path.length];
        ys = new int[path.length];
        for (int i = 0; i < path.length; i++) {
            xs[i] = path[i] % DotGameConstant.dimension;
            ys[i] = path[i] / DotGameConstant.dimension;
        }
    }

    boolean containsDot2(int col, int row) {
        if (xs == null) setXsYs();
        int crossings = 0;
        for (int i = 0; i < path.length; i++) {
            int j = i + 1;
            if (i + 1 == path.length) j = 0;
            boolean cond1 = (ys[i] <= row) && (row < ys[j]);
            boolean cond2 = (ys[j] <= row) && (row < ys[i]);
            if (cond1 || cond2) {
                // need to cast to double
                if (col < (xs[j] - xs[i]) * (row - ys[i]) / (ys[j] - ys[i]) + xs[i])
                    crossings++;
            }
        }
        return crossings % 2 == 1;
    }

    void setColor(Color color) {
        this.color = color;
    }

    Color getColor() {
        return color;
    }

    double getArea() { //B.X*A.Y - A.X*B.Y
        if (xs == null) setXsYs();
        if (area > 0.0) return area;
        double sum = 0.0;
        for (int i = 0; i < path.length - 1; i++) {
            sum = sum + (xs[i + 1] * ys[i]) - (xs[i] * ys[i + 1]);
        }
        sum += (xs[0] * ys[ys.length - 1]) - (xs[xs.length - 1] * ys[0]);
        area = Math.abs(0.5 * sum);
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

    boolean hasCommonVertex(Path p) { // @p - shorter path
        int count = 0;
        for (int i : p.path)
            if (containsVertex(i)) {
                count++;
                if (count > 1) return true;
            }
        return false;
    }

    int[] limitX() {
        if (xs == null) setXsYs();
        int min = DotGameConstant.dimension * DotGameConstant.dimension + 2;
        int max = -1;
        for (int i : xs) {
            if (i < min) min = i;
            if (i > max) max = i;
        }
        return new int[]{min, max};
    }

    int[] limitY() {
        if (xs == null) setXsYs();
        int min = DotGameConstant.dimension * DotGameConstant.dimension + 2;
        int max = -1;
        for (int i : ys) {
            if (i < min) min = i;
            if (i > max) max = i;
        }
        return new int[]{min, max};
    }

    @Override
    public void draw(Graphics g) {
        if (xs == null) setXsYs();
        g.setColor(color);
        int x, y;
        int x1 = 0;
        int y1 = 0;
        for (int i = 0; i < path.length - 1; i++) {
            x = xs[i] * DotGameConstant.gridCellSize;
            y = ys[i] * DotGameConstant.gridCellSize;
            x1 = xs[i + 1] * DotGameConstant.gridCellSize;
            y1 = ys[i + 1] * DotGameConstant.gridCellSize;
            g.drawLine(x, y, x1, y1);
        }
        x = xs[0] * DotGameConstant.gridCellSize;
        y = ys[0] * DotGameConstant.gridCellSize;
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
