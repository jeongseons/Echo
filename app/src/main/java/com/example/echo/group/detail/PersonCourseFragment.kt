package com.example.echo.group.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.echo.R
import com.example.echo.path.CourseList

class PersonCourseFragment : Fragment() {
    var courseList = ArrayList<CourseList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let{
            courseList = it.getSerializable("courseList") as ArrayList<CourseList>
            Log.d("test-번들1", courseList.toString())
        }

        Log.d("test-번들2", courseList.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person_course, container, false)
    }


}