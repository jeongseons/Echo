package com.example.echo.group

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import java.security.acl.Group

class GroupFragment : Fragment() {

    lateinit var adapter :GroupListAdapter
    var groupList = ArrayList<GroupVO>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)

        val rvGroupList = view.findViewById<RecyclerView>(R.id.rvGroupList)
        val btnAddGroup = view.findViewById<Button>(R.id.btnAddGroup)
        val btnEnterGroup = view.findViewById<Button>(R.id.btnEnterGroup)
        val personCnt1: Int = 1
        val personCnt2: Int = 4
        val personLimit1: Int = 3
        val personLimit2: Int = 5

        groupList.add(GroupVO(R.drawable.p1,true,"광주 무등산으로 ㄱㄱ","${personCnt1}/${personLimit1}"))
        groupList.add(GroupVO(R.drawable.p1,true,"대전 계룡산으로 ㄱㄱ","${personCnt2}/${personLimit2}"))

        adapter = GroupListAdapter(requireContext(), groupList)
        //어댑터 리스트로 띄워졌을때 해당 액티비티로 이동해야함.
        rvGroupList.adapter = adapter
        rvGroupList.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

}