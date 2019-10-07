package site.lilpig.lyric.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.lang.IndexOutOfBoundsException

fun Context.toast(message: String) = Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
fun Context.longToast(message: String) = Toast.makeText(this,message,Toast.LENGTH_LONG).show()

fun List<String>.join(sp: String,filterBlank: Boolean = false)=
    with(StringBuffer()){
        this@join.forEachIndexed { index,string ->
            if (!filterBlank || !string.isBlank()) {
                append(string)
                if (index < this@join.size -1)
                    append(sp)
            }
        }
        toString()
    }
fun Array<String>.join(sp: String)=
    with(StringBuffer()){
        this@join.forEachIndexed { index,string ->
            append(string)
            if (index < this@join.size -1)
                append(sp)
        }
        toString()
    }


fun <T>List<T>?.safeGet(index: Int) = try {
    this?.get(index)
}catch (e: IndexOutOfBoundsException){null}



fun String.saveToFile(path: String,filename: String):String {
    val defaultDir = File(Environment.getExternalStorageDirectory(),"mj_lyric")
    if (!defaultDir.exists() || defaultDir.isFile){
        defaultDir.mkdir()
    }
    val path = File(defaultDir,path)
    if(!path.exists() || path.isFile){
        path.mkdirs()
    }

    val fileToSave = File(path,filename)
    val os = FileOutputStream(fileToSave)
    os?.write(this.toByteArray())
    os?.close()
    return fileToSave.absolutePath
}

fun ScrollView.generateImage(): Bitmap{
    var h = 0
    var bitmap: Bitmap? = null
    for (i in 0..childCount-1){
        h += getChildAt(i).getHeight();
    }
    bitmap = Bitmap.createBitmap(getWidth(), h,
        Bitmap.Config.ARGB_8888);
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap;
}
fun Bitmap.saveToGallery(context:Context,path: String,filename: String): String{
    val defaultDir = File(Environment.getExternalStorageDirectory(),"mj_lyric")
    if (!defaultDir.exists() || defaultDir.isFile){
        defaultDir.mkdir()
    }
    val path = File(defaultDir,path)
    if(!path.exists() || path.isFile){
        path.mkdirs()
    }

    val fileToSave = File(path,filename)
    val fos = FileOutputStream(fileToSave)
    compress(Bitmap.CompressFormat.JPEG,100,fos)
    fos.flush()
    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileToSave)))
    return fileToSave.absolutePath
}