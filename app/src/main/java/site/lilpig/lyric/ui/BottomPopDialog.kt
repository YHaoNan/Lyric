package site.lilpig.lyric.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.dialog_bottom_pop.*
import site.lilpig.lyric.R
import android.view.WindowManager





open class DialogItem(val icon:Int,val text:String,val click:View.OnClickListener)

open class BPDViewHolder(val view:View):RecyclerView.ViewHolder(view){
    val icon: ImageView
    val text: TextView
    init {
        icon = view.findViewById(R.id.ibp_icon)
        text = view.findViewById(R.id.ibp_title)
    }

}
open class BPDAdapter(val context: Context,val items: List<DialogItem>) : RecyclerView.Adapter<BPDViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BPDViewHolder {
        return BPDViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_bottom_pop,null)
        )
    }


    override fun getItemCount()=items.size

    override fun onBindViewHolder(holder: BPDViewHolder, position: Int) {
        holder.icon.setImageResource(items[position].icon)
        holder.text.text = items[position].text
        holder.view.setOnClickListener{
            items[position].click.onClick(holder.view)

        }
    }
}
class BottomPopDialog(context: Context,val items:List<DialogItem>) : Dialog(context,R.style.BottomDialogTheme){
    init {
        window?.setGravity(Gravity.BOTTOM)
        window?.setWindowAnimations(R.style.bottom_pop_anim)
        window?.getDecorView()?.setPadding(0, 0, 0, 0)
        val layoutParams = window?.getAttributes()
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.setAttributes(layoutParams)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_bottom_pop)
        dbp_recycler.layoutManager = GridLayoutManager(context,4)
        dbp_recycler.adapter = BPDAdapter(context,items)
    }


}