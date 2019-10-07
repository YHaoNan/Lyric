package site.lilpig.lyric.lyric_image

import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import site.lilpig.lyric.R
import site.lilpig.lyric.utils.BlurTransformation
import site.lilpig.lyric.utils.join

class BlurImageFragment() : BaseImageFragment(){
    private var localImage: Uri? = null
    override fun onImageChanged(uri: Uri) {
        localImage = uri
        setImage()
    }

    override val layout: Int = R.layout.frag_li_blur
    override val thumbnail: Int = R.drawable.li_blur
    override val name: String = "模糊"


    override fun onCreateView(root: View) {

    }

    override fun bindDataToView() {
        setImage()
        this.lyricsView.text = lyrics.join("\n")
        albumsAndArtists.text = song.name + " · " + song.artists.join(" / ")
    }

    private fun setImage() {
        if (image!=null && context!=null)
            if(localImage!=null)
                Glide.with(context!!).load(localImage)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(25,context!!)))
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(image!!)
            else
                Glide.with(context!!).load(song.albumCovor+"?param=240y240")
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(25,context!!)))
                    .into(image!!)
    }

}