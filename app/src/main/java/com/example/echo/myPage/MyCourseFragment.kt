package com.example.echo.myPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echo.MainActivity
import com.example.echo.RetrofitBuilder
import com.example.echo.databinding.FragmentMyCourseBinding
import com.example.echo.path.CourseInfo
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCourseFragment : Fragment() {

    var myCourseList = ArrayList<CourseInfo>()
    lateinit var binding: FragmentMyCourseBinding
    lateinit var adapter: MyCourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var user_id = arguments?.getString("user_id").toString()
        Log.d("test", user_id)
        getMyCourse(user_id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentMyCourseBinding.inflate(layoutInflater, container, false)
        adapter = MyCourseAdapter(requireContext(), myCourseList)
        binding.rvMyCourse.adapter = adapter
        binding.rvMyCourse.layoutManager = LinearLayoutManager(context)


        // 경로 내부로 이동
        adapter.setOnItemClickListener(object : MyCourseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(context, CourseDetailActivity::class.java)
                intent.putExtra("course_seq", myCourseList[position].course_seq)
                intent.putExtra("course_title", myCourseList[position].course_title)
                intent.putExtra("course_time", myCourseList[position].course_time)
                intent.putExtra("course_alt", myCourseList[position].course_alt)
                intent.putExtra("course_distance", myCourseList[position].course_distance)
                intent.putExtra("course_start_dt", myCourseList[position].course_start_dt)
                intent.putExtra("course_end_dt", myCourseList[position].course_end_dt)
                intent.putExtra("course_open", myCourseList[position].course_open)
                intent.putExtra("course_user_id", myCourseList[position].user_id)
                intent.putExtra("course_img",myCourseList[position].course_img)
                startActivity(intent)
            }
        })

        binding.tvMyCourseDelete.setOnClickListener {
            deleteSelectedCourse(adapter.selectDelete())
        }

        binding.tvMyCourseSelect.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                adapter.setCheckAll()
                adapter.notifyDataSetChanged()
            }
        })

//        return inflater.inflate(R.layout.fragment_my_course, container, false)
        return binding.root

    }

    fun getMyCourse(user_id:String) {
        myCourseList.clear()
        val call = RetrofitBuilder.courseApi.getCourse(user_id)
        call.enqueue(object : Callback<List<CourseInfo>> {
            override fun onResponse(call: Call<List<CourseInfo>>, response: Response<List<CourseInfo>>) {
                if(response.isSuccessful&& response.body()?.size!!>0){
                    for(i in 0 until response.body()!!.size){
                        myCourseList.add(response.body()!!.get(i))
                    }
                }
                myCourseList.reverse()
                adapter.notifyDataSetChanged()
                Log.d("test-전부조회",myCourseList.toString())
            }
            override fun onFailure(call: Call<List<CourseInfo>>, t: Throwable) {
                Log.d("test-전부조회", t.localizedMessage)

            }
        })
    }

    fun deleteSelectedCourse(courseSeqList: ArrayList<Int>) {

    }


}