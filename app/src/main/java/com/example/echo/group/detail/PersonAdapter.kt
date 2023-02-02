package com.example.echo.group.detail

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.myPage.binding
import com.example.echo.myPage.user_profile_img
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PersonAdapter(
    val context: Context, val personlist: ArrayList<PersonVO>, val title: String,
    val auth: String
) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

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
                if (auth != "n") {
                    if (personlist[adapterPosition].group_auth != "y") {
                        onClickAlert(personlist[adapterPosition].user_nick)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.person_list, null)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvPersonNick.setText(personlist[position].user_nick)

        if (personlist[position].group_auth != "y") {
            if (auth != "y") {
                holder.tvPersonAuth.text = "그룹원"
            }
            holder.imgPersonKing.isVisible = false
        } else {
            holder.tvPersonAuth.text = "그룹장"
            holder.tvPersonAuth.setTextColor(R.color.black)
        }

        Glide.with(this.context)
            .load(personlist[position].user_profile_img)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.imgPersonPro)

    }

    override fun getItemCount(): Int {
        return personlist.size
    }

    fun onClickAlert(nick: String) { //alert 코드.
        val msgBuilder = AlertDialog.Builder(context)
            .setMessage("${nick}님을 정말 강퇴할까요?")
            .setPositiveButton(
                "강퇴"
            ) { dialogInterface, i -> dropUser(nick) }
            .setNegativeButton(
                "취소"
            ) { dialogInterface, i ->
                Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show()
            }

        val msgDlg = msgBuilder.create()
        msgDlg.show()
    }

    fun dropUser(nick: String) {
        val call = RetrofitBuilder.api.dropUser(nick)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("드랍 실행", response.body().toString())
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