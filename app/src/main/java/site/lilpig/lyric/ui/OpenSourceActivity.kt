package site.lilpig.lyric.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_opensource.*
import site.lilpig.lyric.R
import site.lilpig.lyric.utils.IntentUtils

class OpenSourceActivity : AppCompatActivity() {
    val projectNames = arrayOf("Glide","UCrop","OkHttp","OKIO","kimcy929/FileChooser")
    val projectDescs = arrayOf("An image loading and caching library for Android focused on smooth scrolling",
                                            "Image Cropping Library for Android",
                                            "Square’s meticulous HTTP client for Java and Kotlin",
                                            "A modern I/O library for Android, Kotlin, and Java",
                                            "The Android lib supports choose the path of a folder or file easier")
    val projectLinks = arrayOf("https://github.com/bumptech/glide","https://github.com/Yalantis/uCrop",
                                            "https://github.com/square/okhttp","https://github.com/square/okio","https://github.com/kimcy929/FileChooser")
    val openSourceProjectsList:MutableList<MutableMap<String,String>> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opensource)
        initData()
        val adapter = SimpleAdapter(this,openSourceProjectsList,android.R.layout.simple_list_item_2,
            arrayOf("name","desc"), intArrayOf(android.R.id.text1,android.R.id.text2))
        aos_list.adapter = adapter
        aos_list.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                IntentUtils.openInBrowser(this@OpenSourceActivity,openSourceProjectsList.get(p2).get("url")!!)
            }
        })
    }

    private fun initData() {
        for (i in 0..projectNames.size - 1){
            val map = HashMap<String,String>()
            map.put("name",projectNames[i])
            map.put("desc",projectDescs[i])
            map.put("url",projectLinks[i])
            openSourceProjectsList.add(map)
        }
    }
}