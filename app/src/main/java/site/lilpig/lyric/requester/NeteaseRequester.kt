package site.lilpig.lyric.requester

import android.os.Handler
import okhttp3.*
import java.io.IOException

/**
 * 因为网易云的API是经过JS加密的，所以提供一个NeteaseRequester复用加密过程
 */

private val ENCSECKEY = "3f49c43f35a07e1c0189daf3a23ed82d3c54544c5eefc6e42efb451080cf59e91d3664340560455d1ddc1bd3c7f229ce2495361f23363c7be4aa9997dc143e3cb609a1813438adf3301213ad81fc2c2b218d423b6ab4dc30ac201b7d3e14e20aac6de00b265f8008f32137fded7cda8fc4e184194c311a8fe98b36d5f4210781"


class NeteaseRequester {
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
        encrypt(RequestJsonFactory.search("病态"))
        var client = OkHttpClient()
        var formdata = FormBody.Builder()
            .add("params", encrypt(reqJson))
            .add("encSecKey", ENCSECKEY).build()
        var request = Request.Builder()
            .url(url)
            .post(formdata)
            .addHeader("referer","https://music.163.com/search/")
            .addHeader("user-agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
            .build()
        client.newCall(request).enqueue(object :Callback{
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