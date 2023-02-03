package com.example.echo.group.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.group.AddGroupActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailSettingFragment : Fragment() {

    lateinit var adapter: JoinListAdapter
    var joinList = ArrayList<PersonVO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_setting, container, false)

        //모임장확인
        val groupAuth = requireActivity().intent.getStringExtra("auth")
        val groupTitle = requireActivity().intent.getStringExtra("title")
        val groupSeq = requireActivity().intent.getIntExtra("num", 0)

        GetSignUpList(groupSeq)

        val tvGroupSettingTitle = view.findViewById<TextView>(R.id.tvGroupSettingTitle)
        val imgGroupSettingProfile = view.findViewById<ImageView>(R.id.imgGroupSettingProfile)
        val tvGroupSettingEdit = view.findViewById<TextView>(R.id.tvGroupSettingEdit)
        val tvGroupSettingMax = view.findViewById<TextView>(R.id.tvGroupSettingMax)
        val tvGroupSettingType = view.findViewById<TextView>(R.id.tvGroupSettingType)
        val tvGroupSettingArea = view.findViewById<TextView>(R.id.tvGroupSettingArea)
        val tvGroupSettingLevel = view.findViewById<TextView>(R.id.tvGroupSettingLevel)
        val tvGroupSettingGender = view.findViewById<TextView>(R.id.tvGroupSettingGender)
        val tvGroupSettingAge = view.findViewById<TextView>(R.id.tvGroupSettingAge)
        val tvGroupSettingDetail = view.findViewById<TextView>(R.id.tvGroupSettingDetail)
        val rvGroupSettingJoinList = view.findViewById<RecyclerView>(R.id.rvGroupSettingJoinList)
        val tvGroupSettingDel = view.findViewById<TextView>(R.id.tvGroupSettingDel)
        val tvGroupSettingOut = view.findViewById<TextView>(R.id.tvGroupSettingOut)


        //모임명 셋팅
        tvGroupSettingTitle.setText(groupTitle)

        val textViewGroupSetting7 = view.findViewById<TextView>(R.id.textViewGroupSetting7)
        if(groupAuth=="y"){ //모임장만 출력
            textViewGroupSetting7.visibility=View.VISIBLE
            tvGroupSettingDel.visibility=View.VISIBLE

            tvGroupSettingOut.visibility=View.GONE
        }else{
            textViewGroupSetting7.visibility=View.GONE
            tvGroupSettingDel.visibility=View.GONE

            tvGroupSettingOut.visibility=View.VISIBLE
        }
        


//        joinList.add(PersonVO("https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/2615613938.png?alt=media", "n", "test1"))
//        joinList.add(PersonVO("https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/2615613938.png?alt=media", "n", "test2"))
//        joinList.add(PersonVO("https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/2615613938.png?alt=media", "n", "test3"))


        //가입 신청 리스트 출력
        adapter = JoinListAdapter(requireContext(), joinList, groupAuth!!, groupSeq)
        rvGroupSettingJoinList.adapter = adapter
        rvGroupSettingJoinList.layoutManager = GridLayoutManager(requireContext(), 3)

        if(groupAuth.equals("y")){
            tvGroupSettingEdit.visibility=View.VISIBLE
            tvGroupSettingEdit.setOnClickListener{
                //정보 수정 클릭시 정보 수정 페이지로 이동
            }
        }else{
            tvGroupSettingEdit.visibility=View.GONE
        }

        return view
    }

    fun GetSignUpList(num: Int) {//가입 대기중인 인원 리스트 - 스프링 통신
        val call = RetrofitBuilder.api.getSignUpList(num)
        call.enqueue(object : Callback<List<PersonVO>> {
            override fun onResponse(
                call: Call<List<PersonVO>>,
                response: Response<List<PersonVO>>
            ) {
                if (response.isSuccessful) {//성공
                    Log.d("zxc", response.body().toString())
                    if (response.body()?.size != 0) {//가입한 회원이 있을 때
                        for (i: Int in 0 until response.body()!!.size) {
                            //회원리스트 정보 담아줌.
                            joinList.add(
                                PersonVO(
                                    response.body()!!.get(i).user_profile_img,
                                    response.body()!!.get(i).group_auth,
                                    response.body()!!.get(i).user_nick,
                                )
                            )
                        }
                        //리스트 추가후 어댑터 새로고침 필수!
                        adapter.notifyDataSetChanged()

                    } else {// 가입한 그룹이 없을 때
                        Toast.makeText(context, "가입한 신청자가 없습니다.", Toast.LENGTH_LONG)
                    }

                }
            }

            override fun onFailure(call: Call<List<PersonVO>>, t: Throwable) {
                Log.d("불러오기실패", t.localizedMessage)
            }

        })

    }

    fun GroupDegree(num:Int, nick:String) { //회원 탈퇴용
        val call = RetrofitBuilder.api.groupDegree(num, nick)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("실패", t.localizedMessage)
            }

        })
    }

}