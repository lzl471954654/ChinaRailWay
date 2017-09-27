package Utils

import java.math.BigDecimal
import java.sql.Date
import java.sql.ResultSet
import java.util.*
import kotlin.collections.ArrayList

object DBFromToObject {

    fun<T> converToObjectArray(set: ResultSet,clazz: Class<T>):LinkedList<T>{
        val array = LinkedList<T>()
        while (set.next()){
            array.add(convertToObject(set,clazz))
        }
        return array
    }

    fun<T> convertToObject(set:ResultSet,clazz: Class<T>):T{
        val data = clazz.newInstance()
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
                    val data1 = set.getString(it.name)
                    if(data1 == "null")
                        it.set(data,null)
                    else
                        it.set(data,data1)
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
}