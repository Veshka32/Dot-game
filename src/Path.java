import java.util.Arrays;

public class Path {
    int hash;
    final int[] path;

    public Path(Path p,int last){ //path=array+last
     path=new int[p.size()+1];
     System.arraycopy(p.path,0,path,0,p.size());
     path[path.length-1]=last;
     this.hash=p.hashCode()^last*397;
    }

    public Path(int[] array){
        path=array;
        for (int i:path)
            hash=hash^i*397;
    }

    public String toString(){
        return Arrays.toString(path);
    }

    public int size(){
        return path.length;
    }

    public int start(){
        return path[0];
    }

    public int last(){
        return path[path.length-1];
    }

    public boolean contains(int a){
        for (int i:path){
            if (i==a) return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Path that = (Path) obj;
        return hash==that.hashCode();
    }
}
