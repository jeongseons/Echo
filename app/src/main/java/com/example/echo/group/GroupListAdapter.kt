package com.example.echo.group

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R

class GroupListAdapter(val context: Context, val grouplist: ArrayList<GroupVO>):
RecyclerView.Adapter<GroupListAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGroupTitle: TextView
        val tvGroupPer: TextView
        val imgGroupPro: ImageView
        val imgKingCk: ImageView

        init {
            tvGroupTitle = itemView.findViewById(R.id.tvGroupTitle)
            tvGroupPer = itemView.findViewById(R.id.tvGroupPer)
            imgGroupPro = itemView.findViewById(R.id.imgGroupPro)
            imgKingCk = itemView.findViewById(R.id.imgKingCk)
            itemView.setOnClickListener{
                val intent = Intent(context, GroupActivity :: class.java)
                intent.putExtra("title", grouplist[adapterPosition].title.toString())
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.group_list, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGroupTitle.setText(grouplist[position].title)
        holder.tvGroupPer.setText(grouplist[position].person)
        holder.imgGroupPro.setImageResource(grouplist[position].profile)//
        if(!grouplist[position].king){
            holder.imgKingCk.isVisible = false
        }
    }

    override fun getItemCount(): Int {
        return grouplist.size
    }

}