package site.lilpig.lyric.bean

data class Sentence(val time: Int,val sourceLyric: String,val translateLyric: String?)

data class Lyric(val author: String?, val translateAuthor: String?, val sentences: List<Sentence>)