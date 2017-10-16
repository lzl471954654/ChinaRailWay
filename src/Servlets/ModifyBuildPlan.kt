package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.BuildPlan
import DataClass.TaskData
import Utils.SendUtils
import java.net.URLDecoder
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet(name = "modifyBuildPlan",urlPatterns = arrayOf("/modifyBuildPlan"))
class ModifyBuildPlan:HttpServlet() {

    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var data = req!!.getParameter("data")
        data = URLDecoder.decode(data,"UTF-8")
        val tasks = BaseSearchServlet.gson.fromJson(data,Array<BuildPlan>::class.java)
        var count = 0
        val jdbc = JdbcUtils()
        tasks.forEach {
            val sql = " update buildPlan set bID = '${it.getbID()}' , bName = '${it.getbName()}' , dir = '${it.dir}' , seq = '${it.seq}' , bFromDate = '${it.getbFromDate()}' , bToDate = '${it.getbToDate()}' , side = '${it.side}'  where buildID = '${it.buildID}' "
            println(sql)
            val res = jdbc.update(sql)
            if(res==1L)
                count++
        }
        SendUtils.sendMsg(count,"",resp)
        jdbc.releaseResource()
    }
}