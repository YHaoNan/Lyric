package site.lilpig.lyric

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_lyric.*
import site.lilpig.lyric.adapter.LyricAdapter
import site.lilpig.lyric.bean.Lyric
import site.lilpig.lyric.bean.Song
import site.lilpig.lyric.converter.LyricConverter
import site.lilpig.lyric.requester.NeteaseRequester
import site.lilpig.lyric.requester.REQUEST_LYRIC
import site.lilpig.lyric.requester.RequestCallback
import site.lilpig.lyric.requester.RequestJsonFactory
import site.lilpig.lyric.utils.toast

class LyricActivity : AppCompatActivity(){

    var lyric: Lyric? = null
    var unbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric)
        unbinder = ButterKnife.bind(this)

        al_recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val song: Song = app?.popAValue("song") as Song

        NeteaseRequester().sendRequest(
            REQUEST_LYRIC, RequestJsonFactory.getLyric(song.id),
            RequestCallback {
                lyric = LyricConverter().convert(it)
                Log.i("SongListAdapter",lyric.toString())
                if (lyric == null){
                    toast("没找到这首歌的歌词啊QAQ")
                    finish()
                    return@RequestCallback
                }
                al_recyclerView.adapter = LyricAdapter(this,lyric!!,song)
            })
        Glide.with(this).load(Uri.parse(song.albumCovor+"?param=130y130")).apply(RequestOptions.bitmapTransform(BlurTransformation(100))).into(al_blur_bg)
    }

    override fun onResume() {
        super.onResume()
        unbinder?.unbind()
    }
}