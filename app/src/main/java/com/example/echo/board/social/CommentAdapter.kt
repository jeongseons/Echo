package com.example.echo.board.social

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentAdapter(var context: Context, var commentList:ArrayList<CmtListVO>)
    : RecyclerView.Adapter<CommentAdapter.ViewHolder>(){
        var loginUser = ""
        inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

            val tvCommentUserNick : TextView
            val tvCommentContent : TextView
            val tvCommentTime : TextView
            val tvCommentDelete : TextView

            init {
                tvCommentUserNick = itemView.findViewById(R.id.tvCommentUserNick)
                tvCommentContent = itemView.findViewById(R.id.tvCommentContent)
                tvCommentTime = itemView.findViewById(R.id.tvCommentDate)
                tvCommentDelete = itemView.findViewById(R.id.tvCommentDelete)
                itemView.setOnClickListener {
                    val position = adapterPosition
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.comment_list,null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCommentUserNick.setText(commentList.get(position).user_nick)
        holder.tvCommentContent.setText(commentList.get(position).cmt_content)
        holder.tvCommentTime.setText(commentList.get(position).cmt_dt)
        holder.tvCommentDelete.visibility = View.INVISIBLE

        UserApiClient.instance.me { user, error ->
            loginUser = user?.id.toString()
            if(loginUser==commentList.get(position).user_id){
                holder.tvCommentDelete.visibility = View.VISIBLE
            }
        }

        holder.tvCommentDelete.setOnClickListener {
            deleteComment(commentList.get(position).cmt_seq, commentList.get(position).board_seq)
        }

    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun deleteComment(cmt_seq:Int, board_seq:Int){
        val call = RetrofitBuilder.socialApi.deleteComment(cmt_seq, board_seq)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Log.d("test-삭제후", response.body().toString())
                if(response.isSuccessful) {
                    Toast.makeText(
                        context, "정상적으로 삭제되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()

                }else{
                    Toast.makeText(
                        context, "다시 시도해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-통신실패", t.localizedMessage)
            }
        })
    }

}