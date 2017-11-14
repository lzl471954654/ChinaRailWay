package Servlets

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "downloadAPK",urlPatterns = arrayOf("/getAPK"))
class downloadAPK : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val input = servletContext.getResourceAsStream("/WEB-INF/newVersion.apk")
        val out = resp?.outputStream
        val bytes = ByteArray(4096)
        while (true){
            val count = input.read(bytes)
            if(count==-1)
                break
            out?.write(bytes)
        }
        input.close()
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        doGet(req!!,resp!!)
    }
}