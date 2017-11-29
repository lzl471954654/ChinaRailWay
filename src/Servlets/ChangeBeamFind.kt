package Servlets

import DataBaseClasses.JdbcUtils
import Utils.SendUtils
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "changeBeamFind",urlPatterns = arrayOf("/changeBeamFind"))
class ChangeBeamFind:HttpServlet() {

    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var bName = req?.getParameter("bName")
        var bID = req?.getParameter("bID")
        resp?.contentType = "text/json;charset=UTF-8"
        if (!testParamNullOrEmpty(bName,bID)){
            SendUtils.sendParamError("null params",resp)
            return
        }
        bName = URLDecoder.decode(bName,"UTF-8")
        bID = URLDecoder.decode(bID,"UTF-8")

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val sql = " update beam set find = '${dateFormatter.format(Date(System.currentTimeMillis()))}' where bName = '$bName' and bID = '$bID' "
        val jdbc = JdbcUtils()
        val result = jdbc.update(sql)
        SendUtils.sendMsg(result.toInt(),"",resp)
    }
}