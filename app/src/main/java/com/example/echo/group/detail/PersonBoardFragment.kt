package com.example.echo.group.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.board.BoardDetailActivity
import com.example.echo.board.BoardListAdapter
import com.example.echo.board.BoardListVO
import com.example.echo.myPage.MyBoardAdapter
import com.example.echo.myPage.myBoardList

class PersonBoardFragment : Fragment() {
    var boardList = ArrayList<BoardListVO>()
    lateinit var adapter: PersonBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        boardList = arguments?.getSerializable("boardList") as ArrayList<BoardListVO>

        var bundle = arguments
        if (bundle != null) {
            boardList = bundle.getSerializable("boardList") as ArrayList<BoardListVO>
            Log.d("test-번들1", boardList.toString())
        }

        arguments?.let{
            boardList = it.getSerializable("boardList") as ArrayList<BoardListVO>
            Log.d("test-번들2", boardList.toString())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_person_board, container, false)
        val rvPersonBoard = view.findViewById<RecyclerView>(R.id.rvPersonBoard)
        var tvPersonBoardText = view.findViewById<TextView>(R.id.tvPersonBoardText)

        tvPersonBoardText.text = "총 ${boardList.size}개의 글이 있습니다다"

       Log.d("test-번들3", boardList.toString())

        this.adapter = PersonBoardAdapter(requireContext(), boardList)

        rvPersonBoard.adapter = adapter
        rvPersonBoard.layoutManager = LinearLayoutManager(requireContext())
        adapter.notifyDataSetChanged()

        //         각 게시글 클릭 이벤트 - 게시글 내부로 이동
        adapter.setOnItemClickListener(object : PersonBoardAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(requireContext(), BoardDetailActivity::class.java)
                intent.putExtra("board_seq", boardList[position].board_seq.toString())
                intent.putExtra("board_title", boardList[position].board_title)
                intent.putExtra("board_content", boardList[position].board_content)
                intent.putExtra("board_file", boardList[position].board_file)
                intent.putExtra("user_nick", boardList[position].user_nick)
                intent.putExtra("board_dt", boardList[position].board_dt)
                intent.putExtra("user_id", boardList[position].user_id)
                intent.putExtra("mnt_name", boardList[position].mnt_name)
                intent.putExtra("board_reco_cnt", boardList[position].board_reco_cnt.toString())
                intent.putExtra("user_profile_img", boardList[position].user_profile_img)
                startActivity(intent)
            }
        })

        return view
    }


}