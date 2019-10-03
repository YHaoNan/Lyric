package site.lilpig.lyric.requester

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object CrashHandler : Thread.UncaughtExceptionHandler{
    private var mHandler: Thread.UncaughtExceptionHandler? = null
    private val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSSS")
    override fun uncaughtException(p0: Thread, p1: Throwable) {
        val sdk = Build.VERSION.SDK_INT
        val version = Build.VERSION.RELEASE
        val deviceInfo = Build.BRAND + Build.MODEL
        val tag = Build.TAGS
        val now = format.format(Date())
        val errorMessage = p1.message
        val errorStackTrace = p1.stackTrace


        val stringToWrite = with(StringBuffer()){
            append(deviceInfo+"\n"+sdk+"  "+version+"  "+tag+"\n"+now+"\n"+errorMessage+"\n")
            errorStackTrace.forEach {
                append(it.toString()+"\n")
            }
            toString()
        }

        saveToFile(stringToWrite)
    }

    private fun saveToFile(stringToWrite: String) {
        val os = FileOutputStream(File(Environment.getExternalStorageDirectory(),"crash.txt"))
        os?.write(stringToWrite.toByteArray())
        os?.close()
    }


    fun init(){
        mHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


}