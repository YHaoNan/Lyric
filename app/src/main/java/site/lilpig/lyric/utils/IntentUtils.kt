package site.lilpig.lyric.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri


object IntentUtils{
    fun openInBrowser(context: Context,url:String){
        val intent = Intent()
        intent.setAction("android.intent.action.VIEW")
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        val contentUri = Uri.parse(url)
        intent.setData(contentUri)
        intent.setComponent(ComponentName("com.android.browser", "com.android.browser.BrowserActivity"))
        context.startActivity(intent)
    }
}