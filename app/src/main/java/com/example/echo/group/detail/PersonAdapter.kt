package com.example.echo.group.detail

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PersonAdapter(val context: Context, val personlist: ArrayList<PersonVO>, val title: String):
    RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPersonNick: TextView
        val tvPersonAuth: TextView
        val imgPersonKing: ImageView
        val imgPersonPro: ImageView

        init {
            tvPersonNick = itemView.findViewById(R.id.tvPersonNick)
            tvPersonAuth = itemView.findViewById(R.id.tvPersonAuth)
            imgPersonKing = itemView.findViewById(R.id.imgPersonKing)
            imgPersonPro = itemView.findViewById(R.id.imgPersonPro)
        tvPersonAuth.setOnClickListener { //강퇴하기
            if(!personlist[adapterPosition].auth) {
                onClickAlert(personlist[adapterPosition].nick.toString())
            }
        }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.person_list, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvPersonNick.setText(personlist[position].nick)
        holder.imgPersonPro.setImageResource(personlist[position].profile)//
        if(!personlist[position].auth){
            holder.imgPersonKing.isVisible = false
        }else{
            holder.tvPersonAuth.text = "그룹장"
        }

    }

    override fun getItemCount(): Int {
        return personlist.size
    }

    fun onClickAlert(nick: String) { //alert 코드.
        val msgBuilder = AlertDialog.Builder(context)
            .setMessage("${nick}님을 정말 강퇴할까요?")
            .setPositiveButton(
                "강퇴"
            ) { dialogInterface, i -> dropUser() }
            .setNegativeButton(
                "취소"
            ) { dialogInterface, i ->
                Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show()
            }

        val msgDlg = msgBuilder.create()
        msgDlg.show()
    }

    fun dropUser(){
        val call = RetrofitBuilder.api.dropUser()
        call.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    val intent = (context as Activity).intent
                    context.finish() //현재 액티비티 종료 실시
                    context.overridePendingTransition(0, 0) //효과 없애기
                    context.startActivity(intent) //현재 액티비티 재실행 실시
                    context.overridePendingTransition(0, 0) //효과 없애기

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("드랍실패", t.localizedMessage)
            }

        })
    }

}