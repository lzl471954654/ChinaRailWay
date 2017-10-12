package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.FactoryData
import Utils.SendUtils
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
        if(testParamNullOrEmpty(data)){
            SendUtils.sendMsg(-1,"data",resp)
            return
        }
        data = URLDecoder.decode(data,"UTF-8")
        val dataObject = BaseSearchServlet.gson.fromJson(data,FactoryData::class.java)
        val fileName = dataObject.name +".jpg"
        val clazz = fileName.javaClass
        val builder = StringBuilder()
        builder.append(" update factory set ")
        clazz.declaredFields.forEach {
            if(it.name!="name"){
                it.isAccessible = true
                builder.append(" ${it.name} = '${it.get(dataObject).toString()}', ")
            }
        }
        builder.delete(builder.length-2,builder.length)
        builder.append(" where name = ${dataObject.name}")
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
            file = File(dir)
            if(file.exists()){
                file.delete()
                file.createNewFile()
            }
            var out = file.outputStream()
            var input = req!!.inputStream
            val bytes = ByteArray(4096)
            var count = 0
            while(true){
                count = input.read(bytes)
                if(count==-1){
                    break
                }
                out.write(bytes,0,count)
            }
            SendUtils.sendMsg(result.toInt(),"成功",resp)
        }else{
            SendUtils.sendMsg(-1,"失败",resp)
        }
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        SendUtils.sendMsg(-1,"Do not support Get Request!",resp)
    }
}