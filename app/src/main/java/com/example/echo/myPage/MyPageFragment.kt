package com.example.echo.myPage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.echo.R


class MyPageFragment : Fragment() {



    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View
    ? {
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)



        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val tvProfile = view.findViewById<TextView>(R.id.tvProfile)
        val tvBox1 = view.findViewById<TextView>(R.id.tvBox1)
        val tvBox2 = view.findViewById<TextView>(R.id.tvBox2)


        tvProfile.setOnClickListener {
            val intent = Intent(context, ReviseActivity::class.java)
//            intent.putExtra("rvItemList", tvProfile.tag.toString())
            startActivity(intent)
        }


        return view
    }


}