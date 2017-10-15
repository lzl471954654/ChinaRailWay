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
        when(type.toLowerCase()){
            BaseSearchServlet.storepositionFrom->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<StorePositionData>::class.java),type,resp)
            }
            BaseSearchServlet.storeForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<StoreData>::class.java),type,resp)
            }
            BaseSearchServlet.monthdataForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<MonthData>::class.java),type,resp)
            }
            BaseSearchServlet.modelForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<ModelData>::class.java),type,resp)
            }
            BaseSearchServlet.makepositionForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<MakePosition>::class.java),type,resp)
            }
            BaseSearchServlet.filesForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<FilesData>::class.java),type,resp)
            }
            BaseSearchServlet.factoryForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<FactoryData>::class.java),type,resp)
            }
            BaseSearchServlet.checkRecForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<CheckRecData>::class.java),type,resp)
            }
            BaseSearchServlet.buildPlanForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<BuildPlan>::class.java),type,resp)
            }
            BaseSearchServlet.bridgeForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<BridgeData>::class.java),type,resp)
            }
            BaseSearchServlet.beamForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<BeamData>::class.java),type,resp)
            }
            BaseSearchServlet.taskForm->{
                dealData(ObjectToDBFrom.gson.fromJson(data,Array<TaskData>::class.java),type,resp)
            }
        }
        jdbc.releaseResource()
    }


    fun <T>dealData(srcObject : Array<T>,type:String,resp: HttpServletResponse?){
        //val sql = "insert into $type values ( ${ObjectToDBFrom.convertToSQLInsertValues(srcObject)} )"
        val sqlBuilder = StringBuilder(" insert into $type values ")
        srcObject.forEach {
            sqlBuilder.append("( ${ObjectToDBFrom.convertToSQLInsertValues(it as Any)} ) ,")
        }
        sqlBuilder.delete(sqlBuilder.length-1,sqlBuilder.length)
        println("sql : $sqlBuilder")
        try {
            val result = jdbc.update(sqlBuilder.toString())
            if(result>=1L){
                SendUtils.sendMsg(result.toInt(),"Success",resp)
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