package site.lilpig.lyric.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.Unbinder
import site.lilpig.lyric.adapter.SongListAdapter
import site.lilpig.lyric.converter.SongListConverter
import site.lilpig.lyric.netease_requester.NeteaseRequester
import site.lilpig.lyric.netease_requester.REQUEST_SEARCH
import site.lilpig.lyric.netease_requester.RequestCallback
import site.lilpig.lyric.netease_requester.RequestJsonFactory
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_search.*
import site.lilpig.lyric.R
import site.lilpig.lyric.adapter.HistoryAdapter
import site.lilpig.lyric.adapter.OnItemClick
import site.lilpig.lyric.utils.HistoryUtil


class SearchActivity : AppCompatActivity(){
    lateinit var neteaseRequester: NeteaseRequester
    lateinit var songListAdapter: SongListAdapter
    lateinit var layoutManagerOfSongList: RecyclerView.LayoutManager
    lateinit var historyAdapter: HistoryAdapter
    var unbinder: Unbinder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        unbinder = ButterKnife.bind(this)
        neteaseRequester = NeteaseRequester()
        initSomethingAboutView()
        bindEvent()
    }

    @SuppressLint("WrongConstant")
    private fun initSomethingAboutView() {
        layoutManagerOfSongList = LinearLayoutManager(this)
        as_search_result.layoutManager = layoutManagerOfSongList
        val historyList = HistoryUtil.getHistorys(this)
        historyAdapter = HistoryAdapter(this,historyList!!, object : OnItemClick {
            override fun onclick(i: Int) {
                as_search_bar.setText(historyList[i])
                search(historyList[i])
            }
        })
        as_search_history.layoutManager = LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
        as_search_history.adapter = historyAdapter

    }

    private fun bindEvent() {
        as_search_bar.setOnEditorActionListener { textView, i, keyEvent ->
            val text = textView.text.toString()
            if(!text.isNullOrEmpty()) {
                search(text)
                val view = window.peekDecorView()
                if (view != null) {
                    val inputmanger =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputmanger.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            false
        }
        as_back.setOnClickListener({
            finish()
        })
        as_clear.setOnClickListener({
            as_search_bar.setText("")
        })
    }

    private fun search(keyword: String){
        as_search_history.visibility = View.GONE
        HistoryUtil.putHistorys(this,keyword)
        neteaseRequester.sendRequest(REQUEST_SEARCH,RequestJsonFactory.search(keyword),
            RequestCallback {
                if (!it.isNullOrEmpty()){
                    val songList = SongListConverter().convert(it)
                    songListAdapter = SongListAdapter(keyword,songList)
                    as_search_result.adapter = songListAdapter
                }
            })
    }

}