package Servlets

import Utils.SendUtils
import net.sf.json.JSONObject
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "CheckVersion",urlPatterns = arrayOf("/checkVersion"))
class CheckVersion:HttpServlet() {
    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val proFile = Properties()
        proFile.load(servletContext.getResourceAsStream("/WEB-INF/apk.properties"))
        val version = proFile.getProperty("apkVersion")
        val url = proFile.getProperty("url")
        val json = JSONObject()
        json.put("version",version)
        json.put("url",url)
        resp?.writer?.println(json.toString())
    }
}