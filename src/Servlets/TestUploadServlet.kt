package Servlets

import java.io.File
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//@WebServlet(name = "testServlet",urlPatterns = arrayOf("/testFile"))
class TestUploadServlet:HttpServlet() {

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var file = File("d://accept/factoryImage")
        if(!file.exists())
            file.mkdir()
        val name = req?.getParameter("name")
        val part = req?.getPart("file")
        file = File(FileUploadServlet.srcRoot)
        println(file.absolutePath)
        part?.write("${file.absolutePath+File.separator+"factoryImage"+File.separator}$name.jpg")
    }
}