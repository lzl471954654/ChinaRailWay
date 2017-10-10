package DataBaseClasses;

import Utils.LogUtils;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by LZL on 2017/7/13.
 */
public class DBConnection {
    private  static String DB_Driver = "com.mysql.jdbc.Driver";
    //private static String url = "jdbc:mysql://222.24.63.119:3306/crbf";
    private static String url = "jdbc:mysql://127.0.0.1:3306/crbf";
    private static String user = "root";
    private static String password = "lzl471954654";
     /*private static String user = "cradmin";
     private static String password = "HelloMySQL2017;";
     private static String url = "jdbc:mysql://222.24.63.119:3306/crbf";*/
    private  Connection connection = null;
    private static String tag = "DBConnection";
    static
    {
        LogUtils.initLog();
        try
        {
            Class.forName(DB_Driver);
            System.out.println("JDBC驱动加载成功！");
            logInfo("JDBC驱动加载成功！");

        }catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("JDBC驱动加载失败！");
            logInfo("JDBC驱动加载失败！");
        }
    }


    public static void logInfo(String msg){
        LogUtils.logInfo(tag,msg);
    }
    public static void logException(String msg){
        LogUtils.logException(tag,msg);
    }

    public void initConnection()
    {
        try
        {
            connection = DriverManager.getConnection(url,user,password);
            if(connection==null)
                reConnected();
            if(connection!=null)
                System.out.println("数据库连接成功！");
            logInfo("数据库连接成功！");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("initConnection SQL ERROR!");
            logException("initConnection SQL ERROR!");
        }
    }

    public void reConnected()
    {
       try {
           System.out.println("重新连接数据库中");
           connection = DriverManager.getConnection(url,user,password);
       }catch (SQLException e)
       {
           e.printStackTrace();
           System.out.println("重新连接数据库出现异常！！");
       }
    }
    public Connection getConnection()
    {
        try {
            if(connection==null)
                initConnection();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection;
    }
    public void closeConnection()
    {
        try
        {
            if(connection!=null)
                connection.close();
            connection = null;
            System.out.println("关闭数据库连接成功！");
        }catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("关闭数据库连接失败！");
            connection = null;
        }
    }
}
