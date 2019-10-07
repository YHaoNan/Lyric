package site.lilpig.lyric.utils

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import android.renderscript.Element.U8_4
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript


class BlurTransformation(val radius: Int,val context: Context) : BitmapTransformation(){

    val renderScript: RenderScript
    init {
        renderScript = RenderScript.create(context)
    }
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val input = Allocation.createFromBitmap(renderScript, toTransform)
        val output = Allocation.createTyped(renderScript, input.type)
        val scriptIntrinsicBlur =
            ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        scriptIntrinsicBlur.setRadius(radius.toFloat())
        scriptIntrinsicBlur.setInput(input)
        scriptIntrinsicBlur.forEach(output)
        output.copyTo(toTransform)
        return toTransform
    }

}