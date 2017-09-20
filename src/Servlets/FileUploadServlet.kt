package Servlets

import Utils.SendUtils
import java.io.File
import java.io.FileOutputStream
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "FileUploadServlet",urlPatterns = arrayOf("/fileUpload"))
class FileUploadServlet:HttpServlet() {
    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val type = req?.getHeader("FileType")
        val fileName = req?.getHeader("FileName")
        val fileSuffix = req?.getHeader("FileSuffix")
        var filePath = ""
        if(type == "image")
            filePath = imageDirPath
        else if (type == "video")
            filePath = videoDirPath
        else{
            SendUtils.sendParamError("FileType",resp)
            return
        }
        filePath = filePath + File.separator + fileName + "." +fileSuffix
        val file = File(filePath)
        println("filePath :\t"+file.absolutePath)
        if (!file.exists()){
            file.createNewFile()
        }
        else{
            if(file.delete())
                file.createNewFile()
        }
        val fileInput = req.inputStream
        val fileOutput = FileOutputStream(file)
        val byte = ByteArray(4096)
        var count = 0
        var size = 0L
        while (true){
            count = fileInput.read(byte)
            if(count == -1){
                break
            }
            size += count
            fileOutput.write(byte,0,count)
        }
        fileOutput.close()
        SendUtils.sendFileUploadMsg(size,resp)
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        SendUtils.sendMsg(-1,"Do not support Get Request!",resp)
    }

    companion object {
        val imageDirPath = "/accept/images"
        val videoDirPath = "/accept/video"
    }
}