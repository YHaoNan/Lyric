package site.lilpig.lyric.ui

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_lyric.*
import site.lilpig.lyric.adapter.LyricAdapter
import site.lilpig.lyric.adapter.OnShareModeStartListener
import site.lilpig.lyric.bean.Lyric
import site.lilpig.lyric.bean.Song
import site.lilpig.lyric.converter.LyricConverter
import site.lilpig.lyric.netease_requester.NeteaseRequester
import site.lilpig.lyric.netease_requester.REQUEST_LYRIC
import site.lilpig.lyric.netease_requester.RequestCallback
import site.lilpig.lyric.netease_requester.RequestJsonFactory
import site.lilpig.lyric.utils.BlurTransformation
import site.lilpig.lyric.utils.join
import site.lilpig.lyric.utils.toast
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.KeyEvent
import site.lilpig.lyric.Config
import site.lilpig.lyric.R
import site.lilpig.lyric.app
import site.lilpig.lyric.converter.LrcConverter
import site.lilpig.lyric.converter.TrcConverter
import site.lilpig.lyric.utils.saveToFile


class LyricActivity : AppCompatActivity(){
    private val mark: String = if(app?.isMarkClosed() ?: false) "" else "\n\n歌词来自“某句”App，宁也来下载试试？https://www.coolapk.com/apk/245764"
    var jsonSource: String? = null
    var lyric: Lyric? = null
    var lyricAdapter: LyricAdapter? = null
    val onShareModeStartListener = object : OnShareModeStartListener {
        override fun changed(bol: Boolean) {
            al_share_bar.visibility = if (bol) View.VISIBLE else View.GONE
        }
    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (lyricAdapter?.hasChecked() ?: false){
                lyricAdapter?.clearChecked()
            }else
                finish()
        }
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric)
        al_recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val song: Song = app?.popAValue("song") as Song

        NeteaseRequester().sendRequest(
            REQUEST_LYRIC, RequestJsonFactory.getLyric(song.id),
            RequestCallback {
                jsonSource = it
                lyric = LyricConverter().convert(it)
                Log.i("SongListAdapter",lyric.toString())
                if (lyric == null){
                    toast("没找到这首歌的歌词啊QAQ")
                    finish()
                    return@RequestCallback
                }
                lyricAdapter = LyricAdapter(this,lyric!!,song,onShareModeStartListener)
                al_recyclerView.adapter = lyricAdapter
            })
        Glide.with(this).load(Uri.parse(song.albumCovor+"?param=240y240"))
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions.skipMemoryCacheOf(true))
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25,this)))
            .into(al_blur_bg)

        al_translate_toggle.setOnClickListener{
            var cur = lyricAdapter?.isShowTranslate
            lyricAdapter?.isShowTranslate = !cur!!
            if (cur==true) al_translate_toggle.text = "翻译"
            else al_translate_toggle.text = "关闭"
        }
        al_lyric_share.setOnClickListener{
            val lyricText = lyricAdapter?.checkedLrc?.join("\n") + mark
            val shareMethods:List<DialogItem> = listOf(
                DialogItem(R.drawable.ic_textsms_black_24dp,"分享文本", View.OnClickListener {
                    var shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, lyricText)
                    shareIntent = Intent.createChooser(shareIntent, "分享歌词")
                    startActivity(shareIntent)
                }),
                DialogItem(R.drawable.ic_insert_drive_file_black_24dp,"导出lrc", View.OnClickListener {
                    val lrcText = LrcConverter().convert(jsonSource)
                    val lrcPath = app?.getLrcStorePath()
                    if(lrcText!=null && lrcPath!=null && lrcText!=null){
                        val savePath  = lrcText.saveToFile(lrcPath,song.name+".lrc")
                        toast("保存到${savePath}")
                    } else toast("导出lrc失败")
                }),
                DialogItem(R.drawable.ic_insert_drive_file_black_24dp,"导出翻译", View.OnClickListener {
                    val trcText = TrcConverter().convert(jsonSource)
                    val lrcPath = app?.getLrcStorePath()
                    if(trcText!=null && lrcPath!=null && trcText!="null"){
                        val savePath  = trcText.saveToFile(lrcPath,song.name+"-tr.lrc")
                        toast("保存到${savePath}")
                    } else toast("导出翻译失败")
                }),
                DialogItem(R.drawable.ic_view_carousel_black_24dp,"歌词悬浮窗",View.OnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                        toast("请赋予显示在其他应用上层权限")
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        startActivity(intent)
                    }else
                        app?.service?.addLyricWindow(lyricAdapter?.checkedLrc!!)
                }),
                DialogItem(R.drawable.ic_cloud_upload_black_24dp,"上传到首页", View.OnClickListener { this@LyricActivity.toast("Method is not implemented...")})
            )
            BottomPopDialog(this,shareMethods).show()
        }
        al_lyric_image.setOnClickListener{
            app?.storeAValue("lyrics",lyricAdapter?.checkedLrc!!)
            app?.storeAValue("song",song)
            startActivity(Intent(this,LyricImageActivity().javaClass))
        }
        al_expand.setOnClickListener{
            lyricAdapter?.expend()
        }
        al_reverse.setOnClickListener{
            lyricAdapter?.reverse()
        }
    }

}