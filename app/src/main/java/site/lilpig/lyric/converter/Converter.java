package site.lilpig.lyric.converter;

import org.json.JSONException;

public interface Converter<T> {
    T convert(String json) throws JSONException;
}
