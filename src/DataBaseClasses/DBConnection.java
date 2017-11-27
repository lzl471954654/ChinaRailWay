package DataBaseClasses;

import Utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by LZL on 2017/7/13.
 */
public class DBConnection {
    private  static String DB_Driver = "com.mysql.jdbc.Driver";
    private static Connection connection = null;
    private static String tag = "DBConnection";
    private static String url;
    private static String user;
    private static String password;
    static
    {
        LogUtils.initLog();
        try
        {
            Properties properties = new Properties();
            File file = new File("/WEB-INF/db.properties");
            properties.load(new FileReader(file));
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");


            Class.forName(DB_Driver);
            System.out.println("JDBC驱动加载成功！");
            logInfo("JDBC驱动加载成功！");

        }catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("JDBC驱动加载失败！");
            logInfo("JDBC驱动加载失败！");
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private static void logInfo(String msg){
        LogUtils.logInfo(tag,msg);
    }
    private static void logException(String msg){
        LogUtils.logException(tag,msg);
    }

    private void initConnection()
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

    private void reConnected()
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
            else if(connection.isClosed())
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
