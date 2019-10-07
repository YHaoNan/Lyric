package site.lilpig.lyric.converter

import site.lilpig.lyric.bean.Sentence
import site.lilpig.lyric.utils.SingleSentence

class SentenceConverter {
    fun convert(lrc: SingleSentence, trc: SingleSentence?): Sentence{
        if (trc != null && lrc.playTime == trc.playTime)
            return Sentence(lrc.playTime,lrc.sentence,trc.sentence)
        else
            return Sentence(lrc.playTime,lrc.sentence,null)
    }
}