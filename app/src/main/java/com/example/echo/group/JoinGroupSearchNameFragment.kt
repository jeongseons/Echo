package com.example.echo.group

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.KeyEvent.KEYCODE_NUMPAD_ENTER
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R

class JoinGroupSearchNameFragment : Fragment() {

    lateinit var adapter: JoinGroupAdapter
    var groupList = ArrayList<GroupVO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_join_group_search_name, container, false)
        val rvJoinGroupName = view.findViewById<RecyclerView>(R.id.rvJoinGroupName)
        val etJoinGroupNameSearch = view.findViewById<EditText>(R.id.etJoinGroupNameSearch)
        val imgJoinGroupNameSearch = view.findViewById<ImageView>(R.id.imgJoinGroupNameSearch)


        groupList.add(GroupVO(1,"1","n","테스트",1,1))

        adapter = JoinGroupAdapter(requireContext(), groupList)
        //어댑터 리스트로 띄워졌을때 해당 액티비티로 이동해야함.
        rvJoinGroupName.adapter = adapter
        rvJoinGroupName.layoutManager = LinearLayoutManager(requireContext())

        //etJoinGroupNameSearch 에서 엔터키 클릭 시 값 보내기
        etJoinGroupNameSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                val searchName = etJoinGroupNameSearch.text.toString()
                //엔터키 입력시 이벤트 처리 구간
                Log.d("값확인-검색 모임 이름",searchName)
            }

            true
        }


        //돋보기 이미지 클릭 시 값 보내기
        imgJoinGroupNameSearch.setOnClickListener{
            val searchName = etJoinGroupNameSearch.text.toString()

            Log.d("값확인-검색 모임 이름",searchName)
        }

        return view
    }
}