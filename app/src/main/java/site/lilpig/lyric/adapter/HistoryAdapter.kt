package site.lilpig.lyric.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.lilpig.lyric.R

open interface OnItemClick{
    fun onclick(i: Int)
}
class HistoryAdapter(val context: Context,val list: MutableList<String>,val onItemClickListener: OnItemClick) : RecyclerView.Adapter<ArrayViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ArrayViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history,null))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ArrayViewHolder, position: Int) {
        holder.text.text = list[position]
        holder.view.setOnClickListener({
            onItemClickListener.onclick(position)
        })
    }

}
class ArrayViewHolder(val view: View): RecyclerView.ViewHolder(view){
    var text: TextView
    init {
        this.text = view.findViewById(R.id.ih_history)
    }

}