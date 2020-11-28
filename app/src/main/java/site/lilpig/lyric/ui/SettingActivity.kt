package site.lilpig.lyric.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kimcy929.simple_file_chooser.FileChooserActivity
import kotlinx.android.synthetic.main.activity_setting.*
import site.lilpig.lyric.R
import site.lilpig.lyric.app
import site.lilpig.lyric.utils.toast


class SettingActivity : AppCompatActivity(){
    val DIR_IMG = 0
    val DIR_LRC = 1
    fun chooseDir(dirType: Int){
        val initPath = if (dirType == DIR_IMG) ase_cur_img_dir.text.toString() else ase_cur_lrc_dir.text.toString()
        val directoryIntent = Intent(this, FileChooserActivity::class.java)
        directoryIntent.putExtra(FileChooserActivity.INIT_DIRECTORY_EXTRA,initPath)
        directoryIntent.putExtra(FileChooserActivity.GET_ONLY_DIRECTORY_PATH_FILE_EXTRA, false)
        startActivityForResult(directoryIntent, dirType)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        ase_cur_lrc_dir.text = app?.getLrcStorePath()
        ase_cur_img_dir.text = app?.getImgStorePath()

        ase_lyric_dir.setOnClickListener{
            chooseDir(DIR_LRC)
        }
        ase_img_dir.setOnClickListener{
            chooseDir(DIR_IMG)
        }

        ase_close_mark.setOnClickListener{
            if (app?.isMarkClosed()!!){
                app?.openMark()
                toast("已开启水印")
            }else{
                app?.closeMark()
                toast("已关闭水印")
            }
        }

        ase_opensource.setOnClickListener{
            startActivity(Intent(this,OpenSourceActivity().javaClass))
        }

        ase_about.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("关于")
            dialog.setMessage("某句 Ver1.1\n" +
                    "By LILPIG")
            dialog.setPositiveButton("知道了", DialogInterface.OnClickListener{iface,i->
                iface.dismiss()
            })
            dialog.show()
        }

        ase_grant.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("捐助")
            val imageView = ImageView(this)
            imageView.setImageResource(R.drawable.mmgrant)
            dialog.setView(imageView)
            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && (requestCode == DIR_IMG || requestCode == DIR_LRC)){
            val path = data.getStringExtra(FileChooserActivity.RESULT_DIRECTORY_EXTRA)
            if (path==null){
                toast("出了点错误")
                return
            }
            Log.i("PATH",path)
            if (requestCode == DIR_IMG){
                app?.setImgStorePath(path)
                ase_cur_img_dir.text = path
            }else if (requestCode == DIR_LRC){
                app?.setLrcStorePath(path)
                ase_cur_lrc_dir.text = path
            }
        }
    }
}