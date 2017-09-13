package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.BridgeData
import Utils.DBFromToObject
import Utils.SendUtils
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.util.*
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class BridgeSearch:BaseSearchServlet() {

    override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        super.service(req, resp)
        if(!loginFlag)
            return
        if(searchAll == "1")
        {
            searchAllInfo(resp)
            return
        }
        when(searchType){
            bridgeName->{
                search(resp)
                return
            }
            else->{
                SendUtils.sendParamError("SearchType",resp!!)
                return
            }
        }
    }

    fun searchAllInfo(resp: HttpServletResponse?){
        var sql = "select * from bridge"
        val jdbc = JdbcUtils()
        var set = jdbc.Query(sql)
        if (!set.next()){
            SendUtils.sendMsg(0,"没有数据",resp)
            return
        }
        set.beforeFirst()
        var list = LinkedList<BridgeData>()
        while (set.next()){
            list.add(DBFromToObject.convertToObject(set,BridgeData::class.java))
        }
        SendUtils.sendMsg(list.size,JSONArray.fromObject(list).toString(),resp)
    }

    fun search(resp: HttpServletResponse?){
        var sql = "select * from bridge where BName = '$searchParam'"
        val jdbc = JdbcUtils()
        var set = jdbc.Query(sql)
        println("set was NULL:${set.wasNull()}")
        if(!set.next()){
            SendUtils.sendMsg(0,"没有数据",resp)
            return
        }
        set.first()
        val bName = set.getString("BName")
        val fromToNum = set.getString("FromToNum")
        val memo = set.getString("Memo")
        var bridge = BridgeData(bName,fromToNum,memo)
        SendUtils.sendMsg(1,JSONObject.fromObject(bridge).toString(),resp)
    }

    companion object {
        @JvmStatic
        val bridgeName = "bName"
    }
}