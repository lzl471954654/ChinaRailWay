package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.ModifyData
import Utils.SendUtils
import java.net.URLDecoder
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "ModifyServlet",urlPatterns = arrayOf("/modify"))
class ModifyServlet : HttpServlet() {
    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val type = req?.getParameter("type")
        var primaryKeyParam = req?.getParameter("pk")
        var modifyData = req?.getParameter("modifyData")
        resp?.contentType = "text/json;charset=UTF-8"
        if(!testParamNullOrEmpty(type,primaryKeyParam,modifyData)){
            SendUtils.sendParamError("Some param is NULL",resp)
            return
        }

        primaryKeyParam = URLDecoder.decode(primaryKeyParam,"UTF-8")
        modifyData = URLDecoder.decode(modifyData,"UTF-8")

        val primaryKeyArray = BaseSearchServlet.gson.fromJson(primaryKeyParam,Array<ModifyData>::class.java)
        val modifyDataArray = BaseSearchServlet.gson.fromJson(modifyData,Array<ModifyData>::class.java)
        if (type!!.toLowerCase() == "task"){
            modifyTask(primaryKeyArray,modifyDataArray,req,resp)
        }else{
            val builder = StringBuilder()
            builder.append("update $type set ")
            modifyDataArray.forEach {
                builder.append("${it.name} = '${it.data}', ")
            }
            builder.delete(builder.length-2,builder.length)
            builder.append(" where ")
            primaryKeyArray.forEach {
                builder.append(" ${it.name} = '${it.data}' and")
            }
            builder.delete(builder.length-3,builder.length)
            val sql = builder.toString()
            println("modifySQL is :$sql")
            val jdbc = JdbcUtils()
            val result:Long = jdbc.update(sql)
            if(result>0){
                SendUtils.sendMsg(result.toInt(),"修改成功",resp)
            }else{
                SendUtils.sendMsg(-1,"修改失败",resp)
            }
        }
    }

    fun modifyTask(primaryKeyArray : Array<ModifyData>,modifyDataArray : Array<ModifyData>,req: HttpServletRequest?, resp: HttpServletResponse?){
        val builder = StringBuilder()
        builder.append("update task set ")
        modifyDataArray.forEach {
            builder.append("${it.name} = '${it.data}', ")
        }
        builder.delete(builder.length-2,builder.length)
        builder.append(" where ")
        primaryKeyArray.forEach {
            builder.append(" ${it.name} = '${it.data}' and")
        }
        builder.delete(builder.length-3,builder.length)
        var sql = builder.toString()
        println("modifySQL is :$sql")
        val jdbc = JdbcUtils()
        var result:Long = jdbc.update(sql)
        if (result > 0){
            var makeposid = ""
            var pedid = ""
            var pos = ""
            var permit = "0"
            modifyDataArray.forEach {
                when(it.name.toLowerCase()){
                    "makeposid"-> makeposid = it.data
                    "pedid"-> pedid = it.data
                    "pos"-> pos = it.data
                    "permit"-> permit = it.data
                }
            }
            if (!testParamNullOrEmpty(makeposid)){
                SendUtils.sendMsg(-2,"makeposid is empty or null",resp)
                return
            }
            sql = "update makePosition set idle = ${if (permit == "1") '0' else '1'} where makePosID = '$makeposid' "
            result = jdbc.update(sql)
            if (result == 1L){
                SendUtils.sendMsg(1,"修改成功",resp)
            }else{
                SendUtils.sendMsg(-2,"task update suceess , makeposition failed!",resp)
            }
        }else{
            SendUtils.sendMsg(-1,"修改失败",resp)
        }
    }
}


fun HttpServlet.testParamNullOrEmpty(vararg param:String?):Boolean{
    var flag = true
    param.forEach {
        if (it == null || it == "") {
            flag = false
            return@forEach
        }
    }
    return flag
}