package site.lilpig.lyric.lyric_image

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import site.lilpig.lyric.R
import site.lilpig.lyric.bean.Song
import site.lilpig.lyric.utils.BlurTransformation
import site.lilpig.lyric.utils.join

class TopImageGradientFragment : BaseImageFragment(){
    private var localImage: Uri? = null
    override fun onImageChanged(uri: Uri) {
        localImage = uri
        setImage()
    }

    @SuppressLint("LongLogTag")
    private fun setImage() {
        if (image!=null&& context!=null)
            if(localImage!=null) {
                Log.i("TopImageGradientFragment", (context == null).toString())
                Glide.with(context!!).load(localImage)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(image!!)
            }
            else
                Glide.with(context!!).load(song.albumCovor)
                    .into(image!!)
    }

    override val layout: Int = R.layout.frag_li_top_image_gradient
    override val thumbnail: Int = R.drawable.ic_top_image_grad
    override val name: String = "渐变"


    override fun onCreateView(root: View) {
    }


    override fun bindDataToView(){
        setImage()
        this.lyricsView.text = lyrics.join("\n")
        albumsAndArtists.text = "- "+song.name + " · " + song.artists.join(" / ")
    }
}