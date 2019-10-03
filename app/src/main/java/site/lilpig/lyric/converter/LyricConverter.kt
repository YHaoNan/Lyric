package site.lilpig.lyric.converter

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import site.lilpig.lyric.bean.Lyric
import site.lilpig.lyric.bean.Sentence
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.coroutines.coroutineContext

class LyricConverter : Converter<Lyric?> {
    override fun convert(json: String?): Lyric? {
        val jo = JSONObject(json!!)
        val lrcs: List<String>?= try {
            jo.getJSONObject("lrc").getString("lyric")?.split("\n")
        }catch (e: JSONException){
            null
        }
        val trcs: List<String>? = try {
            jo.getJSONObject("tlyric").getString("lyric")?.split("\n")
        }catch (e: JSONException){
            null
        }

        if (lrcs == null && trcs == null) return null
        val luser = try {
            jo.getJSONObject("lyricUser").getString("nickname")
        }catch (e: JSONException){
            null
        }
        val tuser = try {
            jo.getJSONObject("transUser").getString("nickname")
        }catch (e: JSONException){
            null
        }

        val sentences: MutableList<Sentence> = mutableListOf()
        var trcLine = 0
        lrcs?.forEach {
            var lrc: String
            var trc: String?
            var pattern = Pattern.compile("^\\[(\\d+):(\\d+)(.(\\d+))*\\](.*?)\$")
            var matcher = pattern.matcher(it)
            if (!matcher.find()){
                return@forEach
            }
            var trcTime: Int = 0
            lrc = matcher.group(5)
            fun getTrc(): String?{
                if (trcs == null)return null
                if(trcLine < trcs.size){
                    var matcher2 = pattern.matcher(trcs.get(trcLine))
                    if (matcher2.find()){
                        trcTime = matcher2.group(1).toInt() * 60 * 1000 + matcher2.group(2).toInt() * 1000 + try {
                            matcher.group(4).toInt()
                        }catch (e: Exception){
                            0
                        }
                        return matcher2.group(5)
                    }else {
                        trcLine++
                        return getTrc()
                    }
                }
                return null
            }

            trc = getTrc()
            var sentence: Sentence
            var time = matcher.group(1).toInt() * 60 * 1000 + matcher.group(2).toInt() * 1000 + try {
                matcher.group(4).toInt()
            }catch (e: Exception){
                0
            }
            Log.i("LyricConverter","Lrc: ${lrc}, Lrc Time: ${time}, Trc: ${trc}, Trc Time: ${trcTime}")
            if (time == trcTime){
                sentence = Sentence(time.toLong(),lrc,trc)
                trcLine++
            }else
                sentence = Sentence(time.toLong(),lrc,null)

            sentences.add(sentence)
        }
        return Lyric(luser,tuser,sentences)
    }

}