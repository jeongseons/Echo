package com.example.echo.group.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.echo.R
import com.example.echo.group.GroupActivity

class DetailLocationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_location, container, false)

        val activity = activity as GroupActivity
        val btnDetailLocPrev = view.findViewById<Button>(R.id.btnDetailLocPrev)

        btnDetailLocPrev.setOnClickListener {activity.changeFragment(DetailTalkFragment())}
    return view
    }

}