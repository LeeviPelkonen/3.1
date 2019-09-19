package com.example.arproject

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.popup_layout.view.*
import kotlinx.android.synthetic.main.recycler_layour.view.*

class MainAdapter(private val itemList: List<Furniture>,private val maker: ArMaker): RecyclerView.Adapter<MainAdapter.CustomViewHolder>() {

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_layour, parent, false)
        return CustomViewHolder(cellForRow)
    }

    //numberOfItems
    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int){
        holder.view.textView?.text = itemList[position].text
        holder.view.imageView2?.setImageResource(itemList[position].img)

        holder.itemView.setOnClickListener {
            //Log.d("qwerty",position.toString())
            if(position == 0){maker.doAr("drawer.sfb")}
            if(position == 1){maker.doAr("eb_lamp_01.sfb")}
            if(position == 2){maker.doAr("oben sandalye.sfb")}
            if(position == 3){maker.doAr("wooden stool.sfb")}
        }
    }
}