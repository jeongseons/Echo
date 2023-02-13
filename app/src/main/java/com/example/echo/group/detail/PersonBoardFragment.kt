package com.example.echo.group.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.board.BoardListAdapter
import com.example.echo.board.BoardListVO

class PersonBoardFragment : Fragment() {
    var boardList = ArrayList<BoardListVO>()
    lateinit var adapter: BoardListAdapter

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

        Log.d("test-번들3", boardList.toString())

        this.adapter = BoardListAdapter(requireContext(), boardList)

        rvPersonBoard.adapter = adapter
        rvPersonBoard.layoutManager = LinearLayoutManager(requireContext())


        return view
    }


}