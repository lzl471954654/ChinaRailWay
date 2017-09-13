package Servlets

import DataBaseClasses.JdbcUtils
import DataClass.BeamData
import DataClass.BridgeData
import Utils.DBFromToObject
import Utils.SendUtils
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.math.BigDecimal
import java.sql.Date
import java.sql.ResultSet
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.internal.Reflection


@WebServlet(name = "BeamSearch",urlPatterns = arrayOf("/searchBeam"))
class BeamSearch:BaseSearchServlet (){

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
            bridgeID, bridgeName->{
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
        var sql = "select * from beam"
        val jdbc = JdbcUtils()
        var set = jdbc.Query(sql)
        if (!set.next()){
            SendUtils.sendMsg(0,"没有数据",resp)
            return
        }
        set.beforeFirst()
        var list = LinkedList<BeamData>()
        while (set.next()){
            list.add(DBFromToObject.convertToObject(set, BeamData::class.java))
        }
        SendUtils.sendMsg(list.size, JSONArray.fromObject(list).toString(),resp)
    }

    fun search(resp: HttpServletResponse?){
        var sql = "select * from beam where $searchType = '$searchParam'"
        val jdbc = JdbcUtils()
        var set = jdbc.Query(sql)
        if(!set.next()){
            SendUtils.sendMsg(0,"没有数据",resp)
            return
        }
        set.first()
        val data = getBeamData(set)
        SendUtils.sendMsg(1,JSONObject.fromObject(data).toString(),resp)
    }

    fun getBeamData(set:ResultSet):BeamData{
        val data = BeamData()
        val clazz = data.javaClass
        val fields = clazz.declaredFields
        fields.forEach {
            it.isAccessible = true
            when(it.type){
                Int::class.java->{
                    it.set(data,set.getInt(it.name))
                }
                Short::class.java->{
                    it.set(data,set.getShort(it.name))
                }
                Double::class.java->{
                    it.set(data,set.getDouble(it.name))
                }
                Float::class.java->{
                    it.set(data,set.getFloat(it.name))
                }
                Long::class.java->{
                    it.set(data,set.getLong(it.name))
                }
                String::class.java->{
                    it.set(data,set.getString(it.name))
                }
                BigDecimal::class.java->{
                    it.set(data,set.getBigDecimal(it.name))
                }
                Date::class.java->{
                    it.set(data,set.getDate(it.name))
                }
                else->{
                    it.set(data,set.getObject(it.name,it.type))
                }
            }
        }
        return data
    }

    companion object {
        /*
        * BridgeName 桥名
        * BridgeID 桥编号
        * */
        val bridgeName = "bName"
        val bridgeID = "bID"
    }

}