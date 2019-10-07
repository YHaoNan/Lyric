package site.lilpig.lyric.utils

import android.content.Context

object DisplayUtil{
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.getResources()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
}