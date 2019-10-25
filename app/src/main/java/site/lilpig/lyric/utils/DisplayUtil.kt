package site.lilpig.lyric.utils

import android.content.Context

object DisplayUtil{
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.getResources()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}