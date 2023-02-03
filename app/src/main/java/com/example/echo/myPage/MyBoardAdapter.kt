package com.example.echo.myPage

import android.content.Context
import android.os.Handler
import android.util.Log
import android.util.SparseBooleanArray
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

    private val checkboxStatus = SparseBooleanArray()
    var allSelectCk = false
    val boardSeqList: ArrayList<Int> = ArrayList()

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
        val tvMyBoardRecoCount: TextView
        val tvMyBoardMntName: TextView
        val ckMyBoard: CheckBox
        val tvMyBoardCommCount : TextView

        init {
            tvMyBoardTitle = itemView.findViewById(R.id.tvMyBoardTitle)
            tvMyBoardDate = itemView.findViewById(R.id.tvMyBoardDate)
            tvMyBoardRecoCount = itemView.findViewById(R.id.tvMyBoardRecoCount)
            tvMyBoardMntName = itemView.findViewById(R.id.tvMyBoardMntName)
            ckMyBoard = itemView.findViewById(R.id.ckMyBoard)
            tvMyBoardCommCount = itemView.findViewById(R.id.tvMyBoardCommCount)

            // 각 게시글 클릭 이벤트 - 게시글 내부로 이동
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
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
        holder.tvMyBoardDate.text = myBoardList[position].board_dt
        holder.tvMyBoardRecoCount.text = myBoardList[position].board_reco_cnt.toString()
        holder.tvMyBoardMntName.text = myBoardList[position].mnt_name
        holder.ckMyBoard.isChecked = checkboxStatus[position]
        holder.tvMyBoardCommCount.text=myBoardList[position].board_cmt_cnt.toString()

        holder.ckMyBoard.isChecked = checkboxStatus[position]
        holder.ckMyBoard.setOnClickListener {
            if (!holder.ckMyBoard.isChecked) {
                checkboxStatus.put(position, false)
                boardSeqList.remove(myBoardList[position].board_seq)
            }
            else{
                checkboxStatus.put(position, true)
                boardSeqList.add(myBoardList[position].board_seq)
            }
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int {
        return myBoardList.size
    }

    fun setCheckAll() {
        boardSeqList.clear()
        if(allSelectCk) {
        for(i in 0 until myBoardList.size){
            checkboxStatus.put(i, false)
        }
        }else{
            for(i in 0 until myBoardList.size){
                checkboxStatus.put(i, true)
                boardSeqList.add(myBoardList[i].board_seq)
            }
        }
        allSelectCk = !allSelectCk
        notifyDataSetChanged()
    }

    fun selectDelete(): ArrayList<Int> {
            for (i in 0 until boardSeqList.size) {
                Log.d("test-체크박스", boardSeqList[i].toString())
            }
        return boardSeqList
    }

}