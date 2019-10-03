package site.lilpig.lyric.converter

import org.json.JSONObject
import site.lilpig.lyric.bean.Song

class SongListConverter : Converter<List<Song>>{
    override fun convert(json: String?): List<Song> {
        val songsJson = JSONObject(json).getJSONObject("result").getJSONArray("songs")
        val songs = List<Song>(songsJson.length(),{
            SongConverter().convert(songsJson.getString(it))
        })
        return songs
    }
}