package site.lilpig.lyric.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object HistoryUtil{
    val SPLITOR = "<-^|^->"
    val MAX_HISTORY_COUNT = 10

    private fun getSP(context: Context) = context.getSharedPreferences("historys",Context.MODE_PRIVATE)
    fun getHistorys(context: Context): MutableList<String>? {
        var preferences = getSP(context)
        var historys = preferences.getString("historys","")?.split(SPLITOR)?.toMutableList()
        historys = historys?.filterNot { it.isBlank() } as MutableList<String>?
        Log.i("HistoryUtil",historys.toString())
        return historys
    }

    fun putHistorys(context: Context,name: String){
        var preferences = getSP(context)
        var historys = getHistorys(context)
        if (historys?.contains(name)!!){
            historys.remove(name)
        }
        if(historys?.size!! >= MAX_HISTORY_COUNT){
            historys?.removeAt(historys.lastIndex)
        }
        historys?.add(0,name)
        val newStr = historys.join(SPLITOR)
        preferences.edit().putString("historys",newStr).commit()

    }

}