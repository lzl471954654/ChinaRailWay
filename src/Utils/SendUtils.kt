package Utils

import DataClass.ResponseSingleData
import net.sf.json.JSONObject
import javax.servlet.http.HttpServletResponse

object SendUtils {

    fun sendParamError(wrongParamType: String,resp: HttpServletResponse?){
        sendMsg(-1,"参数：$wrongParamType 错误",resp)
    }

    fun sendMsg(code: Int,msg: String,resp: HttpServletResponse?){
        val data = ResponseSingleData<String>(code,msg)
        resp?.writer?.println(JSONObject.fromObject(data).toString())
    }
}