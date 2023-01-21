package com.example.echo.group.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.echo.R

class DetailSettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_setting, container, false)

        val imgGroupSettion = view.findViewById<ImageView>(R.id.imgGroupSettion)

        imgGroupSettion.setImageResource(R.drawable.profile)




        return view
    }

}