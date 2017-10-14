package Servlets

import DataBaseClasses.JdbcUtils
import Utils.SendUtils
import java.io.File
import java.io.FileOutputStream
import java.net.URLDecoder
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "FileUploadServlet",urlPatterns = arrayOf("/fileUpload"))
class FileUploadServlet:HttpServlet() {
    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val type = req?.getHeader("FileType")
        var fileName = req?.getHeader("FileName")
        val fileSuffix = req?.getHeader("FileSuffix")
        var bID = req?.getHeader("bID")
        var bName = req?.getHeader("bName")
        var filePath = ""

        fileName = URLDecoder.decode(fileName,"UTF-8")
        bName = URLDecoder.decode(bName,"UTF-8")
        bID = URLDecoder.decode(bID,"UTF-8")

        resp?.contentType = "text/json;charset=UTF-8"


        val fileId:Int = (System.currentTimeMillis()+fileName!!.hashCode().toLong()).hashCode()
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
        if(!srcDirFile.exists())
        {
            srcDirFile.mkdir()
            imageDirFile.mkdir()
            videoDirFile.mkdir()
        }
        else{
            if(!imageDirFile.exists())
                imageDirFile.mkdir()
            if(!videoDirFile.exists())
                videoDirFile.mkdir()
        }
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


        val sql = "insert into files values( '$fileId' , '$bID' , '$bName' , '${file.name}' , '$type' , '${file.absolutePath}' )"

        println("FileSql :$sql")
        val jdbcUtils = JdbcUtils()
        val result:Long = jdbcUtils.update(sql)
        if(result>0){
            SendUtils.sendFileUploadMsg(size,resp)
        }else{
            SendUtils.sendMsg(-1,"上传失败!",resp)
            if (file.exists())
                file.delete()
        }
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        SendUtils.sendMsg(-1,"Do not support Get Request!",resp)
    }

    companion object {
        val srcRoot = "/accept"
        val imageDirPath = "$srcRoot/images"
        val videoDirPath = "$srcRoot/video"

        val srcDirFile = File(srcRoot)
        val imageDirFile = File(imageDirPath)
        val videoDirFile = File(videoDirPath)
    }
}