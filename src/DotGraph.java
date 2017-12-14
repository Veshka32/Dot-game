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
            adj[i] = new ArrayList<>();
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

    public void findAllCycles(int v) {
        if (V<4) return;
        HashSet<Path> newCycles = new HashSet<>();
        ArrayDeque<Path> paths = new ArrayDeque<>();
        paths.add(new Path(new int[]{v}));
        while (!paths.isEmpty()) {
            Path path = paths.pop();
            if (newCycles.contains(path)) continue;
            int last=path.last();
            List<Integer> adj=getAdjacent(last);
            Collections.sort(adj, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    if (manhattanDist(last, o1) < manhattanDist(last, o2)) return -1;
                    else if (manhattanDist(last, o1) > manhattanDist(last, o2)) return 1;
                    else return 0;
                }
            });
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
        if (newCycles.isEmpty()) return;
        Path[] cyclesForSort= new Path[newCycles.size()];
        int i=0;
        for (Path p:newCycles)
            cyclesForSort[i++]=p;
        Arrays.sort(cyclesForSort);
        cycles.add(cyclesForSort[cyclesForSort.length-1]);
    }


    public Iterable<Path> getCycles() {
        return cycles;
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


    public static int manhattanDist(int a, int b) {
        int col = a % DotGameConstant.dimension;
        int row = a / DotGameConstant.dimension;
        int col2 = b % DotGameConstant.dimension;
        int row2 = b / DotGameConstant.dimension;
        return Math.abs(col - col2) + Math.abs(row - row2);
    }

}
