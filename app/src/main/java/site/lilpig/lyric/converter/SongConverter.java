package site.lilpig.lyric.converter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import site.lilpig.lyric.bean.Song;

public class SongConverter implements Converter<Song>{
    @Override
    public Song convert(String json) throws JSONException {
        JSONObject songJson = new JSONObject(json);
        String id = songJson.getString("id");
        String name = songJson.getString("name");
        JSONArray artistsJson = songJson.getJSONArray("ar");
        String artists[] = new String[artistsJson.length()];
        for (int i=0;i<artistsJson.length();i++)
            artists[i] = artistsJson.getJSONObject(i).getString("name");
        JSONObject albumJson = songJson.getJSONObject("al");
        String album = albumJson.getString("name");
        String albumCovor = albumJson.getString("picUrl");
        Song song = new Song(id,name,artists,album,albumCovor);
        return song;
    }
}
