package site.lilpig.lyric.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class SquareLinearLayout(context:Context,attr:AttributeSet) : LinearLayout(context,attr){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}