package com.example.echo.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.echo.R




class BoardPost : Fragment() {




    lateinit var imgBoardPostBookmark : ImageView
    lateinit var imgBoardPostPic : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_board_post, container, false)

        imgBoardPostBookmark = view.findViewById(R.id.imgBoardPostBookmark)
        imgBoardPostPic = view.findViewById(R.id.imgBoardPostPic)

        val tvBoardPostTitle = view.findViewById<TextView>(R.id.tvBoardPostTitle)
        val tvBoardPostUserNick = view.findViewById<TextView>(R.id.tvBoardPostUserNick)
        val tvBoardPostDate = view.findViewById<TextView>(R.id.tvBoardPostDate)
        val tvBoardPostContent = view.findViewById<TextView>(R.id.tvBoardPostContent)


        return view
    }


}