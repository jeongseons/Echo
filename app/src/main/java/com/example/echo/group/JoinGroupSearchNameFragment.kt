package com.example.echo.group

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.KeyEvent.KEYCODE_NUMPAD_ENTER
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinGroupSearchNameFragment : Fragment() {

    lateinit var adapter: JoinGroupAdapter
    var groupList = ArrayList<GroupVO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_join_group_search_name, container, false)
        val rvJoinGroupName = view.findViewById<RecyclerView>(R.id.rvJoinGroupName)
        val etJoinGroupNameSearch = view.findViewById<EditText>(R.id.etJoinGroupNameSearch)
        val imgJoinGroupNameSearch = view.findViewById<ImageView>(R.id.imgJoinGroupNameSearch)

        adapter = JoinGroupAdapter(requireContext(), groupList)
        //어댑터 리스트로 띄워졌을때 해당 액티비티로 이동해야함.
        rvJoinGroupName.adapter = adapter
        rvJoinGroupName.layoutManager = LinearLayoutManager(requireContext())

        //etJoinGroupNameSearch 에서 엔터키 클릭 시 값 보내기
        etJoinGroupNameSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                val searchName = etJoinGroupNameSearch.text.toString()
                //엔터키 입력시 이벤트 처리 구간
                Log.d("값확인-검색 모임 이름",searchName)
                JoinGroupNick(searchName)
            }

            true
        }


        //돋보기 이미지 클릭 시 값 보내기
        imgJoinGroupNameSearch.setOnClickListener{
            val searchName = etJoinGroupNameSearch.text.toString()

            Log.d("값확인-검색 모임 이름",searchName)
            JoinGroupNick(searchName)
        }

        return view
    }
    fun JoinGroupNick(Nick: String) {//그룹 조건검색 리스트 - 스프링 통신
        val call = RetrofitBuilder.api.joinGroupNick(Nick)
        call.enqueue(object : Callback<List<GroupVO>> {
            override fun onResponse(
                call: Call<List<GroupVO>>,
                response: Response<List<GroupVO>>
            ) {
                if (response.isSuccessful) {//성공
                    Log.d("rty",response.body().toString())
                    groupList.clear()
                    if(response.body()?.size!=0) {//가입한 그룹이 있을 때
                        for (i: Int in 0 until response.body()!!.size) {
                            //그룹리스트 정보 담아줌.
                            groupList.add(
                                GroupVO(
                                    response.body()!!.get(i).group_seq,
                                    response.body()!!.get(i).group_profile_img,
                                    "n",
                                    response.body()!!.get(i).group_name,
                                    response.body()!!.get(i).user_max,
                                    response.body()!!.get(i).group_current
                                )
                            )

                        }
                        //리스트 추가후 어댑터 새로고침 필수!
                        // 근본적인 원인 : API 호출 이후 새로고침이 일어나야 하는데
                        // 새로고침이 일어난 이후에 데이터가 쌓임
                        adapter.notifyDataSetChanged()

                    }
                    else{// 가입한 그룹이 없을 때
                        Toast.makeText(context,"해당 회원이 생성한 모임이 없습니다!", Toast.LENGTH_LONG)
                    }

                }
            }

            override fun onFailure(call: Call<List<GroupVO>>, t: Throwable) {
                Log.d("반환값 에러", "?")
            }


        })
    }//fun 끝
}