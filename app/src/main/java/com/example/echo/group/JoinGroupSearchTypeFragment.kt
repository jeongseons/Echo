package com.example.echo.group

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R

class JoinGroupSearchTypeFragment : Fragment() {
    lateinit var adapter: GroupListAdapter
    var groupList = ArrayList<GroupVO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_join_group_search_type, container, false)


        val spJoinGroupTypeAge = view.findViewById<Spinner>(R.id.spJoinGroupTypeAge)
        val spJoinGroupTypeType = view.findViewById<Spinner>(R.id.spJoinGroupTypeType)
        val spJoinGroupTypeGender = view.findViewById<Spinner>(R.id.spJoinGroupTypeGender)
        val spJoinGroupTypeArea = view.findViewById<Spinner>(R.id.spJoinGroupTypeArea)
        val spJoinGroupTypeLevel = view.findViewById<Spinner>(R.id.spJoinGroupTypeLevel)
        val btnJoinGroupTypeFind = view.findViewById<Button>(R.id.btnJoinGroupTypeFind)
        val rvJoinGroupType = view.findViewById<RecyclerView>(R.id.rvJoinGroupType)


        spJoinGroupTypeAge.adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.age,
            android.R.layout.simple_spinner_dropdown_item)

        spJoinGroupTypeType.adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.type,
            android.R.layout.simple_spinner_dropdown_item)

        spJoinGroupTypeGender.adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.gender,
            android.R.layout.simple_spinner_dropdown_item)

        spJoinGroupTypeArea.adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.area,
            android.R.layout.simple_spinner_dropdown_item)

        spJoinGroupTypeLevel.adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.level,
            android.R.layout.simple_spinner_dropdown_item)


        groupList.add(GroupVO(1,"1","n","테스트",1,1))

        adapter = GroupListAdapter(requireContext(), groupList)
        //어댑터 리스트로 띄워졌을때 해당 액티비티로 이동해야함.
        rvJoinGroupType.adapter = adapter
        rvJoinGroupType.layoutManager = LinearLayoutManager(requireContext())

        btnJoinGroupTypeFind.setOnClickListener {
            val group_age = spJoinGroupTypeAge.selectedItem.toString()
            val group_type = spJoinGroupTypeType.selectedItem.toString()
            val group_gender = spJoinGroupTypeGender.selectedItem.toString()
            val group_area = spJoinGroupTypeArea.selectedItem.toString()
            val group_level = spJoinGroupTypeLevel.selectedItem.toString()

            val searchType = FindGroupVO(group_type,group_age,group_gender,group_level,group_area)
            Log.d("값확인-조건으로 검색",searchType.toString())
        }




        return view



    }
}