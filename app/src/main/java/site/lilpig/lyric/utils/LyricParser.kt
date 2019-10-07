package site.lilpig.lyric.utils

import java.lang.Exception
import java.util.regex.Pattern


interface SingleSentence{
    val playTime: Int
    val sentence: String
}
object LyricParser{
    /**
     * This function parse the lyric source file to the list of SingleSentence(Ignore blank and no use sentence)
     */
    fun parse(source: String): MutableList<SingleSentence>{
        val sentences: MutableList<SingleSentence> = ArrayList()
        source.split("\n").forEach {
            val sentenceP = parseSingleSentence(it)
            if (sentenceP != null)
                sentences.addAll(sentenceP)
        }
        sentences.sortBy { it.playTime }
        return sentences
    }

    private fun parseSingleSentence(line: String): MutableList<SingleSentence>?{
        var sentences: MutableList<SingleSentence>? = null
        val pattern = Pattern.compile("^\\[(\\d+):(\\d+)(.(\\d+))*\\](.*?)\$")
        val matcher = pattern.matcher(line)
        if (matcher.find()){
            val time = matcher.group(1).toInt() * 60 * 1000 + matcher.group(2).toInt() * 1000 + try {
                matcher.group(4).toInt()
            }catch (e: Exception){0}
            var sentenceStr = matcher.group(5)
            var resultP = parseSingleSentence(sentenceStr)
            if (resultP == null || resultP.size == 0){
                sentences = ArrayList()
                sentences?.add(object : SingleSentence{
                    override val playTime = time
                    override val sentence = sentenceStr
                    override fun toString() = "${playTime} : ${sentence}"
                })
            }else{
                sentences = resultP
                sentences.add(object : SingleSentence{
                    override val playTime = time
                    override val sentence = resultP[0].sentence
                    override fun toString() = "${playTime} : ${sentence}"
                })
            }
            return sentences
        }else{
            return null
        }
    }
}