package com.example.tajo_frontend.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tajo_frontend.Data.Bell
import com.example.tajo_frontend.R

class BellAdapter(private val items: ArrayList<Bell>) : RecyclerView.Adapter<BellAdapter.ViewHolder>() {
    private lateinit var viewCallback: (Bell) -> Unit
    lateinit var btnDoneCallback: (Int, Bell) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bell, parent, false)
        val viewHolder =
            ViewHolder(view)
        viewHolder.callback = btnDoneCallback
        view.setOnClickListener {
            viewCallback(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].let {
            holder.bind(it)

        }
    }
    fun setItemClickListener(callback: (Bell) -> Unit) {
        this.viewCallback = callback
    }
    fun setBtnDoneClickListener(callback: (Int, Bell) -> Unit){
        this.btnDoneCallback = callback
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val tvStn_id = itemView.findViewById<TextView>(R.id.tvStn_id)
        private val tvCount = itemView.findViewById<TextView>(R.id.tvCount)
        private val btnDone = itemView.findViewById<com.ornach.nobobutton.NoboButton>(
            R.id.btnDone
        )
        lateinit var callback: (Int, Bell) -> Unit
        fun bind(bell: Bell) {
            with(bell) {

                tvStn_id.text = bell.stn_id+" 역"
                tvCount.text = bell.count.toString()+ " 명"
                btnDone.setOnClickListener{
                    callback(adapterPosition ,bell)
                }

            }
        }
    }


}