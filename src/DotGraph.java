import java.util.*;
import java.util.List;

public class DotGraph {
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

    private ArrayList<Integer> findCapturedDots(Path p) {
        ArrayList<Integer> innerDots = new ArrayList<>();
        int[] xx = p.limitX();
        int[] yy = p.limitY();
        for (int col = xx[0] + 1; col < xx[1]; col++) {
            for (int row = yy[0] + 1; row < yy[1]; row++) {
                Dot current = dots[col][row];
                if (current != null && p.containsDot(col, row) && current.getColor() != p.getColor() && current.isAvailable()) {
                    innerDots.add(current.id());
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
        return adj[v];
    }

    CaptureResult findNewCycle(int v) {
        if (V < 4) return new CaptureResult();
        int currentMaxOfCapturedDots = 0;
        Path newCycles = null;
        ArrayDeque<Path> paths = new ArrayDeque<>();
        paths.add(new Path(new int[]{v}));
        while (!paths.isEmpty()) {
            Path path = paths.pop();
            if (path.equals(newCycles)) continue;
            int last = path.last();
            List<Integer> adj = getAdjacent(last);
            Collections.sort(adj, Comparator.comparingInt(o -> manhattanDist(last, o)));
            for (int w : adj) {
                if (path.length() > 1) {
                    if (w == path.start() && path.length() > 3 && !path.equals(newCycles)) {
                        ArrayList<Integer> capturedDots = findCapturedDots(path);
                        if (capturedDots.size() > currentMaxOfCapturedDots) {
                            path.setCapturedDots(capturedDots);
                            newCycles = path;
                            currentMaxOfCapturedDots = capturedDots.size();
                        }
                        continue;
                    }
                    if (path.contains(w)) continue;
                }
                Path newPath = new Path(path, w);
                paths.add(newPath);
            }
        }
        if (currentMaxOfCapturedDots == 0) return new CaptureResult();
        newCycles.setColor(dots[newCycles.start() % DotGameConstant.dimension][newCycles.start() / DotGameConstant.dimension].getColor());
        cycles.add(newCycles);
        for (int dot : newCycles.getCapturedDots())
            disableDot(dot);
        return new CaptureResult(newCycles.getColor(), currentMaxOfCapturedDots);
    }

    Iterable<Path> getCycles() {
        return cycles;
    }

    private static int manhattanDist(int a, int b) {
        int col = a % DotGameConstant.dimension;
        int row = a / DotGameConstant.dimension;
        int col2 = b % DotGameConstant.dimension;
        int row2 = b / DotGameConstant.dimension;
        return Math.abs(col - col2) + Math.abs(row - row2);
    }

}
