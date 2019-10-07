package site.lilpig.lyric.lyric_image

import android.net.Uri
import android.view.View
import site.lilpig.lyric.R
import site.lilpig.lyric.utils.join
import site.lilpig.lyric.utils.toast

class TextImageFragment : BaseImageFragment(){
    override fun onImageChanged(uri: Uri) {

    }

    override val thumbnail: Int = R.drawable.li_text
    override val name: String = "纯文字"
    override val layout: Int = R.layout.frag_li_text

    override fun onCreateView(root: View) {
    }

    override fun bindDataToView() {
        lyricsView.text = lyrics.join("\n")
        albumsAndArtists.text = song.name+" · "+song.artists.join(" / ")
    }

}