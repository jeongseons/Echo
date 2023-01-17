package com.example.echo.myPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R

class StorageBoxAdapter (val context: Context, val storageboxList:ArrayList<MemberVO>):
RecyclerView.Adapter<StorageBoxAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val btnBack: Button
        val tvAllch: TextView
        val tvAllde: TextView

        init {
            btnBack = itemView.findViewById(R.id.btnBack)
            tvAllch = itemView.findViewById(R.id.tvAllch)
            tvAllde = itemView.findViewById(R.id.tvAllde)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.storagebox_list, null)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }

    override fun getItemCount(): Int {
      return storageboxList.size
    }
}



