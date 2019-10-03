package site.lilpig.lyric.utils

import android.content.Context
import android.widget.Toast

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
