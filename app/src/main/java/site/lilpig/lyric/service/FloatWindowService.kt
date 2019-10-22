package site.lilpig.lyric.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.WindowManager
import site.lilpig.lyric.ui.FloatLyricCardView
import java.util.*

class FloatWindowService : Service(){

    private val windowMap = hashMapOf<String, View>()

    private var windowManager: WindowManager? = null
    // Some public method
    inner class FloatWindowServiceBinder : Binder(){
        /**
         * Add a lyric window
         * @param lyric The lyric that need showing
         * @return The id of the window . Use it when you wanna remove this window from screen.
         */
        fun addLyricWindow(lyric: List<String>): String{
            val id = UUID.randomUUID().toString()
            val (view,params) = FloatLyricCardView(applicationContext,id,lyric)
            windowMap.put(id,view)
            windowManager?.addView(view,params)
            return id
        }

        /**
         * Remove a lyric window
         * @param id The id of the window. It will be return when you call addLyricWindow
         */
        fun removeLyricWindow(id: String){
            windowManager?.removeView(windowMap.remove(id))
        }

        fun updateWindow(view: FloatLyricCardView){
            val (v,p) = view
            windowManager?.updateViewLayout(v,p)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        return FloatWindowServiceBinder()
    }

}