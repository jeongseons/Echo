package com.example.echo.myPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.board.BoardListVO

class MyBoardAdapter(var context: Context, var myBoardList:ArrayList<BoardListVO>)
    : RecyclerView.Adapter<MyBoardAdapter.ViewHolder>() {

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

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        val tvMyBoardTitle: TextView
        val tvMyBoardDate: TextView
        val tvMyBoardWriter: TextView
        val tvMyBoardRecoCount: TextView
        val tvMyBoardMntName: TextView
        val ckMyBoard: CheckBox

        init {
            tvMyBoardTitle = itemView.findViewById(R.id.tvMyBoardTitle)
            tvMyBoardWriter = itemView.findViewById(R.id.tvMyBoardWriter)
            tvMyBoardDate = itemView.findViewById(R.id.tvMyBoardDate)
            tvMyBoardRecoCount = itemView.findViewById(R.id.tvMyBoardRecoCount)
            tvMyBoardMntName = itemView.findViewById(R.id.tvMyBoardMntName)
            ckMyBoard = itemView.findViewById(R.id.ckMyBoard)

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
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.my_board_list,null)

        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvMyBoardTitle.text = myBoardList[position].board_title
        holder.tvMyBoardWriter.text = myBoardList[position].user_nick
        holder.tvMyBoardDate.text = myBoardList[position].board_dt
        holder.tvMyBoardRecoCount.text = myBoardList[position].board_reco_cnt.toString()
        holder.tvMyBoardMntName.text = myBoardList[position].mnt_name
        holder.ckMyBoard.setOnClickListener(View.OnClickListener {
            holder.ckMyBoard.isChecked = true
        })

    }

    override fun getItemCount(): Int {
        return myBoardList.size
    }


}