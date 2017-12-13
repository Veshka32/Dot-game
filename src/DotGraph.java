import java.util.*;

public class DotGraph {
    int total;
    ArrayList<Integer>[] adj;
    HashSet<Path> cycles = new HashSet<>();
    int V = 0; //current vertex count
    int E = 0;

    public DotGraph(int V) {
        total = V;
        adj = new ArrayList[total];
        for (int i = 0; i < total; i++) {
            adj[i] = new ArrayList<Integer>();
        }
    }

    public int vertexCount() {
        return V;
    }

    public int edgeCount() {
        return E;
    }

    public void addDot() {
        V++;
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    public ArrayList<Integer> getAdjacent(int v) {
        return adj[v];
    }

    public void findAllCycles() {
        ArrayDeque<Path> paths = new ArrayDeque<>();
        for (int i = 1; i < adj.length; i++) {
            paths.add(new Path(new int[]{i}));
            while (!paths.isEmpty()) {
                Path path = paths.pop();
                for (int w : getAdjacent(path.last())){
                    if (path.size() >1) {
                        if (w == path.start() && path.size() > 3) {
                            addCycle(path); continue;
                        }
                        if (path.contains(w)) continue;
                    }
                    Path newPath = new Path(path,w);
                    paths.add(newPath);
                }
            }
        }
    }



    public void addCycle(Path p) {
        if (!cycles.contains(p)) cycles.add(p);
    }

//    public void normalizeCycle(int[] path) {
//        int indexOfMin = 0;
//        for (int i = 0; i < path.length; i++) {
//            if (path[i] < path[indexOfMin])
//                indexOfMin = i;
//        }
//
//        int[] normalized = new int[path.length];
//        System.arraycopy(path, 0, normalized, indexOfMin, path.length - indexOfMin);
//        System.arraycopy(path, path.length - indexOfMin, normalized, 0, indexOfMin);
//        path = normalized;
//    }


}
