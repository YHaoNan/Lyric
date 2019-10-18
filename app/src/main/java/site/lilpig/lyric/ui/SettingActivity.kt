package site.lilpig.lyric.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import kotlinx.android.synthetic.main.activity_setting.*
import site.lilpig.lyric.R
import site.lilpig.lyric.app
import site.lilpig.lyric.utils.toast
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import site.lilpig.lyric.BuildConfig
import java.io.File


class SettingActivity : AppCompatActivity(){
    var unbinder: Unbinder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        unbinder = ButterKnife.bind(this)

        ase_open_lyric.setOnClickListener{
            toast("Method is not implemented!")
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.addCategory(Intent.CATEGORY_DEFAULT)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider",File(Environment.getExternalStorageDirectory().absolutePath+"/mj_lyric/lyrics"))
//            intent.setDataAndType(uri, "*/*")
//            try {
//                startActivity(Intent.createChooser(intent,"选择浏览工具"));
//            } catch (e: ActivityNotFoundException) {
//                e.printStackTrace()
//            }
//
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

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
    }
}