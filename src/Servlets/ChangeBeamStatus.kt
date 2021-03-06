package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.TaskData
import Utils.DBFromToObject
import Utils.SendUtils
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "ChangeBeamStatus",urlPatterns = arrayOf("/changeBeamStatus"))
class ChangeBeamStatus : HttpServlet() {

    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var type = req?.getParameter("type")
        var beamId = req?.getParameter("beamId")
        var bName = req?.getParameter("bName")
        if (!testParamNullOrEmpty(type, beamId,bName)) {
            SendUtils.sendParamError("null", resp)
            return
        }
        type = URLDecoder.decode(type, "UTF-8")
        if (type != inFactory && type != outFactory) {
            SendUtils.sendParamError("type", resp)
            return
        }

        beamId = URLDecoder.decode(beamId,"UTF-8")
        bName = URLDecoder.decode(bName,"UTF-8")

        val simpleDataFormatter = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
        val jdbc = JdbcUtils()
        var buildSQL = "select * from ${BaseSearchServlet.taskForm} where bid = '$beamId' "
        var result = jdbc.Query(buildSQL)
        if (result.next()){
            val plan = DBFromToObject.convertToObject(result,TaskData::class.java)
            var sql = ""
            var changeStoreSQL = ""
            var changeBeamSQL = ""
            var changeStorePosition:String = ""
            if(type == inFactory){
                /*
                * 梁板入场
                * 更新 制梁台状态 设置为空闲（true）
                * 插入库存记录 入场时间为当前时间  出场时间null
                * */
                sql = " update ${BaseSearchServlet.makepositionForm} set Idle = '${1}' where makePosID = '${plan.makePosId}'"
                changeStoreSQL = " insert into ${BaseSearchServlet.storeForm} values ( '$bName' , '$beamId' , '${plan.pedID}' , '${plan.pos}' , '${simpleDataFormatter.format(Date(System.currentTimeMillis()))}' , null )"
                changeBeamSQL = " update ${BaseSearchServlet.beamForm} set status = '已入场' where bName = '$bName' and bId = '$beamId'"
                changeStorePosition = " update ${BaseSearchServlet.storepositionFrom} set status = '$using' where pedId = '${plan.pedID}' and pos = '${plan.pos}' "
            }else{
                /*
                * 梁板出场
                * 更新 存梁台的状态 设置为空闲
                * 更新 库存记录 的出场时间为当前时间
                * */
                sql = " update ${BaseSearchServlet.storepositionFrom} set status = '$empty' where pedId = '${plan.pedID}' and pos = '${plan.pos}'"
                changeStoreSQL = " update ${BaseSearchServlet.storeForm} set outTime = '${simpleDataFormatter.format(Date(System.currentTimeMillis()))}' where bName = '$bName' and bId = '$beamId' "
                changeBeamSQL = " update ${BaseSearchServlet.beamForm} set status = '已出场' where bName = '$bName' and bId = '$beamId'"
            }
            var resultCode = jdbc.update(sql)
            resultCode = if(resultCode==1L){
                if (jdbc.update(changeStoreSQL)==1L){
                    if(type == inFactory){
                        jdbc.update(changeStorePosition)
                    }
                    jdbc.update(changeBeamSQL)
                }else
                    -1
            }else{
                -1
            }
            SendUtils.sendMsg(resultCode.toInt(),"",resp)
        }else{
            SendUtils.sendMsg(-1,"No Task",resp)
        }
        jdbc.releaseResource()
    }

    companion object {
        val hasDone = "已预制"
        val didnt = "未预制"
        val inFactory = "已入库"
        val outFactory = "已出库"
        val empty = "空闲"
        val using = "使用中"
    }
}