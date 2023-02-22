package com.example.echo.group.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.board.BoardListVO
import com.example.echo.databinding.ActivityDetailPersonProfileBinding
import com.example.echo.myPage.MyPageVO
import com.example.echo.path.CourseList
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var boardList = ArrayList<BoardListVO>()
var courseList = ArrayList<CourseList>()
var personInfo = MyPageVO()

class DetailPersonProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPersonProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_person_profile)

        binding = ActivityDetailPersonProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgDetailPersonProfileBack.setOnClickListener {
            finish()
        }

        val user_nick = intent.getStringExtra("user_nick")
        val user_id = intent.getStringExtra("user_id")
        val user_profile_img = intent.getStringExtra("user_profile_img")
        val user_type = intent.getStringExtra("user_type")

        Log.d("값확인-비공개여부",user_type.toString())


        //비공개시 프로필 숨기기
        if(user_type != "n"){//프로필 비공개시
            binding.tvDetailPersonProfileGender.visibility=View.GONE
            binding.tvDetailPersonProfileBirth.visibility=View.GONE
            binding.imgCakeImage.visibility=View.GONE
            binding.flDetailPerson.visibility=View.GONE


            binding.imgProfileLock.visibility=View.VISIBLE
            binding.tvProfileLock.visibility=View.VISIBLE

        }else{//프로필 공개 시

            binding.tvDetailPersonProfileGender.visibility=View.VISIBLE
            binding.tvDetailPersonProfileBirth.visibility=View.VISIBLE
            binding.imgCakeImage.visibility=View.VISIBLE


            binding.imgProfileLock.visibility=View.GONE
            binding.tvProfileLock.visibility=View.GONE

            if (user_id != null) {
                getPersonProfile(user_id)
            }
        }



        binding.tvDetailPersonProfileNick.text = user_nick

        Glide.with(applicationContext)
            .load(user_profile_img)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.imgDetialPersonProfilePic)


        binding.bnvDetailPerson.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuBoard-> {
                    var personBoardFragment = PersonBoardFragment()
                    var bundle = Bundle()
                    bundle.putSerializable("boardList", boardList)
                    personBoardFragment.arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌
                    Log.d("test-번들보내기전", boardList.toString())
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flDetailPerson,
                        personBoardFragment
                    ).commit()
                }
                else -> {
                    var personCourseFragment = PersonCourseFragment()
                    var bundle = Bundle()

                    UserApiClient.instance.me { user, error ->
                        var loginUserId = user?.id.toString()

                        if(user_id!=loginUserId) {
                            var filterdCourseList = courseList.filter{
                                it.course_open == "y"
                            }
                            Log.d("test-filter", filterdCourseList.toString())
                            courseList = filterdCourseList as ArrayList<CourseList>
                        }

                    }

                    bundle.putSerializable("courseList", courseList)
                    personCourseFragment.arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌
                    Log.d("test-번들보내기전", courseList.toString())
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flDetailPerson,
                        personCourseFragment
                    ).commit()
                }
            }
            true
        }

    }

    fun getPersonProfile(user_id:String){
        boardList.clear()
        courseList.clear()
        val call = RetrofitBuilder.userApi.getPersonProfile(user_id)
        call.enqueue(object : Callback<PersonProfile> {
            override fun onResponse(call: Call<PersonProfile>, response: Response<PersonProfile>) {
                if(response.isSuccessful&& response.body()!=null){
                    var body : PersonProfile? = response.body()
                    Log.d("test-전부조회", body.toString())
                    personInfo = body!!.personInfo!!
                    boardList = (body!!.boardList as ArrayList<BoardListVO>?)!!
                    courseList = (body!!.courseList as ArrayList<CourseList>?)!!

                    boardList.reverse()
                    courseList.reverse()
                    
                    val year = personInfo.user_birthdate.subSequence(0,4)
                    val month = personInfo.user_birthdate.subSequence(4,6)
                    val day = personInfo.user_birthdate.subSequence(6,8)

                    binding.tvDetailPersonProfileGender.text = "(${personInfo.user_gender}성)"
                    binding.tvDetailPersonProfileBirth.text = "${year}년 ${month}월 ${day}일"

                    var personBoardFragment = PersonBoardFragment()
                    var bundle = Bundle()
                    bundle.putSerializable("boardList", boardList)
                    personBoardFragment.arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flDetailPerson,
                        personBoardFragment
                    ).commit()
                }
            }
            override fun onFailure(call: Call<PersonProfile>, t: Throwable) {
                Log.d("test-전부조회", t.localizedMessage)
            }
        })
    }



}