package site.lilpig.lyric

import android.app.Application
import site.lilpig.lyric.requester.CrashHandler

var app: LyricApplication? = null
class LyricApplication : Application(){

    private val valueStore = HashMap<String,Any>()

    override fun onCreate() {
        super.onCreate()
        CrashHandler.init()
        app = this
    }


    fun storeAValue(key:String, obj:Any){
        valueStore.put(key,obj)
    }

    fun popAValue(key: String): Any{
        return valueStore.remove(key)!!
    }
}