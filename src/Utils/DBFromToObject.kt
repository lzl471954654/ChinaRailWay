package Utils

import java.math.BigDecimal
import java.sql.Date
import java.sql.ResultSet

object DBFromToObject {

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
}