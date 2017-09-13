package DataClass

class Token(var value:String,var lastTime:Long) {

    override fun equals(other: Any?): Boolean {
        return if(other is Token){
            if (other.value!=this.value)
                false
            else
                other.lastTime-this.lastTime < age
        }
        else
            false
    }

    companion object {
        @JvmStatic
        val age:Long = 30*60*1000
    }
}