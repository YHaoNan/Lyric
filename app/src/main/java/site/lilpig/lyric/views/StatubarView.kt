package site.lilpig.lyric.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Display
import android.view.View
import site.lilpig.lyric.utils.DisplayUtil

class StatubarView(context: Context,attrs: AttributeSet) : View(context){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.makeMeasureSpec(DisplayUtil.getStatusBarHeight(context),MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, height)
    }

}