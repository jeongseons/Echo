package com.example.echo.board

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R


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
            val intent = Intent(requireContext(), BoardWrite::class.java)
            startActivity(intent)
        }

//        var rvBoardList = view.findViewById<RecyclerView>(R.id.rvBoardList)



        return view

    }



}