package com.example.echo.group.detail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.group.DateVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailDateAdapter(val context: Context, val dateList:ArrayList<DateVO>, val selected:String, val auth:String):RecyclerView.Adapter<DetailDateAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvGroupDateDate : TextView
        val tvGroupDateDetail : TextView
        val tvGroupDateText : TextView
        val tvGroupDateEdit : TextView
        val tvGroupDateDel : TextView
        val textView21 : TextView
        val view11 : View

        init {
            tvGroupDateDate = itemView.findViewById(R.id.tvGroupDateDate)
            tvGroupDateDetail = itemView.findViewById(R.id.tvGroupDateDetail)
            tvGroupDateText = itemView.findViewById(R.id.tvGroupDateText)
            tvGroupDateEdit = itemView.findViewById(R.id.tvGroupDateEdit)
            tvGroupDateDel = itemView.findViewById(R.id.tvGroupDateDel)
            textView21 = itemView.findViewById(R.id.textView21)
            view11 = itemView.findViewById(R.id.view11)

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
            holder.tvGroupDateDate.text=dateList[position].cal_dt.substring(0,16)
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


        }else{
            holder.tvGroupDateDate.visibility=View.GONE
            holder.tvGroupDateText.visibility=View.GONE
            holder.tvGroupDateDetail.visibility=View.GONE
            holder.tvGroupDateEdit.visibility=View.GONE
            holder.tvGroupDateDel.visibility=View.GONE
            holder.view11.visibility=View.GONE
            holder.textView21.visibility=View.GONE

        }

        holder.tvGroupDateEdit.setOnClickListener {
            val intent = Intent(context, AddNewGroupDateActivity::class.java)
            intent.putExtra("modifyCk","true")
            intent.putExtra("cal_seq", dateList[position].cal_seq)
            intent.putExtra("cal_dt", dateList[position].cal_dt)
            intent.putExtra("cal_content", dateList[position].cal_content)
            intent.putExtra("group_seq", dateList[position].group_seq)
            context.startActivity(intent)
        }

        holder.tvGroupDateDel.setOnClickListener {
            Log.d("test-????????????", dateList[position].cal_seq.toString())
            val dialog: AlertDialog.Builder = AlertDialog.Builder(
                context,
                android.R.style.ThemeOverlay_Material_Dialog_Alert
            )
            dialog.setMessage("????????? ?????????????????????????")
                .setTitle("?????? ??????")
                .setPositiveButton("?????????") { dialog, which ->
                    Log.i("Dialog", "??????")

                }
                .setNeutralButton("???"
                ) { dialog, which ->
                    deleteCal(dateList[position].cal_seq)
                    android.R.style.ThemeOverlay_Material_Dialog_Alert
                }
                .show()
        }


    }

    override fun getItemCount(): Int {
       return dateList.size
    }

    fun deleteCal(cal_seq:Int){
        val call = RetrofitBuilder.api.deleteCal(cal_seq)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Log.d("test-?????????", response.body().toString())
                if(response.isSuccessful) {
                    Toast.makeText(
                        context, "??????????????? ?????????????????????",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    Toast.makeText(
                        context, "?????? ??????????????????",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-????????????", t.localizedMessage)
            }
        })
    }

}