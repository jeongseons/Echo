package com.example.echo.group.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.group.GroupActivity
import com.example.echo.group.GroupListAdapter
import com.example.echo.group.GroupVO


class DetailPersonFragment : Fragment() {

    lateinit var adapter : PersonAdapter
    lateinit var id: String
    var personList = ArrayList<PersonVO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_person, container, false)
        val title = requireActivity().intent.getStringExtra("title")
        val rvPersonList = view.findViewById<RecyclerView>(R.id.rvPersonList)

        Log.d("title확인", title.toString())
        //C:\Users\smhrd\Desktop\echo\app\src\main\res\drawable\p1.png
        personList.add(PersonVO(R.drawable.p1,false,"가"))
        personList.add(PersonVO(R.drawable.p1,true,"나"))
        personList.add(PersonVO(R.drawable.p1,false,"다"))
        personList.add(PersonVO(R.drawable.p1,false,"라"))

        adapter = PersonAdapter(requireContext(), personList, title!!)
        //어댑터 리스트로 띄워졌을때 해당 액티비티로 이동해야함.
        rvPersonList.adapter = adapter
        rvPersonList.layoutManager = GridLayoutManager(requireContext(),3)

        return view
    }

}