package site.lilpig.lyric.ui

import android.Manifest
import android.app.ActivityOptions
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import site.lilpig.lyric.Config
import site.lilpig.lyric.R
import site.lilpig.lyric.app
import site.lilpig.lyric.bean.EverydayLyric
import site.lilpig.lyric.bean.Song
import site.lilpig.lyric.converter.AppverConverter
import site.lilpig.lyric.converter.EverydayLyricConverter
import site.lilpig.lyric.service.FloatWindowService
import site.lilpig.lyric.utils.IntentUtils
import site.lilpig.lyric.utils.toast
import site.lilpig.lyric.views.OnSwipeListener
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    val conn = object : ServiceConnection{
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            app?.service = p1 as FloatWindowService.FloatWindowServiceBinder?
        }

    }

    val formatterOfDay = SimpleDateFormat("dd")
    val formatterOfMonth = arrayOf("JAN","FEB","MAR","APR","MAY",
        "JUN","JUL","AUG","SEP","OCT","NOV","DEC")
    val formatterOfYear = SimpleDateFormat("yyyy")
    var todayLyric: EverydayLyric? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Check permissions
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
            ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("需要权限")
            alertDialog.setMessage("软件需要读写存储权限用于生成歌词图片和记录软件日志，请在接下来的提示框中勾选同意。")
            alertDialog.setCancelable(false)
            alertDialog.setNegativeButton("嗯哼哼", DialogInterface.OnClickListener { dialogInterface, i ->
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE), 1);
                dialogInterface.dismiss()
            })
            alertDialog.show()
        }
//         Check if accept protocal
        if (!app!!.isAcceptProtocal()){
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("免责声明")
            alertDialog.setMessage("本软件数据来自网易云音乐Web版，仅用于学习和娱乐，请勿用于商业用途。\n\n" +
                    "所有的歌词由软件标注的歌词上传者所有，尊重知识产权，请勿修改导出的lrc以及翻译中的上传者信息，请勿传播lrc歌词文件。\n\n" +
                    "软件只提供歌词查看和歌词分享功能，一切由用户滥用歌词而产生的版权问题与开发者无关。")
            alertDialog.setCancelable(false)
            alertDialog.setNegativeButton("同意", DialogInterface.OnClickListener { dialogInterface, i ->
                app!!.acceptProtocal()
                dialogInterface.dismiss()
            })
            alertDialog.show()
        }

        initView()
        bindEvent()
        bindData()
        //Bind Service
        bindService(Intent(this,FloatWindowService().javaClass),conn, Context.BIND_AUTO_CREATE)
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
        val requestAppVer = Request.Builder()
            .url(Config.ACTIVE_SERVER+"appver")
            .get().build()
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
        client.newCall(requestAppVer).enqueue(object :Callback{
            override fun onFailure(p0: Call, p1: IOException) {
            }

            override fun onResponse(p0: Call, p1: Response) {
                val appver = AppverConverter().convert(p1.body()?.string())
                runOnUiThread { kotlin.run{
                    if (appver!= null && appver.ver > Config.APPVER){
                        val dialog = AlertDialog.Builder(this@MainActivity)
                        dialog.setTitle("新版本"+appver.ver)
                        dialog.setMessage(appver.info)
                        dialog.setCancelable(appver.isforce == 0)
                        dialog.setPositiveButton("更新",DialogInterface.OnClickListener{ iface,i->
                            IntentUtils.openInBrowser(this@MainActivity,appver.url)
                        })
                        dialog.show()
                    }
                }}
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
            IntentUtils.openInBrowser(this,"orpheus://song/"+todayLyric?.neteaseid)
        }
        am_settings.setOnClickListener {
            startActivity(Intent(this,SettingActivity().javaClass))
        }
    }

    fun toSearchActivity(){
        val toSearchIntent = Intent(this, SearchActivity().javaClass)
        setTextAlpha(0f)
        val sharedView: View = am_search_bar
        val transitionName = getString(R.string.search_bar)

        val transitionActivityOptions: ActivityOptions =
            ActivityOptions.makeSceneTransitionAnimation(
                this@MainActivity,
                sharedView,
                transitionName
            )
        startActivity(toSearchIntent,transitionActivityOptions.toBundle())
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
        unbindService(conn)
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
