package DataClass;

public class MakePosition {
    int makePosID;
    short size;
    boolean idle;

    public MakePosition(){}

    public int getMakePosID() {
        return makePosID;
    }

    public void setMakePosID(int makePosID) {
        this.makePosID = makePosID;
    }

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }
}
