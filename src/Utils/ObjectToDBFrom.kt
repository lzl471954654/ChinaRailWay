package Utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.math.BigDecimal
import java.sql.Date

object ObjectToDBFrom {
    @JvmStatic
    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()
    @JvmStatic
    fun convertToSQLInsertValues(srcObject : Any):String{
        val builder = StringBuilder()
        val clazz = srcObject::class.java
        clazz.declaredFields.forEach {
            it.isAccessible = true
            when(it.type){
                Int::class.java->{
                    val data = it.getInt(srcObject)
                    builder.append(" $data ,")
                }
                String::class.java->{
                    val data = it.get(srcObject) as String?
                    builder.append(" ${if (data==null) null else "'$data'"} ,")
                }
                Short::class.java->{
                    val data = it.getShort(srcObject)
                    builder.append(" $data ,")
                }
                BigDecimal::class.java->{
                    val data = it.get(srcObject) as BigDecimal?
                    builder.append(" ${data?.toDouble()} ,")
                }
                Double::class.java->{
                    val data = it.getDouble(srcObject)
                    builder.append(" $data ,")
                }
                Long::class.java->{
                    val data = it.getLong(srcObject)
                    builder.append(" $data ,")
                }
                Float::class.java->{
                    val data = it.getFloat(srcObject)
                    builder.append(" $data ,")
                }
                Boolean::class.java->{
                    val data = it.getBoolean(srcObject)
                    builder.append(" $data ,")
                }
                java.util.Date::class.java->{
                    val data = it.get(srcObject) as java.util.Date?
                    if(data!=null){
                        val date = Date(data.time)
                        builder.append(" '$date' ,")
                    }
                    else
                        builder.append(" null ,")
                }
                else->{
                    val data = it.get(srcObject)
                    builder.append(" '$data' ,")
                }
            }
        }
        return builder.substring(0 until builder.length-1)
    }
}