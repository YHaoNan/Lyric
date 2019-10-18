package site.lilpig.lyric.api_requester

import android.util.Base64
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object EncryptUtil{
    val PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAojNNBVNOFLV4602RK5iYqAK95+LMa6Gz\n" +
            "tkQkjWrm+i3acG85XJOj5+kbkTRPaf7X0UEBoKaKoqBr1oLhChdRigrYJhiAkD9S5YBTj1v5Aa9N\n" +
            "ofPVD8kDW/2mi8G6vfX+U57dI4xKjX22t0yxO4+dqpjVUuaQVGTAXmVDXGKQM8G4g8N5VZ26UbL6\n" +
            "h8fFZr1vDl4Oz414S/9vtoQPcRV2WdGltYNZYroDcYkZyVSqG4cCkxCCxZmMmFe1gAqjqOkUVlYJ\n" +
            "qxcUZc7Oy3gOMcKIJDh25sHfXzu9XrFdug4G1rAYSBXehBaMt4oZKWLL8H6sx391FRiguTn36Z9L\n" +
            "hlkqowIDAQAB"
    fun encryptParams(key: String,content: String):String?{
        var skeySpec = SecretKeySpec(key.toByteArray(),"AES")
        var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE,skeySpec, IvParameterSpec("0102030405060708".toByteArray()))
        return Base64.encodeToString(cipher.doFinal(content.toByteArray()),Base64.NO_WRAP)
    }
    fun encryptKey(key: String): String?{
        val bytes = key.toByteArray()
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher?.init(Cipher.ENCRYPT_MODE,KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(Base64.decode(PUBLIC_KEY,Base64.DEFAULT))))
        return Base64.encodeToString(cipher.doFinal(bytes),Base64.NO_WRAP)
    }
}