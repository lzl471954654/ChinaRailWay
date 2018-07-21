package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.TaskData
import Utils.ObjectToDBFrom
import Utils.SendUtils
import com.google.gson.JsonObject
import net.sf.json.JSONObject
import java.net.URLDecoder
import java.text.SimpleDateFormat
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "removeProducePlan",urlPatterns = ["/removePlan"])
class RemoveProducePlan : HttpServlet() {
    companion object {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val jdbc = JdbcUtils()
    }
    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var param = req?.getParameter("data")
        resp?.contentType = "text/json;charset=UTF-8"
        if (!testParamNullOrEmpty(param)) {
            SendUtils.sendMsg(-1, "data is NULL or Empty!", resp)
            return
        }
        param = URLDecoder.decode(param, "UTF-8")
        val json = ObjectToDBFrom.gson.fromJson<TaskData>(param, TaskData::class.java)
        val bName = json.getbName()
        val bID = json.getbID()
        val taskDate = formatter.format(json.taskDate)
        if (!testParamNullOrEmpty(bName, bID, taskDate)) {
            SendUtils.sendParamError("bName,bID,taskDate", resp)
            return
        }
        val sqlString = "select * from ${BaseSearchServlet.taskForm} where TaskDate = '$taskDate' " +
                "and BName = '$bName' " +
                "and BID = '$bID' "
        val result = jdbc.Query(sqlString)
        val hasNext = result.next()
        if (!hasNext) {
            SendUtils.sendMsg(-2, "There is no task found , please check the param", resp)
            result.close()
            return
        }
        val makePosID = result.getInt("MakePosID")
        val pedID: Int? = result.getInt("PedID")
        val pos: String? = result.getString("Pos")

        var answer: String = ""
        var sql = " update ${BaseSearchServlet.makepositionForm} set Idle = '1' where " +
                " MakePosID = '$makePosID' "
        var count = jdbc.update(sql)
        if (count == 1L) {
            answer += "MakePosition has already update !\t"
        }
        if (pedID != null && pos != null) {
            sql = " update ${BaseSearchServlet.storepositionFrom} set Status = '空闲' " +
                    "where PedID = '$pedID' " +
                    "and Pos = '$pos' "
            count = jdbc.update(sql)
            if (count == 1L) {
                answer += "StorePosition has already update !"
            }
        }

        sql = " delete from ${BaseSearchServlet.taskForm} where TaskDate = '$taskDate' " +
                "and BName = '$bName' " +
                "and BID = '$bID' "

        count = jdbc.update(sql)

        if (count == 1L) {
            SendUtils.sendMsg(1, answer, resp)
        } else {
            SendUtils.sendMsg(0, answer, resp)
        }
    }
}