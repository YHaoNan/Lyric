package site.lilpig.lyric.adapter

import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.lilpig.lyric.ui.LyricActivity
import site.lilpig.lyric.R
import site.lilpig.lyric.app
import site.lilpig.lyric.bean.Song
import site.lilpig.lyric.utils.join
import java.util.regex.Pattern

val TYPE_FOOTER = -1
val TYPE_ITEM = 0

class SongListAdapter(val keyword: String,val songs: List<Song>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song,parent,false)
            SongListViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_netease_footer,parent,false)
            NeteaseFooterViewHolder(view)
        }
    }

    override fun getItemCount() = songs.size + 1

    override fun getItemViewType(position: Int) = if(position>=(songs.size)) TYPE_FOOTER else TYPE_ITEM



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SongListViewHolder){
            val song = songs[position]
            val songNameSpannable = SpannableString(song.name)
            val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#ef9056"))
            val pattern = Pattern.compile(keyword.toLowerCase())
            val matcher = pattern.matcher(song.name.toLowerCase())
            while (matcher.find()){
                songNameSpannable.setSpan(foregroundColorSpan,matcher.start(),matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            holder.songName.text = songNameSpannable
            holder.artistsAndAlbum.text  = song.artists.join(" / ") + " - " + song.album
            holder.view.setOnClickListener({
                with(holder.view.context){
                    app?.storeAValue("song",song)
                    startActivity(Intent(this, LyricActivity().javaClass))
                }
            })
        }
    }

}

class SongListViewHolder(val view: View) : RecyclerView.ViewHolder(view){
    val songName: TextView
    val artistsAndAlbum: TextView

    init {
        songName = view.findViewById(R.id.is_songname)
        artistsAndAlbum = view.findViewById(R.id.is_artists_and_album)
    }
}

class NeteaseFooterViewHolder(view: View) : RecyclerView.ViewHolder(view){

}