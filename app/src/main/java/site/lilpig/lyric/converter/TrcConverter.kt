package site.lilpig.lyric.converter

import org.json.JSONException
import org.json.JSONObject

class TrcConverter : Converter<String?>{
    override fun convert(json: String?): String? {
        val jo = JSONObject(json!!)
        return try {
            jo.getJSONObject("tlyric").getString("lyric")
        }catch (e: JSONException){
            null
        }
    }

}