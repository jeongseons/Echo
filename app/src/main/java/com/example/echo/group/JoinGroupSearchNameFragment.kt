package com.example.echo.group

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R

class JoinGroupSearchNameFragment : Fragment() {

    lateinit var adapter: GroupListAdapter
    var groupList = ArrayList<GroupVO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_join_group_search_name, container, false)
        val rvJoinGroupName = view.findViewById<RecyclerView>(R.id.rvJoinGroupName)

        groupList.add(GroupVO(1,"1","n","테스트",1,1))

        adapter = GroupListAdapter(requireContext(), groupList)
        //어댑터 리스트로 띄워졌을때 해당 액티비티로 이동해야함.
        rvJoinGroupName.adapter = adapter
        rvJoinGroupName.layoutManager = LinearLayoutManager(requireContext())

        return view
    }
}