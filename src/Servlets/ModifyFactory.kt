package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.FactoryData
import Utils.SendUtils
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileOutputStream
import java.net.URLDecoder
import javax.servlet.annotation.MultipartConfig
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "factoryModify",urlPatterns = arrayOf("/factoryModify"))
class ModifyFactory:HttpServlet() {

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var data = req?.getParameter("data")

        resp?.contentType = "text/json;charset=UTF-8"
        if(!testParamNullOrEmpty(data)){
            SendUtils.sendMsg(-1,"data",resp)
            return
        }
        data = URLDecoder.decode(data,"UTF-8")
        println("data: $data")
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
            var dir = FileUploadServlet.srcDirFile.absolutePath + File.separator+"factoryImage"
            var file = File(dir)
            if(!file.exists()){
                file.mkdir()
            }
            dir = dir+File.separator+"${dataObject.name}.jpg"
            val image = File(dir)
            val input = req!!.inputStream
            val out = FileOutputStream(image)
            val bytes = ByteArray(4096)
            while (true){
                val count = input.read(bytes)
                if(count==-1)
                    break
                out.write(bytes,0,count)
            }
            out.close()
            SendUtils.sendMsg(result.toInt(),"成功",resp)
        }else{
            SendUtils.sendMsg(-1,"失败",resp)
        }
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        SendUtils.sendMsg(-1,"Do not support Get Request!",resp)

        val stringArray = Array<Int>(10,init = {
            i ->
            1
        })

        val stringArrays = arrayOf<String>()
    }
}