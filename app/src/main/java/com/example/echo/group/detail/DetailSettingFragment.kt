package com.example.echo.group.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R

class DetailSettingFragment : Fragment() {

    lateinit var adapter: JoinListAdapter
    var joinList = ArrayList<PersonVO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_setting, container, false)

        //모임장확인
        val groupAuth = requireActivity().intent.getStringExtra("auth")
        val groupTitle = requireActivity().intent.getStringExtra("title")
        val groupSeq = requireActivity().intent.getIntExtra("num", 0)


        val tvGroupSettingTitle = view.findViewById<TextView>(R.id.tvGroupSettingTitle)
        val imgGroupSettingProfile = view.findViewById<ImageView>(R.id.imgGroupSettingProfile)
        val tvGroupSettingEdit = view.findViewById<TextView>(R.id.tvGroupSettingEdit)
        val tvGroupSettingMax = view.findViewById<TextView>(R.id.tvGroupSettingMax)
        val tvGroupSettingType = view.findViewById<TextView>(R.id.tvGroupSettingType)
        val tvGroupSettingArea = view.findViewById<TextView>(R.id.tvGroupSettingArea)
        val tvGroupSettingLevel = view.findViewById<TextView>(R.id.tvGroupSettingLevel)
        val tvGroupSettingGender = view.findViewById<TextView>(R.id.tvGroupSettingGender)
        val tvGroupSettingAge = view.findViewById<TextView>(R.id.tvGroupSettingAge)
        val tvGroupSettingDetail = view.findViewById<TextView>(R.id.tvGroupSettingDetail)
        val rvGroupSettingJoinList = view.findViewById<RecyclerView>(R.id.rvGroupSettingJoinList)
        val tvGroupSettingDel = view.findViewById<TextView>(R.id.tvGroupSettingDel)
        val tvGroupSettingOut = view.findViewById<TextView>(R.id.tvGroupSettingOut)


        //모임명 셋팅
        tvGroupSettingTitle.setText(groupTitle)

        val textViewGroupSetting7 = view.findViewById<TextView>(R.id.textViewGroupSetting7)
        if(groupAuth=="y"){ //모임장만 출력
            textViewGroupSetting7.visibility=View.VISIBLE
            tvGroupSettingDel.visibility=View.VISIBLE

            tvGroupSettingOut.visibility=View.GONE
        }else{
            textViewGroupSetting7.visibility=View.GONE
            tvGroupSettingDel.visibility=View.GONE

            tvGroupSettingOut.visibility=View.VISIBLE
        }
        


        joinList.add(PersonVO("https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/2615613938.png?alt=media", "n", "test1"))
        joinList.add(PersonVO("https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/2615613938.png?alt=media", "n", "test2"))
        joinList.add(PersonVO("https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/2615613938.png?alt=media", "n", "test3"))


        //가입 신청 리스트 출력
        adapter = JoinListAdapter(requireContext(), joinList, groupAuth!!)
        rvGroupSettingJoinList.adapter = adapter
        rvGroupSettingJoinList.layoutManager = GridLayoutManager(requireContext(), 3)

        if(groupAuth.equals("y")){
            tvGroupSettingEdit.visibility=View.VISIBLE
            tvGroupSettingEdit.setOnClickListener{
                //정보 수정 클릭시 정보 수정 페이지로 이동
            }
        }else{
            tvGroupSettingEdit.visibility=View.GONE
        }












        return view
    }

}