package com.example.echo.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.auth.JoinActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardFragment : Fragment() {

    var boardList = ArrayList<BoardListVO>()
    lateinit var adapter: BoardListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_board, container, false)
        val btnBoardPost = view.findViewById<Button>(R.id.btnBoardPost)

        btnBoardPost.setOnClickListener {
            val intent = Intent(requireContext(), BoardWriteActivity::class.java)
            startActivity(intent)
        }

        var rvBoardList = view.findViewById<RecyclerView>(R.id.rvBoardList)

        //모든 게시글 정보
        getBoard()
        Log.d("text-create안게시글전부조회", boardList.toString())

        adapter = BoardListAdapter(requireContext(), boardList)


        // 각 게시글 클릭 이벤트 - 게시글 내부로 이동
        adapter.setOnItemClickListener(object : BoardListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {

                val intent = Intent(requireActivity(), BoardDetailActivity::class.java)

                intent.putExtra("board_seq", boardList[position].board_seq.toString())
                intent.putExtra("board_title", boardList[position].board_title)
                intent.putExtra("board_content", boardList[position].board_content)
                intent.putExtra("board_file",boardList[position].board_file)
                intent.putExtra("user_nick", boardList[position].user_nick)
                intent.putExtra("board_dt", boardList[position].board_dt)
                intent.putExtra("user_id", boardList[position].user_id)
                intent.putExtra("mnt_name", boardList[position].mnt_name)
                intent.putExtra("board_reco_cnt", boardList[position].board_reco_cnt.toString())

                startActivity(intent)

            }
        })

        rvBoardList.adapter = adapter
        rvBoardList.layoutManager = LinearLayoutManager(requireContext())
        adapter.notifyDataSetChanged()

        return view

    }

    fun getBoard(){
        val call = RetrofitBuilder.boardApi.getBoard()
        call.enqueue(object : Callback<List<BoardListVO>> {
            override fun onResponse(call: Call<List<BoardListVO>>, response: Response<List<BoardListVO>>) {
                Log.d("text-게시글전부조회", response.body().toString())
                if(response.isSuccessful&& response.body()?.size!!>0){
                    for(i in 0 until response.body()!!.size){
                        boardList.add(response.body()!!.get(i))
                    }
                }
                boardList.reverse()
                adapter.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<List<BoardListVO>>, t: Throwable) {
                Log.d("text-게시글전부조회", t.localizedMessage)

            }


        })
    }

}


