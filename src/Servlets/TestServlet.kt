package Servlets

import DataBaseClasses.DBConnection
import Utils.SendUtils
import java.sql.ResultSet
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "test",urlPatterns = ["/test"])
class TestServlet : HttpServlet(){

    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val connection = DBConnection.getNewConnection()
        if (connection == null){
            SendUtils.sendMsg(-1,"database is busy",resp)
            return
        }
        val state = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE)
        val set = state.executeQuery("select * from task where TaskDate = '2017-12-05'")
        set.last()
        val count = set.row
        resp?.writer?.println(count)
        state.close()
        connection.close()
    }
}