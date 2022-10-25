package com.ibrahimgok.stt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_row.view.*


class RecyclerAdapter(val urunListesi: ArrayList<String>) : RecyclerView.Adapter<RecyclerAdapter.urunler>() {

    class urunler(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): urunler {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
        return urunler(itemView)
    }

    override fun getItemCount(): Int {

        return urunListesi.size

    }

    override fun onBindViewHolder(holder: urunler, position: Int) {
        holder.itemView.textView.text = urunListesi.get(position)

    }
}
