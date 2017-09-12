package Servlets;

import DataBaseClasses.JdbcUtils;
import DataClass.ResponseSingleData;
import DataClass.Users;
import Utils.LogUtils;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        String pwd = req.getParameter("pwd");
        try{
            ResultSet set = null;
            JdbcUtils jdbcUtils = new JdbcUtils();
            String sql = "select * from users where uid = '"+uid+"' and pwd = '"+pwd+"'";
            set = jdbcUtils.Query(sql);
            JSONObject object;
            String json;
            if(set==null&&set.wasNull()&&!set.next()){
                ResponseSingleData<String> data = new ResponseSingleData<>(0,"null");
                object = JSONObject.fromObject(data);
                json = object.toString();
            }
            else {
                set.first();
                String uName = set.getString("UName");
                int roleId = set.getInt("roleID");
                int state = set.getInt("State");
                Users users = new Users(uid,uName,roleId,state);
                ResponseSingleData<Users> data = new ResponseSingleData<>(1,users);
                object = JSONObject.fromObject(data);
                json = object.toString();
            }
            resp.setContentType("text/json;charset=UTF-8");
            //resp.setCharacterEncoding("UTF-8");
            resp.getWriter().println(json);
        }catch (SQLException e){
            e.printStackTrace();
            LogUtils.logException(getClass().getName(),e.getMessage()+"");
            ResponseSingleData<String> data = new ResponseSingleData<>(0,"Exception"+e.getMessage());
            resp.getWriter().println(JSONObject.fromObject(data).toString());
        }
    }
}
