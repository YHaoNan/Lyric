package site.lilpig.lyric

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Typeface
import android.util.Log
import android.widget.RelativeLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import kotlinx.android.synthetic.main.activity_main.*
import site.lilpig.lyric.requester.NeteaseRequester
import site.lilpig.lyric.requester.RequestCallback
import site.lilpig.lyric.requester.RequestJsonFactory
import site.lilpig.lyric.views.OnSwipeListener
import site.lilpig.lyric.views.SwipeView


class MainActivity : AppCompatActivity() {

    var unbinder: Unbinder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        unbinder = ButterKnife.bind(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        initView()
        bindEvent()
    }

    private fun bindEvent() {
        am_search_bar.setOnClickListener {
            toSearchActivity()
        }
        am_top_covor.swipeListener = object : OnSwipeListener{
            override fun onSwipe(dest: Int) {
                Log.i("MainActivity",dest.toString())
                setTextAlpha(100/dest.toFloat())
            }

            override fun onRelease() {
                toSearchActivity()
            }
        }

    }

    fun toSearchActivity(){
        val toSearchIntent = Intent(this,SearchActivity().javaClass)
        setTextAlpha(0f)
        startActivity(toSearchIntent)
        overridePendingTransition(R.anim.anim_activity_bottom_fade_in,R.anim.anim_activty_hide_out)
    }
    fun setTextAlpha(alpha: Float){
        am_bg_container.alpha = alpha
    }

    fun initView(){
        val typeface = Typeface.createFromAsset(assets, "fonts/Roboto-Light.ttf")
        am_day.setTypeface(typeface)
        am_month_and_year.setTypeface(typeface)
        Glide.with(this).load(R.drawable.bg_test).into(am_song_bg_covor)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
    }

    override fun onResume() {
        super.onResume()
        Thread{
            kotlin.run {
                var alpha = 0f
                while (alpha<=1f){
                    alpha+=0.05f
                    runOnUiThread({
                        kotlin.run { setTextAlpha(alpha) }
                    })
                    Thread.sleep(20)
                }
            }
        }.start()
    }
}
