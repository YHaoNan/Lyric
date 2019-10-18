package site.lilpig.lyric.converter

import site.lilpig.lyric.bean.Lyric
import site.lilpig.lyric.bean.Sentence

class PureTextLyricConverter : Converter<MutableList<Sentence>>{
    override fun convert(json: String?): MutableList<Sentence> {
        val sentence = mutableListOf<Sentence>()
        json?.split("\n")?.forEach {
            sentence.add(Sentence(-1,it,null))
        }
        return sentence
    }

}