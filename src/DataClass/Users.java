package DataClass;

import java.io.Serializable;

public class Users implements Serializable {
    String uid;
    String Uname;
    String roleName;
    String priv;
    int roleId;
    int state;

    public Users(){}

    public Users(String uid, String uname, String roleName, String priv, int roleId, int state) {
        this.uid = uid;
        Uname = uname;
        this.roleName = roleName;
        this.priv = priv;
        this.roleId = roleId;
        this.state = state;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPriv() {
        return priv;
    }

    public void setPriv(String priv) {
        this.priv = priv;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
