package Servlets

import java.io.File
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "downloadAPK",urlPatterns = arrayOf("/getAPK"))
class downloadAPK : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.contentType = "application/vnd.android.package-archive"
        //val input = servletContext.getResourceAsStream("/WEB-INF/newVersion.apk")
        //println(servletContext.getMimeType("/WEB-INF/newVersion.apk"))
        //println(servletContext.getRealPath("/WEB-INF/newVersion.apk"))
        val file = File(servletContext.getRealPath("/WEB-INF/newVersion.apk"))
        resp?.addHeader("content-disposition","attachment;filename=${file.name}")
        resp?.setContentLengthLong(file.length())
        val input = file.inputStream()
        val out = resp?.outputStream
        val bytes = ByteArray(4096)
        while (true){
            val count = input.read(bytes)
            if(count==-1)
                break
            out?.write(bytes,0,count)
        }
        //out?.close()
        input.close()
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        doGet(req!!,resp!!)
    }
}