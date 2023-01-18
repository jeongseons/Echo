package com.example.echo.group.detail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.group.GroupListAdapter
import com.example.echo.group.NewDateVO
import java.util.Date

class DetailDateAdapter(val context: Context, val dateList:ArrayList<NewDateVO>, val selected:String,val auth:String):RecyclerView.Adapter<DetailDateAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvGroupDateDate : TextView
        val tvGroupDateDetail : TextView
        val tvGroupDateText : TextView
        val tvGroupDateEdit : TextView
        val tvGroupDateDel : TextView
        val textView21 : TextView
        init {
            tvGroupDateDate = itemView.findViewById(R.id.tvGroupDateDate)
            tvGroupDateDetail = itemView.findViewById(R.id.tvGroupDateDetail)
            tvGroupDateText = itemView.findViewById(R.id.tvGroupDateText)
            tvGroupDateEdit = itemView.findViewById(R.id.tvGroupDateEdit)
            tvGroupDateDel = itemView.findViewById(R.id.tvGroupDateDel)
            textView21 = itemView.findViewById(R.id.textView21)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.group_date_list, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val year = dateList[position].cal_dt.substring(0,4)
        val month = dateList[position].cal_dt.substring(5,7)
        val day = dateList[position].cal_dt.substring(8,10)
        val sumDate = "${year}-${month}-${day}"

        if(selected.equals(sumDate)){
            holder.tvGroupDateDate.text=dateList[position].cal_dt
            holder.tvGroupDateDetail.text=dateList[position].cal_content
            holder.tvGroupDateDate.visibility=View.VISIBLE
            holder.tvGroupDateText.visibility=View.VISIBLE
            holder.tvGroupDateDetail.visibility=View.VISIBLE
            if(auth.equals("y")){
                holder.tvGroupDateEdit.visibility=View.VISIBLE
                holder.tvGroupDateDel.visibility=View.VISIBLE
            }else{
                holder.tvGroupDateEdit.visibility=View.GONE
                holder.tvGroupDateDel.visibility=View.GONE
            }

            holder.textView21.visibility=View.VISIBLE
        }else{
            holder.tvGroupDateDate.visibility=View.GONE
            holder.tvGroupDateText.visibility=View.GONE
            holder.tvGroupDateDetail.visibility=View.GONE
            holder.tvGroupDateEdit.visibility=View.GONE
            holder.tvGroupDateDel.visibility=View.GONE
            holder.textView21.visibility=View.GONE
        }





    }

    override fun getItemCount(): Int {
       return dateList.size
    }
}