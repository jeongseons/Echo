package com.example.echo.board

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.RetrofitBuilder
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
        val view =  inflater.inflate(R.layout.fragment_board, container, false)
        val btnBoardPost = view.findViewById<Button>(R.id.btnBoardPost)
        val svBoardSearch = view.findViewById<SearchView>(R.id.svBoardSearch)
//        svBoardSearch.isSubmitButtonEnabled = true

        btnBoardPost.setOnClickListener {
            val intent = Intent(requireContext(), BoardWriteActivity::class.java)
            startActivity(intent)
        }

        var rvBoardList = view.findViewById<RecyclerView>(R.id.rvBoardList)

        //모든 게시글 정보
        getBoard(null)
        Log.d("text-create안게시글전부조회", boardList.toString())

        // 검색 기능
        var searchViewTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
                override fun onQueryTextSubmit(s: String): Boolean {
                    return false
                }
                //텍스트 입력/수정시에 호출
                override fun onQueryTextChange(s: String): Boolean {
                    adapter.getFilter().filter(s)
                    Log.d("test-서치뷰", "SearchVies Text is changed : $s")
                    return false
                }
            }

        svBoardSearch.setOnQueryTextListener(searchViewTextListener)

        adapter = BoardListAdapter(requireContext(), boardList)

        // 각 게시글 클릭 이벤트 - 게시글 내부로 이동
//        adapter.setOnItemClickListener(object : BoardListAdapter.OnItemClickListener {
//            override fun onItemClick(view: View, position: Int) {
//
//                val intent = Intent(requireActivity(), BoardDetailActivity::class.java)
//
//                intent.putExtra("board_seq", boardList[position].board_seq.toString())
//                intent.putExtra("board_title", boardList[position].board_title)
//                intent.putExtra("board_content", boardList[position].board_content)
//                intent.putExtra("board_file",boardList[position].board_file)
//                intent.putExtra("user_nick", boardList[position].user_nick)
//                intent.putExtra("board_dt", boardList[position].board_dt)
//                intent.putExtra("user_id", boardList[position].user_id)
//                intent.putExtra("mnt_name", boardList[position].mnt_name)
//                intent.putExtra("board_reco_cnt", boardList[position].board_reco_cnt.toString())
//
//                startActivity(intent)
//
//            }
//        })

        rvBoardList.adapter = adapter
        rvBoardList.layoutManager = LinearLayoutManager(requireContext())


        return view

    }

    fun getBoard(user_id:String?) {
        boardList.clear()
        val call = RetrofitBuilder.boardApi.getBoard(null)
        call.enqueue(object : Callback<List<BoardListVO>> {
            override fun onResponse(call: Call<List<BoardListVO>>, response: Response<List<BoardListVO>>) {
                if(response.isSuccessful&& response.body()?.size!!>0){
                    for(i in 0 until response.body()!!.size){
                        boardList.add(response.body()!!.get(i))
                Log.d("test-게시글전부조회", response.body().toString())
                    }
                }
                boardList.reverse()
                adapter.notifyDataSetChanged()
                Log.d("test-재조회여부", "yes")
            }
            override fun onFailure(call: Call<List<BoardListVO>>, t: Throwable) {
                Log.d("test-게시글전부조회", t.localizedMessage)

            }
        })
    }

    fun refresh(){
        val intent = (context as Activity).intent
        (context as Activity).finish() //현재 액티비티 종료 실시
        (context as Activity).overridePendingTransition(0, 0) //효과 없애기
        (context as Activity).startActivity(intent) //현재 액티비티 재실행 실시
        (context as Activity).overridePendingTransition(0, 0) //효과 없애기
    }

}


