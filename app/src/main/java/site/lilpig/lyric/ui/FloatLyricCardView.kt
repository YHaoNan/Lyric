package site.lilpig.lyric.ui

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.float_lyric_card_view.view.*
import site.lilpig.lyric.R
import site.lilpig.lyric.app
import site.lilpig.lyric.utils.DisplayUtil
import site.lilpig.lyric.utils.join
import site.lilpig.lyric.utils.toast

class FloatLyricCardView(val context: Context,val id: String,val lyric: List<String>){
    private var isLocked = false
    private var isEditable = false
    private var width = 600
    private var height = 800

    private var minHeight = DisplayUtil.dip2px(context,100f)
    private var minWidth = DisplayUtil.dip2px(context,140f)
    private val windowParams: WindowManager.LayoutParams
    private val view: View
    private inner class MoveTouchListener: View.OnTouchListener{
        var x = 0
        var y = 0
        override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {
            if (isLocked)return true
            when(motionEvent?.action){
                MotionEvent.ACTION_DOWN -> {
                    x = motionEvent.rawX.toInt()
                    y = motionEvent.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = motionEvent.rawX.toInt()
                    val nowY = motionEvent.rawY.toInt()
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    windowParams.x += movedX
                    windowParams.y += movedY
                    app?.service?.updateWindow(this@FloatLyricCardView)
                }
            }
            return true

        }

    }

    private inner class ScaleTouchListener: View.OnTouchListener{
        private var x = 0
        private var y = 0
        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            if (isLocked)return true
            when(p1?.action){
                MotionEvent.ACTION_DOWN ->{
                    x = p1.rawX.toInt()
                    y = p1.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = p1.rawX.toInt()
                    val nowY = p1.rawY.toInt()
                    val moveX = nowX - x
                    val moveY = nowY - y
                    val finalWidth = windowParams.width + moveX
                    val finalHeight = windowParams.height + moveY

                    windowParams.width = Math.max(finalWidth,minWidth)
                    windowParams.height = Math.max(finalHeight,minHeight)

                    x = nowX
                    y = nowY
                    app?.service?.updateWindow(this@FloatLyricCardView)
                }
            }
            return true
        }

    }
    init {
        windowParams = WindowManager.LayoutParams()
        view = LayoutInflater.from(context).inflate(R.layout.float_lyric_card_view,null)
        view.flcv_lyric.setText(lyric.join("\n"))
        view.flcv_close.setOnClickListener {
            app?.service?.removeLyricWindow(id)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            windowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            windowParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        windowParams.format = PixelFormat.RGBA_8888
        windowParams.gravity = Gravity.TOP or Gravity.LEFT
        windowParams.x = 0
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        windowParams.y = 0
        windowParams.width = width
        windowParams.height = height
        view.flcv_move.setOnTouchListener(MoveTouchListener())
        view.flcv_resize.setOnTouchListener(ScaleTouchListener())
        view.flcv_nail.setOnClickListener {
            isLocked=!isLocked
            view.flcv_nail.setImageResource(if(isLocked) R.drawable.ic_lock_on else R.drawable.ic_lock_off)
        }
        view.flcv_edit.setOnClickListener {
            windowParams.flags = if (isEditable){
                view.flcv_edit.setImageResource(R.drawable.ic_edit_off)
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            }else{
                view.flcv_edit.setImageResource(R.drawable.ic_edit_on)
                0
            }
            isEditable = !isEditable
            app?.service?.updateWindow(this)
        }

    }

    operator fun component1() = view
    operator fun component2() = windowParams
}