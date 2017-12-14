import java.awt.*;

class Captured {
    private Color color=null;
    private int count=0;

    Captured(Color color, int count){
        this.color=color;
        this.count=count;
    }

    Captured(){}

    int size(){
        return count;
    }

    Color getColor(){
        return color;
    }
}
