package Utils

import DataClass.ResponseSingleData
import com.google.gson.Gson
import net.sf.json.JSONObject
import javax.servlet.http.HttpServletResponse

object SendUtils {

    fun sendParamError(wrongParamType: String,resp: HttpServletResponse?){
        sendMsg(-1,"参数：$wrongParamType 错误",resp)
    }

    fun sendMsg(code: Int,msg: String,resp: HttpServletResponse?){
        val data = ResponseSingleData<String>(code.toLong(),msg)
        resp?.writer?.println(JSONObject.fromObject(data).toString())
    }

    fun sendFileUploadMsg(code : Long,resp: HttpServletResponse?){
        val data = ResponseSingleData<String>(code,"")
        resp?.writer?.println(JSONObject.fromObject(data).toString())
    }
}