package site.lilpig.lyric.converter

import org.json.JSONObject
import site.lilpig.lyric.bean.EverydayLyric

class EverydayLyricConverter : Converter<EverydayLyric>{
    override fun convert(json: String?) = with(JSONObject(json)){
            EverydayLyric(getInt("id"),getString("neteaseid"),getString("lyric"),getString("song"),
                getString("artist"),getString("upuser"),getString("covor"),getLong("date"))
        }

}