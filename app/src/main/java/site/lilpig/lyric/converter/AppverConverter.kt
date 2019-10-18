package site.lilpig.lyric.converter

import org.json.JSONObject
import site.lilpig.lyric.bean.Appver

class AppverConverter : Converter<Appver?>{
    override fun convert(json: String?): Appver? {
        if (json!=null){
            val jo = JSONObject(json)
            val ver = Appver()
            ver.id = jo.getInt("id")
            ver.info = jo.getString("info")
            ver.isforce = jo.getInt("isforce")
            ver.ver = jo.getString("ver")
            ver.url = jo.getString("url")
            return ver
        }
        return null
    }

}