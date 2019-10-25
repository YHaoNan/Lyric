package site.lilpig.lyric.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri


object IntentUtils{
    fun openInBrowser(context: Context,url:String) {
        val intent = Intent()
        intent.setAction(Intent.ACTION_VIEW)
        val content_url = Uri.parse(url)
        intent.setData(content_url)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(Intent.createChooser(intent,"选择浏览器"))
    }
}