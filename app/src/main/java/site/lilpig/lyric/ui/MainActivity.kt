package site.lilpig.lyric.ui

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import android.os.Build
import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import site.lilpig.lyric.Config
import site.lilpig.lyric.R
import site.lilpig.lyric.app
import site.lilpig.lyric.bean.EverydayLyric
import site.lilpig.lyric.bean.Song
import site.lilpig.lyric.converter.EverydayLyricConverter
import site.lilpig.lyric.utils.toast
import site.lilpig.lyric.views.OnSwipeListener
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    val formatterOfDay = SimpleDateFormat("dd")
    val formatterOfMonth = arrayOf("JAN","FEB","MAR","APR","MAY",
        "JUN","JUL","AUG","SEP","OCT","NOV","DEC")
    val formatterOfYear = SimpleDateFormat("yyyy")
    var todayLyric: EverydayLyric? = null
    var unbinder: Unbinder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
            ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("需要权限")
            alertDialog.setMessage("软件需要读写存储权限用于生成歌词图片和记录软件日志，请在接下来的提示框中勾选同意。")
            alertDialog.setNegativeButton("嗯哼哼", DialogInterface.OnClickListener { dialogInterface, i ->
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE), 1);
                dialogInterface.dismiss()
            })
            alertDialog.show()
        }
        unbinder = ButterKnife.bind(this)
        initView()
        bindEvent()
        bindData()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if( grantResults.size <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED){
                toast("不给权限不让用，哼唧！")
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun bindData() {
        val date = Date()
        am_month_and_year.text = formatterOfMonth[date.month] +" · "+ formatterOfYear.format(date)
        am_day.text = formatterOfDay.format(date)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(Config.ACTIVE_SERVER +"today")
            .get().build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(p0: Call, p1: IOException) {
                p1.printStackTrace()
            }

            override fun onResponse(p0: Call, p1: Response) {
                val everydayLyric = EverydayLyricConverter().convert(p1.body()?.string())
                runOnUiThread {
                    kotlin.run {
                        am_lyric_today.text = everydayLyric.lyric
                        Glide.with(this@MainActivity).load(Uri.parse(everydayLyric.covor))
                            .transition(DrawableTransitionOptions.withCrossFade(100))
                            .into(am_song_bg_covor)
                        am_song_and_artist.text = everydayLyric.song + " · "+everydayLyric.artist
                        am_share_user.text = everydayLyric.upuser
                        todayLyric = everydayLyric
                    }
                }
            }
        })
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
        val toLyricPage = View.OnClickListener{
            if (todayLyric == null){
                toast("获取歌曲失败")
            }else{
                app?.storeAValue("song",Song(todayLyric!!.neteaseid,todayLyric!!.song, arrayOf(todayLyric!!.artist),"",todayLyric!!.covor))
                startActivity(Intent(this@MainActivity, LyricActivity().javaClass))
            }

        }

        am_song_and_artist.setOnClickListener(toLyricPage)
        am_lyric_today.setOnClickListener(toLyricPage)
        am_share.setOnClickListener {
            toast("Method is not implemented!\n并且开发者决定让你自己截图分享")
        }
    }

    fun toSearchActivity(){
        val toSearchIntent = Intent(this, SearchActivity().javaClass)
        setTextAlpha(0f)
        startActivity(toSearchIntent)
        overridePendingTransition(
            R.anim.anim_activity_bottom_fade_in,
            R.anim.anim_activty_hide_out
        )
    }
    fun setTextAlpha(alpha: Float){
        am_bg_container.alpha = alpha
    }

    fun initView(){
        val typeface = Typeface.createFromAsset(assets, "fonts/Roboto-Light.ttf")
        am_day.setTypeface(typeface)
        am_month_and_year.setTypeface(typeface)
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
