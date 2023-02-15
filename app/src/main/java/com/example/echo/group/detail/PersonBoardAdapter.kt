package com.example.echo.group.detail

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.echo.R
import com.example.echo.board.BoardDetailActivity
import com.example.echo.board.BoardListVO

class PersonBoardAdapter(var context: Context, var boardList:ArrayList<BoardListVO>)
    : RecyclerView.Adapter<PersonBoardAdapter.ViewHolder>() {

    // 리스너 커스텀
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    // 객체 저장 변수 선언
    lateinit var mOnItemClickListener: OnItemClickListener

    //객체 전달 메서드
    fun setOnItemClickListener(OnItemClickListener: OnItemClickListener) {
        mOnItemClickListener = OnItemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBoardTitle: TextView
        val tvBoardDate: TextView
        val tvBoardWriter: TextView
        val tvBoardRecoCount: TextView
        val tvBoardMntName : TextView
        val tvBoardCommCount : TextView
        val imgBoardPic : ImageView

        init {
            tvBoardTitle = itemView.findViewById(R.id.tvBoardTitle)
            tvBoardWriter = itemView.findViewById(R.id.tvBoardWriter)
            tvBoardDate = itemView.findViewById(R.id.tvBoardDate)
            tvBoardRecoCount = itemView.findViewById(R.id.tvBoardRecoCount)
            tvBoardMntName = itemView.findViewById(R.id.tvBoardMntName)
            tvBoardCommCount = itemView.findViewById(R.id.tvBoardCommCount)
            imgBoardPic = itemView.findViewById(R.id.imgBoardPic)

            // 각 게시글 클릭 이벤트 - 게시글 내부로 이동
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // 버그로 인해 -1이 아닐경우에
                    mOnItemClickListener.onItemClick(itemView, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("test-필터","언제 실행되니")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.board_list, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvBoardTitle.text = boardList[position].board_title
        holder.tvBoardWriter.text = boardList[position].user_nick
        holder.tvBoardDate.text = boardList[position].board_dt.substring(0,boardList[position].board_dt.length-3)
        holder.tvBoardRecoCount.text = boardList[position].board_reco_cnt.toString()
        holder.tvBoardMntName.text = boardList[position].mnt_name
        holder.tvBoardCommCount.text = boardList[position].board_cmt_cnt.toString()
        if(boardList[position].board_file.isEmpty()) {
            holder.imgBoardPic.visibility = View.INVISIBLE
        } else{
            Glide.with(context)
                .load(boardList[position].board_file)
                .into(holder.imgBoardPic)} //지역변수

    }

    override fun getItemCount(): Int {
        return boardList.size
    }

}