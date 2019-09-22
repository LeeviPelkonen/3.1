package com.example.lab6

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.recycler_layout.view.*

class MainAdapter(private val itemList: List<ScanResult>): RecyclerView.Adapter<MainAdapter.CustomViewHolder>() {

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_layout, parent, false)
        return CustomViewHolder(cellForRow)
    }

    //numberOfItems
    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int){
        holder.view.name?.text = itemList[position].device.address
        holder.view.address?.text = itemList[position].isConnectable.toString()

        holder.itemView.setOnClickListener {
            /*
            Log.d("qwerty",position.toString())
            Log.d("qwerty",itemList[position].device.bluetoothClass.toString())
            Log.d("qwerty",itemList[position].device.bondState.toString())
            Log.d("qwerty",itemList[position].device.toString())
            */
        }
    }
}