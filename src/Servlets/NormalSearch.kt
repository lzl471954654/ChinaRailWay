package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.*
import Utils.DBFromToObject
import Utils.SendUtils
import com.google.gson.Gson
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "NormalSearch",urlPatterns = arrayOf("/search"))
class NormalSearch:BaseSearchServlet() {
    var type:String? = ""
    lateinit var resp:HttpServletResponse
    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if(resp!=null)
            this.resp = resp
        else
            return
        super.service(req, resp)
        if(!loginFlag)
            return
        type = req?.getParameter("type")
        if(type==null)
            type=""
        when(type?.toLowerCase()){
            factoryForm->search(FactoryData::class.java)
            beamForm->search(BeamData::class.java)
            bridgeForm->search(BridgeData::class.java)
            buildPlanForm->search(BuildPlan::class.java)
            checkRecForm->search(CheckRecData::class.java)
            filesForm->search(FilesData::class.java)
            makepositionForm->search(MakePosition::class.java)
            modelForm->search(ModelData::class.java)
            monthdataForm->search(MonthData::class.java)
            storeForm->search(StoreData::class.java)
            storepositionFrom->search(StorePositionData::class.java)
            taskForm->search(TaskData::class.java)
            "taskWithBeam"->taskWithBeam()
            else->{
                SendUtils.sendParamError("FormTypeError",resp!!)
                return
            }
        }

    }

    private fun taskWithBeam(){
        val sql = "select beam.* , task.* from beam,task where beam.bid = task.bid and beam.bName = task.bName"
        val jdbc = JdbcUtils()
        val set = jdbc.Query(sql)
        if(!set.next()){
            SendUtils.sendMsg(0,"没有数据",resp)
            return
        }
        set.beforeFirst()
        val listData = DBFromToObject.converToObjectArray(set,TaskWithBeam::class.java)
        SendUtils.sendMsg(listData.size, gson.toJson(listData),resp)
    }

    fun <T>search(clazz:Class<T>){
        if(searchAll == "1"){
            searchAllInfo(clazz)
            return
        }
        val sql = "select * from $type where $searchType = '$searchParam'"
        val jdbc = JdbcUtils()
        val set = jdbc.Query(sql)
        if(!set.next()){
            SendUtils.sendMsg(0,"没有数据",resp)
            return
        }
        set.beforeFirst()
        val listData = DBFromToObject.converToObjectArray(set,clazz)
        SendUtils.sendMsg(listData.size,gson.toJson(listData),resp)
    }

    fun <T>searchAllInfo(clazz: Class<T>){
        var sql = "select * from $type"
        val jdbc = JdbcUtils()
        var set = jdbc.Query(sql)
        if(!set.next()){
            SendUtils.sendMsg(0,"没有数据",resp)
            return
        }
        set.beforeFirst()
        val listData = DBFromToObject.converToObjectArray(set,clazz)
        SendUtils.sendMsg(listData.size, Gson().toJson(listData),resp)
    }


}