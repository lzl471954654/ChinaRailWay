package Servlets

import DataBaseClasses.DBConnection
import DataBaseClasses.JdbcUtils
import DataClass.TaskData
import Utils.SendUtils
import java.net.URLDecoder
import java.sql.Date
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "modifyTask",urlPatterns = arrayOf("/modifyTask"))
class ModifyTask:HttpServlet() {

    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var data = req!!.getParameter("data")
        val commite = req.getParameter("commite")

        if (!testParamNullOrEmpty(data)){
            SendUtils.sendParamError("data",resp)
            return
        }
        data = URLDecoder.decode(data,"UTF-8")
        val tasks = BaseSearchServlet.gson.fromJson(data,Array<TaskData>::class.java)
        println("first TaskDate is ${tasks[0].taskDate}")
        var count = 0
        val connection = DBConnection.getNewConnection()
        if(connection==null){
            SendUtils.sendMsg(-2,"DataBases is busy , please wait ",resp)
            return
        }
        val point = connection.setSavepoint("last")
        val state = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE)

        val set = state.executeQuery("select * from factory")
        val ability = if (set.next()){
            set.getInt("Ability")
        }else -1

        if(ability == -1){
            SendUtils.sendMsg(-3,"factory error",resp)
            return
        }
        var flag = false
        tasks.forEach {
            var sql = " update task set makeOrder = '${it.makeOrder}' , permit = '${if(it.isPermit) 1 else 0}'  where taskdate = '${it.taskDate}' and bName = '${it.getbName()}' and bID = '${it.getbID()}' "
            println(sql)
            var res = state.executeLargeUpdate(sql)
            if(res==1L){

                if(it.isPermit){
                    res = state.executeLargeUpdate("update Beam set status ='制作中' where bName = '${it.getbName()}' and bID = '${it.getbID()}' ")
                        if(res == 1L){
                        count++
                    }else{
                        flag = true
                        return@forEach
                    }
                }
            }
        }
        if(commite == "1"){
            SendUtils.sendMsg(1,"update ok",resp)
            state.close()
            connection.close()
            return
        }
        if(flag)
        {
            SendUtils.sendMsg(-4,"update failed",resp)
            connection.rollback(point)
            state.close()
            connection.close()
            return
        }
        /**
         * 参数中的 '' 是英文的
         * */
        val resultSet = state.executeQuery("select * from task where TaskDate = '${tasks[0].taskDate}'  order by makeOrder")

        resultSet.last()
        val countSum = resultSet.row
        if(countSum == 0)
        {
            SendUtils.sendMsg(-5,"no result",resp)
            connection.rollback(point)
            state.close()
            connection.close()
            return
        }
        resultSet.beforeFirst()
        val days : Int = countSum/ability
        if(days == 0){
            SendUtils.sendMsg(1,"success",resp)
            state.close()
            connection.close()
            return
        }
        var date = tasks[0].taskDate
        val calender = Calendar.getInstance()
        calender.time = date
        var i = 0
        while (resultSet.next()){
            resultSet.updateDate(1,Date(calender.timeInMillis))
            i++
            if(i == ability){
                calender.add(Calendar.DATE,1)
                i = 0
            }
        }
        SendUtils.sendMsg(1,"successful",resp)
        state.close()
        connection.close()
    }

    /*override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var data = req!!.getParameter("data")
        data = URLDecoder.decode(data,"UTF-8")
        val tasks = BaseSearchServlet.gson.fromJson(data,Array<TaskData>::class.java)
        var count = 0
        val jdbc = JdbcUtils()

        tasks.forEach {
            var sql = " update task set makeOrder = '${it.makeOrder}' , permit = '${if(it.isPermit) 1 else 0}'  where taskdate = '${it.taskDate}' and bName = '${it.getbName()}' and bID = '${it.getbID()}' "
            println(sql)
            val res = jdbc.update(sql)
            if(res==1L)
            {
                count++
                if(it.isPermit){
                    sql = " update beam set status = ‘制作中’ where bName = '${it.getbName()}' and bID = '${it.getbID()}'"
                    jdbc.update(sql)
                }
            }
        }
        resp?.contentType = "text/json;charset=UTF-8"
        SendUtils.sendMsg(count,"",resp)
        jdbc.releaseResource()
    }*/
}