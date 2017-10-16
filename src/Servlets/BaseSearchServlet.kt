package Servlets

import DataClass.ResponseSingleData
import DataClass.TokenMap
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.sf.json.JSONObject
import java.net.URLDecoder
import java.net.URLEncoder
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class BaseSearchServlet:HttpServlet() {
    var loginFlag:Boolean = false
    var tokenValue:String? = ""
    var uid:String? = ""
    var searchType:String? = ""
    var searchParam:String? = ""
    var searchAll:String? = "0"
    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req?.cookies?.forEach {
            if(it.name == "token")
                tokenValue = it.value
        }
            uid = req?.getParameter("uid")
            loginFlag = TokenMap.verifyUser(uid!!,tokenValue!!)
            resp?.contentType = "text/json;charset=UTF-8"
            if (!loginFlag){
                val data = ResponseSingleData<String>(-2,"Permission denied")
                resp?.writer?.println(JSONObject.fromObject(data).toString())
            }
            searchType = req?.getParameter("searchType")
            searchParam =req?.getParameter("searchParam")
            searchAll = req?.getParameter("searchAll")
            if(searchParam!=null)
                searchParam = URLDecoder.decode(searchParam,"UTF-8")
    }

    companion object {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()
        val factoryForm = "factory"
        val buildPlanForm = "buildplan"
        val filesForm = "files"
        val monthdataForm = "monthdata"
        val modelForm = "model"
        val checkRecForm = "checkrec"
        val beamForm = "beam"
        val bridgeForm = "bridge"
        val makepositionForm = "makeposition"
        val storeForm = "store"
        val storepositionFrom = "storeposition"
        val taskForm = "task"
    }

}