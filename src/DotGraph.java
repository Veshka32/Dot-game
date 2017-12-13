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
        ArrayDeque<int[]> paths = new ArrayDeque<>();
        for (int i = 0; i < adj.length; i++) {
            paths.add(new int[]{i});
            while (!paths.isEmpty()) {
                int[] path = paths.pop();
                for (int w : getAdjacent(path[path.length - 1])) {
                    if (path.length >1) {
                        if (w == path[0] && path.length > 3) {
                            addCycle(path); continue;
                        }
                        if (contains(path,w)) continue;
                    }
                    int[] newPath = new int[path.length + 1];
                    System.arraycopy(path, 0, newPath, 0, path.length);
                    newPath[path.length] = w;
                    paths.add(newPath);
                }
            }
        }
    }

    public boolean contains(int[] array,int a){
        for (Integer i:array){
            if (i==a) return true;
        }
        return false;
    }

    public void addCycle(int[] path) {
        Path p=new Path(path);
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
