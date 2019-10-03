package site.lilpig.lyric.requester

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private val KEY1 = "e5dHkZkm5iLKF89m"
private val KEY2 = "0CoJUm6Qyw8W8jud"

private fun aesEn(key: String,message: String):String {
    var skeySpec = SecretKeySpec(key.toByteArray(),"AES")
    var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE,skeySpec,IvParameterSpec("0102030405060708".toByteArray()))
    return Base64.encodeToString(cipher.doFinal(message.toByteArray()),Base64.NO_WRAP)
}


fun encrypt(source: String) = aesEn(KEY1, aesEn(KEY2,source))
