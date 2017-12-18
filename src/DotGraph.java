import java.awt.*;
import java.util.*;
import java.util.List;

public class DotGraph implements Drawable {
    private int total = DotGameConstant.dimension * DotGameConstant.dimension;
    ArrayList<Integer>[] adj;
    private HashSet<Path> cycles = new HashSet<>();
    private int V = 0; //count of vertexes added to gameBoard so far
    private Dot[][] dots;

    DotGraph(Dot[][] dots) {
        adj = new ArrayList[total];
        for (int i = 0; i < total; i++) {
            adj[i] = new ArrayList<>();
        }
        this.dots = dots;
    }

    DotGraph() {
        adj = new ArrayList[total];
        for (int i = 0; i < total; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    void addDot() {
        V++;
    }

    void disableDot(int v) {
        int col = v % DotGameConstant.dimension;
        int row = v / DotGameConstant.dimension;
        for (int i : adj[v]) {
            adj[i].remove((Integer) v);
        }
        adj[v].clear();
        if (dots != null) dots[col][row].disable();
    }

    private ArrayList<Integer>[] findInnerDots(Path p) {
        ArrayList<Integer>[] innerDots = new ArrayList[]{new ArrayList(), new ArrayList()}; //[0] - opposite color,[1] - same color
        int[] xx = p.limitX();
        int[] yy = p.limitY();
        for (int col = xx[0] + 1; col < xx[1]; col++) {
            for (int row = yy[0] + 1; row < yy[1]; row++) {
                Dot current = dots[col][row];
                if (current != null && p.containsDot2(col, row) && current.isAvailable()) {
                    if (current.getColor() != p.getColor()) innerDots[0].add(current.id());
                    else {if (!p.containsVertex(current.id())) innerDots[1].add(current.id());} //if color the same, it might be boundary point
                }
            }
        }
        return innerDots;
    }

    void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }

    private ArrayList<Integer> getAdjacent(int v) {
        Collections.sort(adj[v], Comparator.comparingInt(o -> manhattanDist(v, o))); //remove to Path Class
        return adj[v];
    }

    int findNewCycle(int v, Color color) {
        if (V < 7) return 0;
        ArrayList<Integer>[] innerDotsSoFar = new ArrayList[]{new ArrayList(), new ArrayList()}; //to list
        Path newCycles = null;
        ArrayDeque<Path> paths = new ArrayDeque<>();
        paths.add(new Path(new int[]{v}));
        while (!paths.isEmpty()) {
            Path path = paths.pop();
            if (path.equals(newCycles) || cycles.contains(path)) continue;
            int last = path.last();
            List<Integer> adj = getAdjacent(last);
            for (int w : adj) {
                if (path.length() > 1) {
                    if (path.length() > 3 && w == path.start()) {
                        path.setColor(color);
                        ArrayList<Integer>[] capturedDots = findInnerDots(path);
                        if (capturedDots[0].size() < 1) continue;
                        if (capturedDots[0].size() > innerDotsSoFar[0].size() || (capturedDots[0].size() == innerDotsSoFar[0].size() && path.getArea() > newCycles.getArea())) {
                            newCycles = path;
                            innerDotsSoFar = capturedDots;
                        }
                        continue;
                    }
                    if (path.containsVertex(w)) continue;
                }
                Path newPath = new Path(path, w);
                paths.add(newPath);
            }
        }
        if (innerDotsSoFar[0].isEmpty()) return 0;
        cycles.add(newCycles);
        for (ArrayList<Integer> array : innerDotsSoFar)
            for (int i : array)
                disableDot(i);
        return innerDotsSoFar[0].size();
    }

    private static int manhattanDist(int a, int b) {
        int col = a % DotGameConstant.dimension;
        int row = a / DotGameConstant.dimension;
        int col2 = b % DotGameConstant.dimension;
        int row2 = b / DotGameConstant.dimension;
        return Math.abs(col - col2) + Math.abs(row - row2);
    }

    @Override
    public void draw(Graphics g) {
        ((Graphics2D) g).setStroke(new BasicStroke(3.0f));
        for (Path p : cycles)
            p.draw(g);
    }
}
