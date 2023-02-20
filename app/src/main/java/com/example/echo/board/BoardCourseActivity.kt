package com.example.echo.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.group.detail.PersonCourseAdapter
import com.example.echo.myPage.CourseDetailActivity
import com.example.echo.myPage.MyCourseAdapter
import com.example.echo.myPage.user_id
import com.example.echo.path.CourseList
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardCourseActivity : AppCompatActivity() {
    var courseList = ArrayList<CourseList>()
    var user_id = ""
    lateinit var adapter: PersonCourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_course)

        UserApiClient.instance.me { user, error ->
            user_id = user?.id.toString()
            getMyCourse(user_id)
        }

        val rvBoardCourse = findViewById<RecyclerView>(R.id.rvBoardCourse)

        adapter = PersonCourseAdapter(this, courseList)

        rvBoardCourse.adapter = adapter
        rvBoardCourse.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()


        // 경로 내부로 이동
        adapter.setOnItemClickListener(object : PersonCourseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@BoardCourseActivity, BoardWriteActivity::class.java)
                intent.putExtra("course_seq", courseList[position].course_seq)
                intent.putExtra("course_img",courseList[position].course_img)
                setResult(RESULT_OK, intent)
                finish()
            }
        })

    }

    fun getMyCourse(user_id:String) {
        courseList.clear()
        val call = RetrofitBuilder.courseApi.getCourse(user_id)
        call.enqueue(object : Callback<List<CourseList>> {
            override fun onResponse(call: Call<List<CourseList>>, response: Response<List<CourseList>>) {
                if(response.isSuccessful&& response.body()?.size!!>0){
                    for(i in 0 until response.body()!!.size){
                        courseList.add(response.body()!!.get(i))
                    }
                }
                courseList.reverse()
                adapter.notifyDataSetChanged()
                Log.d("test-전부조회",courseList.toString())
            }
            override fun onFailure(call: Call<List<CourseList>>, t: Throwable) {
                Log.d("test-전부조회", t.localizedMessage)

            }
        })
    }
}