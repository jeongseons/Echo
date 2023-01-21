package com.example.echo.group.detail

import android.content.ContentValues
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.group.GroupActivity
import com.example.echo.group.GroupListAdapter
import com.example.echo.group.GroupVO
import com.example.echo.group.Message
import com.kakao.sdk.user.UserApiClient


class DetailTalkFragment : Fragment() {

    lateinit var adapter: TalkAdapter
    var talkList = ArrayList<Message>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_talk, container, false)

        val btn1 = view.findViewById<Button>(R.id.btnTalkchat1)
        val btn2 = view.findViewById<Button>(R.id.btnTalkchat2)
        val btn3 = view.findViewById<Button>(R.id.btnTalkchat3)
        val rvtalk = view.findViewById<RecyclerView>(R.id.rvtalk)

        talkList.add(
            Message(
                "good",
                "2617009803","04:00"
            )
        )
        talkList.add(
            Message(
                "U",
                "2617123456","05:00"
            )
        )


        adapter = TalkAdapter(requireContext(), talkList)
        //어댑터 리스트로 띄워졌을때 해당 액티비티로 이동해야함.
        rvtalk.adapter = adapter
        rvtalk.layoutManager = LinearLayoutManager(requireContext())

        return view


    }

}