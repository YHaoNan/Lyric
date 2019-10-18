package site.lilpig.lyric

import android.app.Application
import android.content.Context
import site.lilpig.lyric.utils.CrashHandler

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

    fun isAcceptProtocal(): Boolean =
        getSharedPreferences("acceptProtocal", Context.MODE_PRIVATE).getBoolean("accept",false);

    fun acceptProtocal(){
        getSharedPreferences("acceptProtocal",Context.MODE_PRIVATE).edit().putBoolean("accept",true).commit()
    }
    fun isMarkClosed(): Boolean{
        return getSharedPreferences("closeMark",Context.MODE_PRIVATE).getBoolean("close",false)
    }

    fun openMark(){
        getSharedPreferences("closeMark",Context.MODE_PRIVATE).edit().putBoolean("close",false).commit()
    }
    fun closeMark(){
        getSharedPreferences("closeMark",Context.MODE_PRIVATE).edit().putBoolean("close",true).commit()
    }
}