package com.example.echo.board

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.echo.R

class BoardListAdapter(var context: Context, var BoardList:ArrayList<BoardListVO>)
    :RecyclerView.Adapter<BoardListAdapter.ViewHolder>(), Filterable {

    var initCk = true
    var initCn = 0
    var filteredBoard = ArrayList<BoardListVO>()
    var unfilteredBoard = BoardList
    var itemFilter = ItemFilter()

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
                var filteredList = filteredBoard[position]
//                if (position != RecyclerView.NO_POSITION) {
//                    // 버그로 인해 -1이 아닐경우에
//                    mOnItemClickListener.onItemClick(itemView, position)
//                }
                val intent = Intent(context, BoardDetailActivity::class.java)

                intent.putExtra("board_seq", filteredList.board_seq.toString())
                intent.putExtra("board_title", filteredList.board_title)
                intent.putExtra("board_content", filteredList.board_content)
                intent.putExtra("board_file",filteredList.board_file)
                intent.putExtra("user_nick", filteredList.user_nick)
                intent.putExtra("user_profile_img", filteredList.user_profile_img)
                intent.putExtra("board_dt", filteredList.board_dt)
                intent.putExtra("user_id", filteredList.user_id)
                intent.putExtra("mnt_name", filteredList.mnt_name)
                intent.putExtra("board_reco_cnt", filteredList.board_reco_cnt.toString())

                context.startActivity(intent)
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
            Log.d("test-필터","언제 실행됨?")
            val filteredList: BoardListVO = filteredBoard[position]
            Log.d("test-필터",filteredList.toString())
            initCk = false

                holder.tvBoardTitle.text = filteredList.board_title
                holder.tvBoardWriter.text = filteredList.user_nick
                holder.tvBoardDate.text = filteredList.board_dt.substring(0,filteredList.board_dt.length-3)
                holder.tvBoardRecoCount.text = filteredList.board_reco_cnt.toString()
                holder.tvBoardMntName.text = filteredList.mnt_name
                holder.tvBoardCommCount.text = filteredList.board_cmt_cnt.toString()
                if(filteredList.board_file.isEmpty()) {
                    holder.imgBoardPic.visibility = View.INVISIBLE
                } else{
                Glide.with(context)
                    .load(filteredList.board_file)
                    .into(holder.imgBoardPic)} //지역변수

        }

        override fun getItemCount(): Int {
            Log.d("test-필터","순서가 궁금해")
            if(initCk&&initCn==3){ filteredBoard.addAll(BoardList)}
            Log.d("test-필터",filteredBoard.size.toString())
            initCn++
            return filteredBoard.size
        }

    //-- filter
    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()
            initCk = false
            //검색이 필요없을 경우를 위해 원본 배열을 복제
            var filteredList: ArrayList<BoardListVO> = ArrayList<BoardListVO>()
            //공백제외 아무런 값이 없을 경우 -> 원본 배열
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                filteredList = BoardList
            } //그 외의 경우 검색
            else {
                for (board in BoardList) {
                    if (board.board_title.contains(filterString)
                        || board.mnt_name.contains(filterString)
                    ) {
                        filteredList.add(board)
                    }
                }
            }
            results.values = filteredList
            results.count = filteredList.size

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            filteredBoard.clear()
            filteredBoard.addAll(filterResults.values as ArrayList<BoardListVO>)
            notifyDataSetChanged()
        }
    }


}
