package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.FactoryData
import Utils.SendUtils
import com.google.gson.GsonBuilder
import java.io.File
import java.net.URLDecoder
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "factoryModify",urlPatterns = arrayOf("/factoryModify"))
class ModifyFactory:HttpServlet() {

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var data = req?.getParameter("data")
        req?.getPart("file")
        if(!testParamNullOrEmpty(data)){
            SendUtils.sendMsg(-1,"data",resp)
            return
        }
        data = URLDecoder.decode(data,"UTF-8")
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()
        val dataObject = gson.fromJson(data,FactoryData::class.java)
        val fileName = dataObject.name +".jpg"
        val clazz = FactoryData::class.java
        val builder = StringBuilder()
        builder.append(" update factory set ")
        clazz.declaredFields.forEach {
            it.isAccessible = true
            if(it.name!="name"){
                val name = it.name
                val data = it.get(dataObject).toString()
                builder.append(" $name = '$data', ")
            }
        }
        builder.delete(builder.length-2,builder.length)
        builder.append(" where name = '${dataObject.name}' ")
        val sql = builder.toString()
        println("modifySQL is :$sql")
        val jdbc = JdbcUtils()
        val result:Long = jdbc.update(sql)
        if(result>0){
            var dir = FileUploadServlet.srcRoot+ File.separator+"factoryImage"
            var file = File(dir)
            if(!file.exists()){
                file.mkdir()
            }
            dir = dir+File.separator+"${dataObject.name}.jpg"
            //file = File(dir)
            val part = req?.getPart("file")
            part?.write(dir)
            SendUtils.sendMsg(result.toInt(),"成功",resp)
        }else{
            SendUtils.sendMsg(-1,"失败",resp)
        }
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        SendUtils.sendMsg(-1,"Do not support Get Request!",resp)
    }
}