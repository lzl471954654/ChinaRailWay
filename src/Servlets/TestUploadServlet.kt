package Servlets

import java.io.File
import java.net.URLDecoder
import java.net.URLEncoder
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//@WebServlet(name = "testServlet",urlPatterns = arrayOf("/testFile"))
class TestUploadServlet:HttpServlet() {

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var file = File("d://accept/factoryImage")
        if(!file.exists())
            file.mkdir()
 //       val part = req?.getPart("file")

       // val bytes = ByteArray(4096)

        file = File(FileUploadServlet.srcRoot)
        println(file.absolutePath)
        val data = req?.getHeader("data")
        println(URLDecoder.decode(data,"UTF-8"))
        //part?.write("${file.absolutePath+File.separator+"factoryImage"+File.separator}$name.jpg")
    }
}