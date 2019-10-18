package site.lilpig.lyric.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class SquareLinearLayout(context:Context,attr:AttributeSet) : LinearLayout(context,attr){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val hmsC = if (View.MeasureSpec.getSize(heightMeasureSpec) > View.MeasureSpec.getSize(
                widthMeasureSpec
            )
        ) heightMeasureSpec else widthMeasureSpec
        super.onMeasure(widthMeasureSpec, hmsC)
    }
}