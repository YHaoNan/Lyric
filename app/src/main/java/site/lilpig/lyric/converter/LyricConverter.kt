package site.lilpig.lyric.converter

import org.json.JSONException
import org.json.JSONObject
import site.lilpig.lyric.bean.Lyric
import site.lilpig.lyric.bean.Sentence
import site.lilpig.lyric.utils.LyricParser
import site.lilpig.lyric.utils.safeGet

class LyricConverter : Converter<Lyric?> {
    val sentenceConverter = SentenceConverter()
    override fun convert(json: String?): Lyric? {
        val jo = JSONObject(json!!)
        val lrcSource: String? = try {
            jo.getJSONObject("lrc").getString("lyric")
        }catch (e: JSONException){
            null
        }
        val trcSource: String? = try {
            jo.getJSONObject("tlyric").getString("lyric")
        }catch (e: JSONException){
            null
        }

        if (lrcSource == null)return null

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

        var lrcs = LyricParser.parse(lrcSource)
        var trcs = if (trcSource!=null)LyricParser.parse(trcSource) else null

        val sentences = mutableListOf<Sentence>()
        var trcLines = 0
        lrcs.forEach{
            val sentence = sentenceConverter.convert(it,trcs.safeGet(trcLines))
            sentences.add(sentence)
            if (sentence.translateLyric != null)trcLines++
        }
        return Lyric(luser,tuser,sentences)
    }

}