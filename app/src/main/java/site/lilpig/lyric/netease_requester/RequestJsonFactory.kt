package site.lilpig.lyric.netease_requester

object RequestJsonFactory{
    fun search(keyword: String) = "{\"hlpretag\":\"<span class=\\\"s-fc7\\\">\",\"hlposttag\":\"</span>\",\"s\":\"${keyword}\",\"type\":\"1\",\"offset\":\"0\",\"total\":\"true\",\"limit\":\"30\",\"csrf_token\":\"\"}"
    fun getLyric(id: String) = "{\"id\":\"${id}\",\"lv\":-1,\"tv\":-1,\"csrf_token\":\"\"}"
}