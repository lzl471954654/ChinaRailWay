package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.*
import Utils.ObjectToDBFrom
import Utils.SendUtils
import java.net.URLDecoder
import java.sql.SQLException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "AddServlet",urlPatterns = arrayOf("/addInfo"))
class AddInfoServlet:HttpServlet() {
    val jdbc = JdbcUtils()
    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val type = req?.getParameter("type")
        var data = req?.getParameter("data")

        resp?.contentType = "text/json;charset=UTF-8"
        if (type==null||data==null){
            SendUtils.sendParamError("${if (type==null) "type" else "data"} is null",resp)
            return
        }
        data = URLDecoder.decode(data,"UTF-8")
        when(type){
            BaseSearchServlet.storepositionFrom->{
                dealData(ObjectToDBFrom.gson.fromJson(data,StorePositionData::class.java),type,resp)
            }
            BaseSearchServlet.storeForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,StoreData::class.java),type,resp)
            }
            BaseSearchServlet.monthdataForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,MonthData::class.java),type,resp)
            }
            BaseSearchServlet.modelForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,ModelData::class.java),type,resp)
            }
            BaseSearchServlet.makepositionForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,MakePosition::class.java),type,resp)
            }
            BaseSearchServlet.filesForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,FilesData::class.java),type,resp)
            }
            BaseSearchServlet.factoryForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,FactoryData::class.java),type,resp)
            }
            BaseSearchServlet.checkRecForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,CheckRecData::class.java),type,resp)
            }
            BaseSearchServlet.buildPlanForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,BuildPlan::class.java),type,resp)
            }
            BaseSearchServlet.bridgeForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,BridgeData::class.java),type,resp)
            }
            BaseSearchServlet.beamForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,BeamData::class.java),type,resp)
            }
            BaseSearchServlet.taskForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,TaskData::class.java),type,resp)
            }
        }
        jdbc.releaseResource()
    }


    fun dealData(srcObject : Any,type:String,resp: HttpServletResponse?){
        val sql = "insert into $type values ( ${ObjectToDBFrom.convertToSQLInsertValues(srcObject)} )"
        println("sql : $sql")
        try {
            val result = jdbc.update(sql)
            if(result==1L){
                SendUtils.sendMsg(1,"Success",resp)
            }
            else{
                SendUtils.sendMsg(-1,"Failed",resp)
            }
        }catch (e:SQLException){
            e.printStackTrace()
            SendUtils.sendMsg(-1,"data type is Wrong",resp)
        }
    }
}