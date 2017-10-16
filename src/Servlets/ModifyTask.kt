package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.TaskData
import Utils.SendUtils
import java.net.URLDecoder
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "modifyTask",urlPatterns = arrayOf("/modifyTask"))
class ModifyTask:HttpServlet() {

    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var data = req!!.getParameter("data")
        data = URLDecoder.decode(data,"UTF-8")
        val tasks = BaseSearchServlet.gson.fromJson(data,Array<TaskData>::class.java)
        var count = 0
        val jdbc = JdbcUtils()
        tasks.forEach {
//            val sql = " update task set bName = '${it.getbName()}' , bID = '${it.getbID()}' , makeOrder = '${it.makeOrder}' , makePosID = '${it.makePosId}', pedID = '${it.pedID}', pos = '${it.pos}',permit = '${if(it.isPermit) 1 else 0}' , taskdate = '${it.taskDate}' where taskdate = '${it.taskDate}'"
            val sql = " update task set  makeOrder = '${it.makeOrder}' , makePosID = '${it.makePosId}', pedID = '${it.pedID}', pos = '${it.pos}',permit = '${if(it.isPermit) 1 else 0}' where taskdate = '${it.taskDate}'and bName = '${it.getbName()}'and bID = '${it.getbID()}'"
            println(sql)
            val res = jdbc.update(sql)
            if(res==1L)
                count++
        }
        SendUtils.sendMsg(count,"",resp)
        jdbc.releaseResource()
    }
}