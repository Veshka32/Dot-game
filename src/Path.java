import java.util.Arrays;

public class Path {
    int hash;
    final int[] path;

    public Path(int[] array,int hash){ //use new array and hash of previous array
     path=array;
     this.hash=hash^path[path.length-1]*397;
    }

    public Path(int[] array){
        path=array;
        for (int i:path)
            hash=hash^i*397;
    }

    public String toString(){
        return Arrays.toString(path);
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
