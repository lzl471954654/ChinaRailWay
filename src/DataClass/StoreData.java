package DataClass;

public class StoreData {
    String bName;
    String bID;
    short pedID;
    String pos;

    /*
    * inTime原始类型为 datetime
    * outTime原始类型为 datetime
    * */
    String inTime;
    String outTime;

    public StoreData(){}

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbID() {
        return bID;
    }

    public void setbID(String bID) {
        this.bID = bID;
    }

    public short getPedID() {
        return pedID;
    }

    public void setPedID(short pedID) {
        this.pedID = pedID;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
}
