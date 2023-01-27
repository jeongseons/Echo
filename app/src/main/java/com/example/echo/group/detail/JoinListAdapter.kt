package com.example.echo.group.detail


import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JoinListAdapter(val context: Context, val joinList: ArrayList<PersonVO>, val auth:String
, val seq:Int) :
    RecyclerView.Adapter<JoinListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgJoinListProfile :ImageView
        val tvJoinListName : TextView
        val tvJoinListYes : TextView
        val tvJoinListNo : TextView

        init {
            imgJoinListProfile = itemView.findViewById(R.id.imgJoinListProfile)
            tvJoinListName = itemView.findViewById(R.id.tvJoinListName)
            tvJoinListYes = itemView.findViewById(R.id.tvJoinListYes)
            tvJoinListNo = itemView.findViewById(R.id.tvJoinListNo)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.group_join_list, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(auth=="y"){//모임장인 경우에만 가입신청 확인 가능

            holder.imgJoinListProfile.visibility = View.VISIBLE
            holder.tvJoinListName.visibility = View.VISIBLE
            holder.tvJoinListYes.visibility = View.VISIBLE
            holder.tvJoinListNo.visibility = View.VISIBLE

            holder.tvJoinListName.setText(joinList[position].user_nick)

            Glide.with(this.context)
                .load(joinList[position].user_profile_img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imgJoinListProfile)


            holder.tvJoinListYes.setOnClickListener{
                //수락했을 경우
                GroupAgree(seq, joinList[position].user_nick)
                Log.d("값확인-가입승인","가입승인")
            }

            holder.tvJoinListNo.setOnClickListener{
                //거절했을 경우
                GroupDegree(seq, joinList[position].user_nick)
                Log.d("값확인-가입거절","가입거절")
            }
        }else{ //모임장이 아닌 경우 가입신청 확인 불가

            holder.imgJoinListProfile.visibility = View.GONE
            holder.tvJoinListName.visibility = View.GONE
            holder.tvJoinListYes.visibility = View.GONE
            holder.tvJoinListNo.visibility = View.GONE

        }




    }

    override fun getItemCount(): Int {
        return joinList.size
    }

    fun GroupAgree(num:Int, nick:String) {
        val call = RetrofitBuilder.api.groupAgree(num, nick)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("가입 실행", response.body().toString())
                    val intent = (context as Activity).intent
                    context.finish() //현재 액티비티 종료 실시
                    context.overridePendingTransition(0, 0) //효과 없애기
                    context.startActivity(intent) //현재 액티비티 재실행 실시
                    context.overridePendingTransition(0, 0) //효과 없애기

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("실패", t.localizedMessage)
            }

        })
    }

    fun GroupDegree(num:Int, nick:String) {
        val call = RetrofitBuilder.api.groupDegree(num, nick)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("거절 실행", response.body().toString())
                    val intent = (context as Activity).intent
                    context.finish() //현재 액티비티 종료 실시
                    context.overridePendingTransition(0, 0) //효과 없애기
                    context.startActivity(intent) //현재 액티비티 재실행 실시
                    context.overridePendingTransition(0, 0) //효과 없애기

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("실패", t.localizedMessage)
            }

        })
    }


}
