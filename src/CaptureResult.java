import java.awt.*;

class CaptureResult {
    private Color color=null;
    private int count=0;

    CaptureResult(Color color, int count){
        this.color=color;
        this.count=count;
    }

    CaptureResult(){}

    int size(){
        return count;
    }

    Color getColor(){
        return color;
    }
}
