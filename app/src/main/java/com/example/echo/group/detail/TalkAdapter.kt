package com.example.echo.group.detail

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.group.GroupActivity
import com.example.echo.group.Message
import com.example.echo.myPage.MyPageVO
import com.example.echo.myPage.binding
import com.example.echo.myPage.user_profile_img
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TalkAdapter(
    val context: Context, val talkList: ArrayList<Message>
) : RecyclerView.Adapter<TalkAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //다른 유저
        val imgTalkPro: ImageView
        val imgTalkL: ImageView
        val tvLTalkContent: TextView
        val tvLTalkTime: TextView
        val tvTalkNick: TextView

        //나
        val tvRTalkTime: TextView
        val tvRTalkContent: TextView
        val imgTalkR: ImageView

        val activity = context as GroupActivity


        init {
            //다른 유저
            imgTalkPro = itemView.findViewById(R.id.imgTalkPro)
            tvLTalkContent = itemView.findViewById(R.id.tvLTalkContent)
            tvLTalkTime = itemView.findViewById(R.id.tvLTalkTime)
            tvTalkNick = itemView.findViewById(R.id.tvTalkNick)
            imgTalkL = itemView.findViewById(R.id.imgTalkL)

            // 해당 다른 유저의 현재 위치를 실시간으로 보여주는 화면으로 전환
            imgTalkPro.setOnClickListener {
                activity.changeFragment(
                    DetailLocationFragment(),
                    talkList[adapterPosition].x,
                    talkList[adapterPosition].y
                )
            }

            //나
            imgTalkR = itemView.findViewById(R.id.imgTalkR)
            tvRTalkTime = itemView.findViewById(R.id.tvRTalkTime)
            tvRTalkContent = itemView.findViewById(R.id.tvRTalkContent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.talk_list, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user_id = talkList[position].proId

        //프로필 사진
            Glide.with(context!!)
                .load("https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/${user_id}.png?alt=media")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imgTalkPro)


        holder.tvLTalkTime.text = talkList[position].date
        holder.tvRTalkTime.text = talkList[position].date

        if (talkList[position].ui=="me") {//내가 친 채팅
            holder.tvRTalkTime.text = talkList[position].date
            holder.tvRTalkContent.setText(talkList[position].content)

            holder.imgTalkR.visibility = View.VISIBLE
            holder.tvRTalkTime.visibility = View.VISIBLE
            holder.tvRTalkContent.visibility = View.VISIBLE

            holder.imgTalkL.visibility = View.GONE
            holder.imgTalkPro.visibility = View.GONE
            holder.tvLTalkContent.visibility = View.GONE
            holder.tvLTalkTime.visibility = View.GONE
            holder.tvTalkNick.visibility = View.GONE

        } else {//남이 친 채팅
            holder.tvLTalkContent.setText(talkList[position].content)
            holder.tvTalkNick.setText(talkList[position].sender)

            holder.imgTalkL.visibility = View.VISIBLE
            holder.imgTalkPro.visibility = View.VISIBLE
            holder.tvLTalkContent.visibility = View.VISIBLE
            holder.tvLTalkTime.visibility = View.VISIBLE
            holder.tvTalkNick.visibility = View.VISIBLE

            holder.imgTalkR.visibility = View.GONE
            holder.tvRTalkTime.visibility = View.GONE
            holder.tvRTalkContent.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return talkList.size
    }

//    fun getMyPage(user_id: String) {
//        val call = RetrofitBuilder.userApi.getMyPage(user_id)
//        call.enqueue(object : Callback<MyPageVO> {
//            override fun onResponse(
//                call: Call<MyPageVO>, response: Response<MyPageVO>,
//            ) {
//                if (response.isSuccessful) {
//                    Log.d("text-마이페이지", "실행중")
//                    var body = response.body()!!
//                    Log.d("text-마이페이지", body.toString())
//                    myNick = body.user_nick
//                }
//            }
//
//            override fun onFailure(call: Call<MyPageVO>, t: Throwable) {
//            }
//        })
//    }

}