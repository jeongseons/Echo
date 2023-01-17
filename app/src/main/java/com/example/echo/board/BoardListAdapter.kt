package com.example.echo.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R

class BoardListAdapter(var context: Context, var BoardList:ArrayList<BoardListVO>)
    :RecyclerView.Adapter<BoardListAdapter.ViewHolder>() {

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

        init {
            tvBoardTitle = itemView.findViewById(R.id.tvBoardTitle)
            tvBoardWriter = itemView.findViewById(R.id.tvBoardWriter)
            tvBoardDate = itemView.findViewById(R.id.tvBoardDate)
            tvBoardRecoCount = itemView.findViewById(R.id.tvBoardRecoCount)

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
        val view = inflater.inflate(R.layout.board_list, null)
        return ViewHolder(view) //ViewHolder로 View를 보내줌
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvBoardTitle.text = BoardList[position].board_title
            holder.tvBoardWriter.text = BoardList[position].user_nick
            holder.tvBoardDate.text = BoardList[position].board_dt
            holder.tvBoardRecoCount.text = BoardList[position].board_reco_cnt.toString()
        }

        override fun getItemCount(): Int {
            return BoardList.size
        }


    }
