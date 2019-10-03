package site.lilpig.lyric.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_lyric.view.*
import org.w3c.dom.Text
import site.lilpig.lyric.R
import site.lilpig.lyric.bean.Lyric
import site.lilpig.lyric.bean.Song
import site.lilpig.lyric.utils.join

val TYPE_HEADER_LA = 0
val TYPE_FOOTER_LA = 1
val TYPE_NORMAL_LA = 2

class LyricAdapter(val context: Context,val lyric: Lyric, val song: Song): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount() = lyric.sentences.size + 2

    override fun getItemViewType(position: Int) = if (position==0) TYPE_HEADER_LA else if(position > 0 && position < lyric.sentences.size + 1 ) TYPE_NORMAL_LA else TYPE_FOOTER_LA

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LyricViewHolder){
            var sentence = lyric.sentences[position - 1 ]
            holder.lrc.text = sentence.sourceLyric
            if (sentence.translateLyric == null)
                holder.trc.visibility = View.GONE
            else{
                holder.trc.visibility = View.VISIBLE
                holder.trc.text = sentence.translateLyric
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == TYPE_HEADER_LA) HeaderHolder(LayoutInflater.from(context).inflate(R.layout.item_lyric_header,parent,false))
        else if (viewType == TYPE_FOOTER_LA) FooterHolder(LayoutInflater.from(context).inflate(R.layout.item_lyric_footer,parent,false))
        else LyricViewHolder(LayoutInflater.from(context).inflate(R.layout.item_lyric,parent,false))

}
class LyricViewHolder(view:View): RecyclerView.ViewHolder(view){
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