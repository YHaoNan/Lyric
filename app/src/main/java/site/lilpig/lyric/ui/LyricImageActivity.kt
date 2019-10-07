package site.lilpig.lyric.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.Unbinder
import kotlinx.android.synthetic.main.activity_lyric_image.*
import site.lilpig.lyric.R
import site.lilpig.lyric.app
import site.lilpig.lyric.bean.Song
import site.lilpig.lyric.lyric_image.BaseImageFragment
import site.lilpig.lyric.lyric_image.BlurImageFragment
import site.lilpig.lyric.lyric_image.TextImageFragment
import site.lilpig.lyric.lyric_image.TopImageFragment
import site.lilpig.lyric.utils.generateImage
import site.lilpig.lyric.utils.saveToGallery
import site.lilpig.lyric.utils.toast
import java.util.*
import com.yalantis.ucrop.UCrop
import java.io.File




class LyricImageActivity : AppCompatActivity(){
    private var unbinder: Unbinder? = null
    private val imageFragments: Array<BaseImageFragment> = arrayOf(
        TopImageFragment(),
        BlurImageFragment(),
        TextImageFragment()
    )
    private val convertBarData: MutableList<DialogItem> = mutableListOf()
    private var currentFragment: BaseImageFragment = imageFragments[0]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric_image)
        unbinder = ButterKnife.bind(this)
        initData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.lyric_image_menu,menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            val uri = data?.getData()
            val destinationUri = Uri.fromFile(File(externalCacheDir, "uCrop.jpg"))
            if(uri!=null)
                UCrop.of(uri, destinationUri)
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(800, 800)
                    .start(this);
        }else if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP){
            val resultUri = UCrop.getOutput(data!!)
            if(resultUri!=null)
                imageFragments.forEach {
                    Log.i("LyricImageActivity",it.name)
                    it.onImageChanged(resultUri!!)
                }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.lim_change_image -> {
                val intent_gallery = Intent()
                intent_gallery.type = "image/*"
                intent_gallery.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent_gallery, 1)
            }
            R.id.lim_save -> {
                val path = ali_image_container.generateImage().saveToGallery(this,"imagesOut",Date().time.toString()+".jpg")
                toast("保存到${path}")
                val imageUri = Uri.parse(path);
                val shareIntent = Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "分享歌词图片"));
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initData() {
        setSupportActionBar(ali_toolbar)
        val lyrics:List<String> = app?.popAValue("lyrics") as List<String>
        val song: Song = app?.popAValue("song") as Song
        imageFragments.forEach {that->
            convertBarData.add(
                DialogItem(
                that.thumbnail,that.name, View.OnClickListener {
                        changeFragment(that,lyrics,song)
                    }
                )
            )
        }
        ali_changebar.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        ali_changebar.adapter = BPDAdapter(this,convertBarData)
        changeFragment(imageFragments[0],lyrics,song)
    }

    fun changeFragment(fragment: BaseImageFragment,lyrics: List<String>,song: Song){
        val transaction = this@LyricImageActivity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ali_image_container,fragment)
        currentFragment = fragment
        transaction.commit()
        currentFragment.setData(lyrics,song)
    }
    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
    }
}

