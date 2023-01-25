package com.example.echo.group.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.example.echo.R

class DetailSettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_setting, container, false)

        //모임장확인
        val GroupAuth = requireActivity().intent.getStringExtra("auth")
        val GroupTitle = requireActivity().intent.getStringExtra("title")
        val GroupSeq = requireActivity().intent.getIntExtra("num",0)

        //모임명 출력
        val tvGroupSettingName = view.findViewById<TextView>(R.id.tvGroupSettingName)
        tvGroupSettingName.text = GroupTitle

        Log.d("값확인-GroupSeq",GroupSeq.toString())
        Log.d("값확인-GroupTitle",GroupTitle.toString())
        Log.d("값확인-GroupAuth",GroupAuth.toString())


        //권한에 따라 출력 변경
        val tvGroupSettingMax = view.findViewById<TextView>(R.id.tvGroupSettingMax)

        val tvGroupSettingType = view.findViewById<TextView>(R.id.tvGroupSettingType)

        val tvGroupSettingArea = view.findViewById<TextView>(R.id.tvGroupSettingArea)

        val tvGroupSettingLevel = view.findViewById<TextView>(R.id.tvGroupSettingLevel)

        val spGroupSettingMax = view.findViewById<Spinner>(R.id.spGroupSettingMax)
        val spGroupSettingType = view.findViewById<Spinner>(R.id.spGroupSettingType)
        val spGroupSettingArea = view.findViewById<Spinner>(R.id.spGroupSettingArea)
        val spGroupSettingLevel = view.findViewById<Spinner>(R.id.spGroupSettingLevel)

        val btnGroupSettingInfoEdit =view.findViewById<Button>(R.id.btnGroupSettingInfoEdit)
        val tvGroupSettingDel = view.findViewById<TextView>(R.id.tvGroupSettingDel)
        val imgGroupProfileEdit = view.findViewById<ImageView>(R.id.imgGroupProfileEdit)


        if(GroupAuth.equals("y")){
            tvGroupSettingMax.visibility=View.GONE
            tvGroupSettingType.visibility=View.GONE
            tvGroupSettingArea.visibility=View.GONE
            tvGroupSettingLevel.visibility=View.GONE

            spGroupSettingMax.visibility=View.VISIBLE
            spGroupSettingType.visibility=View.VISIBLE
            spGroupSettingArea.visibility=View.VISIBLE
            spGroupSettingLevel.visibility=View.VISIBLE
            btnGroupSettingInfoEdit.visibility=View.VISIBLE
            tvGroupSettingDel.visibility=View.VISIBLE
            imgGroupProfileEdit.visibility=View.VISIBLE
        }else{
            tvGroupSettingMax.visibility=View.VISIBLE
            tvGroupSettingType.visibility=View.VISIBLE
            tvGroupSettingArea.visibility=View.VISIBLE
            tvGroupSettingLevel.visibility=View.VISIBLE

            spGroupSettingMax.visibility=View.GONE
            spGroupSettingType.visibility=View.GONE
            spGroupSettingArea.visibility=View.GONE
            spGroupSettingLevel.visibility=View.GONE
            btnGroupSettingInfoEdit.visibility=View.GONE
            tvGroupSettingDel.visibility=View.GONE
            imgGroupProfileEdit.visibility=View.GONE
        }



        val imgGroupSetting = view.findViewById<ImageView>(R.id.imgGroupSetting)


        imgGroupSetting.setImageResource(R.drawable.profile)








        return view
    }

}