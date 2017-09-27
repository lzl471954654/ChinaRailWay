package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.FilesData
import Utils.DBFromToObject
import Utils.SendUtils
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "fileSearch",urlPatterns = arrayOf("/fileSearch"))
class FileSearch:HttpServlet() {

    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val bId = req?.getParameter("bID")
        val bName = req?.getParameter("bName")
        resp?.contentType = "text/json;charset=UTF-8"
        if(!testParamNullOrEmpty(bId,bName)){
            SendUtils.sendParamError("bid or bName",resp)
            return
        }

        val sql = "select * from files where bId = '$bId' and bName = '$bName'"
        val set = JdbcUtils().Query(sql)
        if (!set.next()){
            SendUtils.sendMsg(0,"No data",resp)
            return
        }
        set.beforeFirst()
        val array = DBFromToObject.converToObjectArray(set,FilesData::class.java)
        SendUtils.sendMsg(array.size,BaseSearchServlet.gson.toJson(array),resp)
    }
}