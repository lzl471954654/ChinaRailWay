package Servlets

import Utils.SendUtils
import java.io.File
import java.net.URLDecoder
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "download",urlPatterns = arrayOf("/download"))
class download:HttpServlet() {

    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val type = req?.getParameter("type")
        var fileName = req?.getParameter("fileName")

        if(!testParamNullOrEmpty(type,fileName)){
            SendUtils.sendParamError(" type or fileName",resp)
            return
        }

        fileName = URLDecoder.decode(fileName,"UTF-8")
        var path:String? = null
        if(type == "image")
        {
            path = FileUploadServlet.imageDirPath+File.separator+fileName
            resp?.contentType = "image"
        }
        else if(type == "video")
        {
            path = FileUploadServlet.videoDirPath+File.separator+fileName
            resp?.contentType = "video/mpeg4"
        }else{
            path = FileUploadServlet.srcRoot+File.separator+"factoryImage"+File.separator+fileName
            resp?.contentType = "image"
        }

        val file = File(path)
        val input = file.inputStream()
        val dataArray = ByteArray(1024)
        while(true){
            val count = input.read(dataArray)
            if(count==-1)
                break
            resp?.outputStream?.write(dataArray,0,count)
        }
    }
}