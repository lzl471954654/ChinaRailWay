package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.*
import Utils.DBFromToObject
import Utils.SendUtils
import com.google.gson.Gson
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import sun.util.resources.cldr.ebu.CalendarData_ebu_KE
import java.sql.Date
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "NormalSearch",urlPatterns = arrayOf("/search"))
class NormalSearch:BaseSearchServlet() {
    var type:String? = ""
    var week:String? = ""
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
            buildPlanForm->makePlanTask()
            checkRecForm->search(CheckRecData::class.java)
            filesForm->search(FilesData::class.java)
            makepositionForm->search(MakePosition::class.java)
            modelForm->search(ModelData::class.java)
            monthdataForm->search(MonthData::class.java)
            storeForm->search(StoreData::class.java)
            storepositionFrom->search(StorePositionData::class.java)
            //taskForm->search(TaskData::class.java)
            taskForm->{week = req?.getParameter("week");task(TaskData::class.java)}
            "taskwithbeam"->taskWithBeam()
            else->{
                SendUtils.sendParamError("FormTypeError",resp!!)
                return
            }
        }

    }


    private fun makePlanTask(){
        val dayArray = getWeekStartAndEndByCount(1)
        val sql = "select * from buildPlan where bFromDate <= '${dayArray[1]}' and bFromDate >= '${dayArray[0]}'"
        val jdbc = JdbcUtils()
        val set = jdbc.Query(sql)
        if(!set.next()){
            SendUtils.sendMsg(0,"没有数据",resp)
            return
        }
        set.beforeFirst()
        val listData = DBFromToObject.converToObjectArray(set,BuildPlan::class.java)
        println("day0 ${dayArray[0].time}  day1 ${dayArray[1].time}")
        /*val data = listData.filter {
            println(it.getbFromDate().time)
            val result = it.getbFromDate().time<=dayArray[1].time&&it.getbFromDate().time>=dayArray[0].time
            println(result)
            result
        }
        listData.forEach {
            println(it.getbFromDate())
        }*/
        SendUtils.sendMsg(listData.size,gson.toJson(listData),resp)
    }

    private fun getDayDistanceMonDay(calendar: Calendar):Int{
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return if(dayOfWeek==1)
            -6
        else
            2-dayOfWeek
    }

    private fun getWeekStartAndEndByCount(count:Int):Array<Date>{
        val date = java.util.Date(System.currentTimeMillis())
        val calender = Calendar.getInstance()
        calender.time = date
        calender.firstDayOfWeek = Calendar.MONDAY
        calender.add(Calendar.DAY_OF_WEEK,7*count)
        val distance = getDayDistanceMonDay(calender)
        calender.add(Calendar.DAY_OF_WEEK,distance)
        val monday = calender.time
        calender.add(Calendar.DAY_OF_WEEK,6)
        val sunday = calender.time
        return arrayOf(Date((monday.time/100000)*100000),Date((sunday.time/100000)*100000))

    }

    private fun <T>task(clazz:Class<T>){
        if(searchAll != "3"){
            search(clazz)
            return
        }
        val count:Int = if(week==null||week == ""){
            0
        }else{
            var c = 0
            try {
                c = week!!.toInt()
            }catch (e:Exception){
                e.printStackTrace()
            }
            c
        }
        val date = getWeekStartAndEndByCount(count)
        println("${date[0]}\t${date[1]}")
        val sql = "select * from task where taskDate >= '${date[0]}' and taskDate <= '${date[1]}'"
        val jdbc = JdbcUtils()
        val set = jdbc.Query(sql)
        if(!set.next()){
            SendUtils.sendMsg(0,"没有数据",resp)
            return
        }
        set.beforeFirst()
        val listData = DBFromToObject.converToObjectArray(set,TaskData::class.java)
        SendUtils.sendMsg(listData.size, gson.toJson(listData),resp)
    }

    private fun taskWithBeam(){
        var sql = ""
        if(searchAll == "0")
        {
            sql = "select beam.* , task.* from beam,task where beam.bid = task.bid and beam.bName = task.bName and task.taskDate = '${Date(System.currentTimeMillis())}' "
        }else
            sql = "select beam.* , task.* from beam,task where beam.bid = task.bid and beam.bName = task.bName"
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
        SendUtils.sendMsg(listData.size, gson.toJson(listData),resp)
    }


}