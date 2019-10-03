package site.lilpig.lyric.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

interface OnSwipeListener{
    fun onSwipe(dest: Int): Unit
    fun onRelease(): Unit
}
class SwipeView (context: Context,attrs: AttributeSet): View(context,attrs){

    lateinit var swipeListener: OnSwipeListener
    var startY = 0f
    init {
        setOnTouchListener(OnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> startY = motionEvent.rawY
                MotionEvent.ACTION_MOVE -> if (motionEvent.rawY < startY)swipeListener.onSwipe((startY-motionEvent.rawY).toInt())
                MotionEvent.ACTION_UP -> if (startY - motionEvent.rawY > 100)swipeListener.onRelease()
            }
            false
        })
    }
}
