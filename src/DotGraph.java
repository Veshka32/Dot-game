import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;

public class DotGraph {
    int total=DotGameConstant.dimension*DotGameConstant.dimension;
    ArrayList<Integer>[] adj=new ArrayList[total];
    int V=0; //current vertex count
    int E=0;

    public DotGraph(){
        for (int i=0;i<total;i++){
            adj[i]=new ArrayList<Integer>();
        }
    }

    public int vertexCount(){
        return V;
    }

    public int edgeCount(){
        return E;
    }

    public void addDot(int d){
        V++;
    }

    public void addEdge(int v,int w){
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    public ArrayList<Integer> getConnected(int v){
        return adj[v];
    }
}
