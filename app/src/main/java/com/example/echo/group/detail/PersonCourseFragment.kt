package com.example.echo.group.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.myPage.CourseDetailActivity
import com.example.echo.path.CourseList

class PersonCourseFragment : Fragment() {
    var courseList = ArrayList<CourseList>()
    lateinit var adapter: PersonCourseAdapter

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
        val view = inflater.inflate(R.layout.fragment_person_course, container, false)

        val rvPersonCourse = view.findViewById<RecyclerView>(R.id.rvPersonCourse)
        var tvPersonCourseText = view.findViewById<TextView>(R.id.tvPersonCourseText)

        tvPersonCourseText.text = "총 ${courseList.size}개의 경로가 있습니다다"

        adapter = PersonCourseAdapter(requireContext(), courseList)

        rvPersonCourse.adapter = adapter
        rvPersonCourse.layoutManager = LinearLayoutManager(requireContext())
        adapter.notifyDataSetChanged()

        // 경로 내부로 이동
        adapter.setOnItemClickListener(object : PersonCourseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(context, CourseDetailActivity::class.java)
                intent.putExtra("course_seq", courseList[position].course_seq)
                intent.putExtra("course_title", courseList[position].course_title)
                intent.putExtra("course_time", courseList[position].course_time)
                intent.putExtra("course_alt", courseList[position].course_alt)
                intent.putExtra("course_distance", courseList[position].course_distance)
                intent.putExtra("course_start_dt", courseList[position].course_start_dt)
                intent.putExtra("course_end_dt", courseList[position].course_end_dt)
                intent.putExtra("course_open", courseList[position].course_open)
                intent.putExtra("course_user_id", courseList[position].user_id)
                intent.putExtra("course_img", courseList[position].course_img)
                startActivity(intent)
            }
        })

        return view
    }


}