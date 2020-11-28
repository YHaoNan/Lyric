package site.lilpig.lyric.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.*
import site.lilpig.lyric.R
import site.lilpig.lyric.adapter.HistoryAdapter
import site.lilpig.lyric.adapter.OnItemClick
import site.lilpig.lyric.adapter.SongListAdapter
import site.lilpig.lyric.converter.SongListConverter
import site.lilpig.lyric.netease_requester.NeteaseRequester
import site.lilpig.lyric.netease_requester.REQUEST_SEARCH
import site.lilpig.lyric.netease_requester.RequestCallback
import site.lilpig.lyric.netease_requester.RequestJsonFactory
import site.lilpig.lyric.utils.HistoryUtil
import site.lilpig.lyric.utils.toast


class SearchActivity : AppCompatActivity(){
    lateinit var neteaseRequester: NeteaseRequester
    lateinit var songListAdapter: SongListAdapter
    lateinit var layoutManagerOfSongList: RecyclerView.LayoutManager
    lateinit var historyAdapter: HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setActivityAnimation()
        neteaseRequester = NeteaseRequester()
        initSomethingAboutView()
        bindEvent()
    }

    private fun setActivityAnimation() {
        val slide: Transition =
            TransitionInflater.from(this).inflateTransition(R.transition.activity_slide_in)
        window.enterTransition = slide
    }

    @SuppressLint("WrongConstant")
    private fun initSomethingAboutView() {
        layoutManagerOfSongList = LinearLayoutManager(this)
        as_search_result.layoutManager = layoutManagerOfSongList
        as_search_history.layoutManager = LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
        genHistoryView()
    }

    private fun genHistoryView(){
        val historyList = HistoryUtil.getHistorys(this)
        historyAdapter = HistoryAdapter(this,historyList!!, object : OnItemClick {
            override fun onclick(i: Int) {
                as_search_bar.setText(historyList[i])
                search(historyList[i])
            }
        })
        as_search_history.adapter = historyAdapter
        as_history_clear_bar.visibility = if (historyList.size >= 1) View.VISIBLE else View.GONE
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
            as_search_result.visibility = View.GONE
            as_search_history.visibility = View.VISIBLE
            as_history_clear_bar.visibility = View.VISIBLE
        })
        as_delete.setOnClickListener {
            HistoryUtil.clearHistorys(this)
            genHistoryView()
        }
    }

    private fun search(keyword: String){
        HistoryUtil.putHistorys(this,keyword)
        neteaseRequester.sendRequest(REQUEST_SEARCH,RequestJsonFactory.search(keyword),
            RequestCallback {
                if (!it.isNullOrEmpty()){
                    as_search_result.visibility=View.VISIBLE
                    as_search_history.visibility = View.GONE
                    as_history_clear_bar.visibility = View.GONE
                    val songList = SongListConverter().convert(it)
                    songListAdapter = SongListAdapter(keyword,songList)
                    as_search_result.adapter = songListAdapter
                }else
                    toast("没有找到歌曲")
            })
    }

}