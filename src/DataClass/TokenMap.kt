package DataClass

import java.util.concurrent.ConcurrentHashMap

object TokenMap {
    @JvmStatic
    var map = ConcurrentHashMap<String, Token>()

    @JvmStatic
    fun containsUser(id: String): Boolean {
        return map.containsKey(id)
    }
    @JvmStatic
    fun verifyUser(id: String?,tokenValue: String?): Boolean {
        if(id==null || tokenValue == null)
            return false
        if(!containsUser(id))
            return false
        return map[id]!! == Token(tokenValue!!,System.currentTimeMillis())
    }
    @JvmStatic
    fun addUser(id: String,tokenValue:String){
        val token = Token(tokenValue,System.currentTimeMillis())
        if(containsUser(id))
            map.set(id,token)
        else
            map.put(id,token)
    }
}
