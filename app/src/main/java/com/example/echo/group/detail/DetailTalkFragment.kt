package com.example.echo.group.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.echo.R
import com.example.echo.group.GroupActivity


class DetailTalkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_talk, container, false)

        val btnToLocation = view.findViewById<Button>(R.id.btnToLocation)
        val activity = activity as GroupActivity

        btnToLocation.setOnClickListener { activity.changeFragment(DetailLocationFragment()) }

        return view
    }

}