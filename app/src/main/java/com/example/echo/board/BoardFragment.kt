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

    var BoardList = ArrayList<BoardVO>()
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

        adapter = BoardListAdapter(requireContext(), BoardList)

        rvBoardList.adapter = adapter
        rvBoardList.layoutManager = LinearLayoutManager(requireContext())


        return view

    }

    fun getBoard(){
        val call = RetrofitBuilder.boardApi.getBoard()
        call.enqueue(object : Callback<List<PostVO>> {
            override fun onResponse(call: Call<List<PostVO>>, response: Response<List<PostVO>>) {
                Log.d("text-게시글전부조회", response.body().toString())

            }
            override fun onFailure(call: Call<List<PostVO>>, t: Throwable) {
                Log.d("text-게시글전부조회", t.localizedMessage)

            }


        })
    }

}


