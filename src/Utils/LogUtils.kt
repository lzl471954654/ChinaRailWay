package Utils

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class LogUtils{

    companion object {
        @JvmStatic
        fun logException(classTag:String,msg:String){
            try{
                val date = Date()
                val log = "$date  Exception: $classTag : $msg"
                write.println(log)
                write.flush()
            }catch (e: IOException){
                e.printStackTrace()
                initLog()
            }
        }
        @JvmStatic
        fun logInfo(classTag: String,msg: String){
            try{
                val date = Date()
                val log = "$date  Info: $classTag : $msg"
                write.println(log)
                write.flush()
            }catch (e: IOException){
                e.printStackTrace()
                initLog()
            }
        }
        @JvmStatic
        fun initLog(){
            try {
                if (logFile.exists())
                {
                    if (logFile.delete())
                        logFile.createNewFile()
                }
                else
                    println(logFile.createNewFile())
                write = PrintWriter(logFile)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
        @JvmStatic
        fun releaseResource(){
            try{
                write.close()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }

        @JvmField
        var logFile: File = File("Log.txt")
        lateinit var write: PrintWriter
    }
}


fun JvmType.Object.logInfo(msg:String){
    LogUtils.logInfo(this.javaClass.name,msg)
}

fun JvmType.Object.logException(msg: String){
    LogUtils.logException(this.javaClass.name,msg)
}
