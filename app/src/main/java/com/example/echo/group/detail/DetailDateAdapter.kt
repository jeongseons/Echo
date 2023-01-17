package com.example.echo.group.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.group.GroupListAdapter
import com.example.echo.group.NewDateVO
import java.util.Date

class DetailDateAdapter(val context: Context, val dateList:ArrayList<NewDateVO>):RecyclerView.Adapter<DetailDateAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvGroupDateDate : TextView
        val tvGroupDateDetail : TextView
        val imgDropDate : ImageView
        val tvGroupDateText : TextView
        val tvGroupDateEdit : TextView
        val tvGroupDateDel : TextView
        init {
            tvGroupDateDate = itemView.findViewById(R.id.tvGroupDateDate)
            tvGroupDateDetail = itemView.findViewById(R.id.tvGroupDateDetail)
            imgDropDate = itemView.findViewById(R.id.imgDropDate)
            tvGroupDateText = itemView.findViewById(R.id.tvGroupDateText)
            tvGroupDateEdit = itemView.findViewById(R.id.tvGroupDateEdit)
            tvGroupDateDel = itemView.findViewById(R.id.tvGroupDateDel)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.group_date_list, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGroupDateDate.text = dateList[position].group_dt
        holder.tvGroupDateDetail.text = dateList[position].group_detail
        
        //상세정보 기본 접어두기
        holder.imgDropDate.tag = false
        holder.tvGroupDateText.visibility=View.GONE
        holder.tvGroupDateDetail.visibility=View.GONE
        holder.tvGroupDateEdit.visibility=View.GONE
        holder.tvGroupDateDel.visibility=View.GONE


        holder.imgDropDate.setOnClickListener{
            //펼쳐있을 시 접기
            if(holder.imgDropDate.tag == true){
                holder.imgDropDate.setImageResource(R.drawable.ic_drop_up)
                holder.tvGroupDateText.visibility=View.GONE
                holder.tvGroupDateDetail.visibility=View.GONE
                holder.tvGroupDateEdit.visibility=View.GONE
                holder.tvGroupDateDel.visibility=View.GONE
                holder.imgDropDate.tag = false
            }else{
                //접혀있을 시 펼치기
                holder.imgDropDate.setImageResource(R.drawable.ic_drop_down)
                holder.tvGroupDateText.visibility=View.VISIBLE
                holder.tvGroupDateDetail.visibility=View.VISIBLE
                holder.tvGroupDateEdit.visibility=View.VISIBLE
                holder.tvGroupDateDel.visibility=View.VISIBLE
                holder.imgDropDate.tag = true
            }
        }


    }

    override fun getItemCount(): Int {
       return dateList.size
    }
}