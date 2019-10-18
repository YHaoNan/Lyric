package site.lilpig.lyric.api_requester

import android.os.Handler
import okhttp3.*
import site.lilpig.lyric.api_requester.EncryptUtil.encryptKey
import site.lilpig.lyric.api_requester.EncryptUtil.encryptParams
import site.lilpig.lyric.netease_requester.RequestCallback
import site.lilpig.lyric.utils.join

import java.io.IOException
import java.util.*

private val CHARS = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,.!@#$%&^*";
class LyricAPIRequester{
    interface Response{
        val data: String
        val callback: RequestCallback
    }

    private val handler = Handler{
        var resp = (it.obj as Response)
        resp.callback.done(resp.data)
        false
    }


    fun sendRequest(url: String,reqJson: String,callback: RequestCallback){
        val key = Array<String>(16,{
            CHARS[Random().nextInt(CHARS.length)].toString()
        }).join("")
        var client = OkHttpClient()
        var formdata = FormBody.Builder()
            .add("params", encryptParams(key,reqJson))
            .add("key", encryptKey(key)).build()
        var request = Request.Builder()
            .url(url)
            .post(formdata)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(p0: Call, p1: okhttp3.Response) {
                sendMessage(object : Response{
                    override val data: String = p1.body()?.string() ?: ""
                    override val callback: RequestCallback = callback
                })
            }

            override fun onFailure(p0: Call, p1: IOException) {
                sendMessage(object :Response{
                    override val data: String = p1.message?:""
                    override val callback: RequestCallback = callback
                })
            }

        })
    }
    private fun sendMessage(response:Response){
        var msg = handler.obtainMessage()
        msg.obj = response
        handler.sendMessage(msg)
    }
}