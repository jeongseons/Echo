package com.example.echo.group

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R

class JoinGroupAdapter (val context: Context, var grouplist: ArrayList<GroupVO>):
    RecyclerView.Adapter<JoinGroupAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGroupTitle: TextView
        val tvGroupPer: TextView
        val imgGroupPro: ImageView
        val imgKingCk: ImageView

        init {
//            Log.d("qwe", grouplist.toString())
            tvGroupTitle = itemView.findViewById(R.id.tvGroupTitle)
            tvGroupPer = itemView.findViewById(R.id.tvGroupPer)
            imgGroupPro = itemView.findViewById(R.id.imgGroupPro)
            imgKingCk = itemView.findViewById(R.id.imgKingCk)
            itemView.setOnClickListener{
                val intent = Intent(context, JoinGroupProfileActivity :: class.java)
                intent.putExtra("title", grouplist[adapterPosition].group_name)
                intent.putExtra("num", grouplist[adapterPosition].group_seq)
                intent.putExtra("auth",grouplist[adapterPosition].group_auth)

                var sharedPreferences = context.getSharedPreferences("group_seq",0)
                var editor = sharedPreferences.edit()

                editor.putInt("group_seq", grouplist[adapterPosition].group_seq)
                editor.commit()

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
        holder.imgKingCk.visibility=View.GONE
        holder.tvGroupTitle.setText(grouplist[position].group_name)
        holder.tvGroupPer.setText("(${grouplist[position].group_current}/${grouplist[position].user_max})")
        holder.imgGroupPro.setImageResource(R.drawable.p1)//

    }

    override fun getItemCount(): Int {
        return grouplist.size
    }

}