package site.lilpig.lyric.ui

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.Unbinder
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
import site.lilpig.lyric.R
import site.lilpig.lyric.app
import site.lilpig.lyric.converter.LrcConverter
import site.lilpig.lyric.converter.TrcConverter
import site.lilpig.lyric.utils.saveToFile


class LyricActivity : AppCompatActivity(){
    var jsonSource: String? = null
    var lyric: Lyric? = null
    var lyricAdapter: LyricAdapter? = null
    var unbinder: Unbinder? = null
    val onShareModeStartListener = object : OnShareModeStartListener {
        override fun changed(bol: Boolean) {
            al_share_bar.visibility = if (bol) View.VISIBLE else View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric)
        unbinder = ButterKnife.bind(this)
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
            if (cur==true) al_translate_toggle.text = "打开翻译"
            else al_translate_toggle.text = "关闭翻译"
        }
        al_lyric_share.setOnClickListener{
            val lyricText = lyricAdapter?.checkedLrc?.join("\n")
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
                    if(lrcText!=null){
                        val savePath  = lrcText.saveToFile("lyrics",song.name+".lrc")
                        toast("保存到${savePath}")
                    } else toast("导出lrc失败")
                }),
                DialogItem(R.drawable.ic_insert_drive_file_black_24dp,"导出翻译", View.OnClickListener {
                    val trcText = TrcConverter().convert(jsonSource)
                    if(trcText!=null){
                        val savePath  = trcText.saveToFile("lyrics",song.name+"-tr.lrc")
                        toast("保存到${savePath}")
                    } else toast("导出翻译失败")
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
    }

    override fun onResume() {
        super.onResume()
        unbinder?.unbind()
    }
}