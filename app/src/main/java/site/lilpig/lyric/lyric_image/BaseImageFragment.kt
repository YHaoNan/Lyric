package site.lilpig.lyric.lyric_image

import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.frag_li_top_image.view.*
import site.lilpig.lyric.R
import site.lilpig.lyric.app
import site.lilpig.lyric.bean.Song
import site.lilpig.lyric.utils.SingleSentence


abstract class BaseImageFragment: Fragment() {
    abstract val thumbnail: Int
    abstract val name: String
    abstract val layout: Int
    lateinit var lyrics: List<String>
    lateinit var song: Song
    var image:ImageView? = null
    lateinit var lyricsView:TextView
    lateinit var albumsAndArtists: TextView
    lateinit var mark: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout,container,false)
        image = view.findViewById(R.id.frag_li_image)
        mark = view.findViewById(R.id.frag_li_mark)
        lyricsView = view.findViewById(R.id.frag_li_lyric)
        albumsAndArtists = view.findViewById(R.id.frag_li_artist_and_song)
        mark.visibility = if (app?.isMarkClosed()!!){
            View.GONE
        }else View.VISIBLE
        onCreateView(view)
        return view
    }

    abstract fun onCreateView(root: View)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindDataToView()
    }
    abstract fun bindDataToView()

    abstract fun onImageChanged(uri: Uri)
    fun setData(lyrics: List<String>,song: Song){
        this.lyrics = lyrics
        this.song = song
    }
}