import java.util.*;
import java.util.List;

public class DotGraph {
    private int total;
    ArrayList<Integer>[] adj;
    private HashSet<Path> cycles = new HashSet<>();
    private int V = 0; //current vertex count
    private int E = 0;
    private Dot[][] dots;

    DotGraph(int V, Dot[][] dots) {
        total = V;
        adj = new ArrayList[total];
        for (int i = 0; i < total; i++) {
            adj[i] = new ArrayList<>();
        }
        this.dots = dots;
    }

    DotGraph(int V) {
        total = V;
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
                if (current != null && current.getColor() != p.getColor() && current.isAvailable()) {
                    innerDots.add(current.id());
                }
            }
        }
        return innerDots;
    }

    void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    private ArrayList<Integer> getAdjacent(int v) {
        return adj[v];
    }

    CaptureResult findNewCycle(int v) {
        if (V < 4) new CaptureResult();
        HashSet<Path> newCycles = new HashSet<>();
        ArrayDeque<Path> paths = new ArrayDeque<>();
        paths.add(new Path(new int[]{v}));
        while (!paths.isEmpty()) {
            Path path = paths.pop();
            if (newCycles.contains(path)) continue;
            int last = path.last();
            List<Integer> adj = getAdjacent(last);
            Collections.sort(adj, Comparator.comparingInt(o -> manhattanDist(last, o)));
            for (int w : adj) {
                if (path.size() > 1) {
                    if (w == path.start() && path.size() > 3 && !newCycles.contains(path)) {
                        newCycles.add(path);
                        continue;
                    }
                    if (path.contains(w)) continue;
                }
                Path newPath = new Path(path, w);
                paths.add(newPath);
            }
        }
        if (newCycles.isEmpty()) return new CaptureResult();
        Path[] cyclesForSort = new Path[newCycles.size()];
        int i = 0;
        for (Path p : newCycles)
            cyclesForSort[i++] = p;
        Arrays.sort(cyclesForSort, (o1, o2) -> {
            return -Integer.compare(findCapturedDots(o1).size(), findCapturedDots(o2).size()); //reverse order by inner dots of other color
        });
        Path p = cyclesForSort[0];
        p.setColor(dots[p.start() % DotGameConstant.dimension][p.start() / DotGameConstant.dimension].getColor());
        List<Integer> capturedDots = findCapturedDots(p);
        if (capturedDots.size() > 0) {
            cycles.add(p);
            for (int dot : capturedDots)
                disableDot(dot);
        }

        return new CaptureResult(p.getColor(), capturedDots.size());
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
