package site.lilpig.lyric.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_lyric.view.*
import org.w3c.dom.Text
import site.lilpig.lyric.R
import site.lilpig.lyric.bean.Lyric
import site.lilpig.lyric.bean.Sentence
import site.lilpig.lyric.bean.Song
import site.lilpig.lyric.utils.DisplayUtil
import site.lilpig.lyric.utils.IntentUtils
import site.lilpig.lyric.utils.join
import site.lilpig.lyric.utils.toast

val TYPE_HEADER_LA = 0
val TYPE_FOOTER_LA = 1
val TYPE_NORMAL_LA = 2

interface OnShareModeStartListener{
    fun changed(bol: Boolean)
}
class LyricAdapter(val context: Context,val lyric: Lyric, val song: Song, val onShareModeStartListener: OnShareModeStartListener): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val dotDrawable = context.getDrawable(R.drawable.dot_white)

    private var checkedCount = 0
    private var checkedLyric = Array<Boolean>(lyric.sentences.size,{false})

    init {
        dotDrawable?.bounds = Rect(0,0,DisplayUtil.dip2px(context,10f),DisplayUtil.dip2px(context,10f))
    }
    override fun getItemCount() = lyric.sentences.size + 2

    override fun getItemViewType(position: Int) = if (position==0) TYPE_HEADER_LA else if(position > 0 && position < lyric.sentences.size + 1 ) TYPE_NORMAL_LA else TYPE_FOOTER_LA

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LyricViewHolder){
            var sentence = lyric.sentences[position - 1 ]
            holder.lrc.text = sentence.sourceLyric
            if (sentence.translateLyric == null || !_isShowTranslate || sentence.translateLyric?.isBlank()!!)
                holder.trc.visibility = View.GONE
            else{
                holder.trc.visibility = View.VISIBLE
                holder.trc.text = sentence.translateLyric
            }
            fun setBG(view:View,checked: Boolean){
                view.setBackgroundColor(if (!checked) 0b00000000 else Color.parseColor("#90000000"))
            }

            setBG(holder.view,checkedLyric[position-1])
            holder.lrc.setCompoundDrawables(if (checkedLyric[position-1]) dotDrawable else null,null,null,null)
            holder.view.setOnClickListener{
                if (lyric.sentences[position-1].sourceLyric.isBlank()){
                    return@setOnClickListener
                }
                val equalsZeroBefore = checkedCount==0
                checkedLyric[position-1] = !checkedLyric[position-1]
                checkedCount += if (checkedLyric[position -1 ]==true) 1 else -1
                if (checkedCount == 0)
                    onShareModeStartListener.changed(false)
                if (equalsZeroBefore && checkedCount == 1)
                    onShareModeStartListener.changed(true)
                setBG(it, checkedLyric[position-1])
                holder.lrc.setCompoundDrawables(if (checkedLyric[position-1]) dotDrawable else null,null,null,null)
                false
            }
        }else if (holder is FooterHolder){
            holder.upUser.text = with(StringBuffer()){
                if(lyric.author!=null){
                    append("歌词由 ${lyric.author} 上传")
                }
                if (lyric.translateAuthor!=null){
                    if (length!=0)append(" ")
                    append("由 ${lyric.translateAuthor} 翻译")
                }
                toString()
            }
        }else if (holder is HeaderHolder){
            holder.songName.text = song.name
            holder.artists.text = song.artists.join(" / ")
            holder.songName.setOnClickListener{
                IntentUtils.openInBrowser(context,"orpheus://song/"+song.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == TYPE_HEADER_LA) HeaderHolder(LayoutInflater.from(context).inflate(R.layout.item_lyric_header,parent,false))
        else if (viewType == TYPE_FOOTER_LA) FooterHolder(LayoutInflater.from(context).inflate(R.layout.item_lyric_footer,parent,false))
        else LyricViewHolder(LayoutInflater.from(context).inflate(R.layout.item_lyric,parent,false))

    fun hasChecked() = checkedCount > 0
    val hasTranslate: Boolean
        get() = lyric.sentences.all { it.translateLyric != null }
    private var _isShowTranslate = true
    var isShowTranslate: Boolean
        get() = _isShowTranslate
        set(bol: Boolean) {
            _isShowTranslate = bol
            notifyDataSetChanged()
        }
    fun reverse(){
        for(i in 0..lyric.sentences.size - 1){
            checkedLyric[i] = !checkedLyric[i]
        }
        notifyDataSetChanged()
    }
    fun expend(){
        var startI = -1
        for (start in 0..lyric.sentences.size){
            if (checkedLyric[start]){
                startI = start
                break
            }
        }

        if (startI == -1) return
        var endI = -1
        for (end in lyric.sentences.size - 1 downTo startI + 1){
            if (checkedLyric[end]){
                endI = end
                break
            }
        }
        if (endI != -1 ){
            for (i in startI..endI){
                if (lyric.sentences[i].sourceLyric != "")
                    checkedLyric[i] = true
            }
            notifyDataSetChanged()
        }
    }


    val checkedLrc: List<String>
        get(){
            val lists = mutableListOf<String>()
            lyric.sentences.forEachIndexed {index: Int, sentence: Sentence ->
                if (!checkedLyric[index])return@forEachIndexed
                lists.add(sentence.sourceLyric)
                if (sentence.translateLyric!=null && isShowTranslate){
                    lists.add(sentence.translateLyric)
                }
            }
            return lists
        }
    fun clearChecked(){
        checkedCount = 0 //Tmp
        checkedLyric = Array<Boolean>(lyric.sentences.size,{b->false})
        onShareModeStartListener.changed(false)
        notifyDataSetChanged()
    }
}
class LyricViewHolder(val view:View): RecyclerView.ViewHolder(view){
    var lrc: TextView
    var trc: TextView
    init {
        lrc = view.findViewById(R.id.il_lrc)
        trc = view.findViewById(R.id.il_trc)
    }
}
class HeaderHolder(view: View): RecyclerView.ViewHolder(view){
    var songName: TextView
    var artists: TextView
    init {
        songName = view.findViewById(R.id.ilh_song_name)
        artists = view.findViewById(R.id.ilh_song_artists)
    }
}
class FooterHolder(view: View): RecyclerView.ViewHolder(view){
    var upUser: TextView
    init {
        upUser = view.findViewById(R.id.ilf_up_user)
    }
}