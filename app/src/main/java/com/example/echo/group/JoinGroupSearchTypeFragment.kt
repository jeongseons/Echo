package com.example.echo.group

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinGroupSearchTypeFragment : Fragment() {
    lateinit var adapter: JoinGroupAdapter
    var groupList = ArrayList<GroupVO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_join_group_search_type, container, false)


        val spJoinGroupTypeAge = view.findViewById<Spinner>(R.id.spJoinGroupTypeAge)
        val spJoinGroupTypeType = view.findViewById<Spinner>(R.id.spJoinGroupTypeType)
        val spJoinGroupTypeGender = view.findViewById<Spinner>(R.id.spJoinGroupTypeGender)
        val spJoinGroupTypeArea = view.findViewById<Spinner>(R.id.spJoinGroupTypeArea)
        val spJoinGroupTypeLevel = view.findViewById<Spinner>(R.id.spJoinGroupTypeLevel)
        val btnJoinGroupTypeFind = view.findViewById<Button>(R.id.btnJoinGroupTypeFind)
        val rvJoinGroupType = view.findViewById<RecyclerView>(R.id.rvJoinGroupType)


        spJoinGroupTypeAge.adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.age,
            android.R.layout.simple_spinner_dropdown_item)

        spJoinGroupTypeType.adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.type,
            android.R.layout.simple_spinner_dropdown_item)

        spJoinGroupTypeGender.adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.gender,
            android.R.layout.simple_spinner_dropdown_item)

        spJoinGroupTypeArea.adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.area,
            android.R.layout.simple_spinner_dropdown_item)

        spJoinGroupTypeLevel.adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.level_filter,
            android.R.layout.simple_spinner_dropdown_item)

        adapter = JoinGroupAdapter(requireContext(), groupList)
        //어댑터 리스트로 띄워졌을때 해당 액티비티로 이동해야함.
        rvJoinGroupType.adapter = adapter
        rvJoinGroupType.layoutManager = LinearLayoutManager(requireContext())

        btnJoinGroupTypeFind.setOnClickListener {
            val group_age = spJoinGroupTypeAge.selectedItem.toString()
            val group_type = spJoinGroupTypeType.selectedItem.toString()
            val group_gender = spJoinGroupTypeGender.selectedItem.toString()
            val group_area = spJoinGroupTypeArea.selectedItem.toString()
            val group_level = spJoinGroupTypeLevel.selectedItem.toString()

            val searchType = FindGroupVO(group_type,group_age,group_gender,group_level,group_area)
            Log.d("값확인-조건으로 검색",searchType.toString())

            JoinGroupCon(searchType)
        }

        return view

    }
    fun JoinGroupCon(Condition: FindGroupVO) {//그룹 조건검색 리스트 - 스프링 통신
        val call = RetrofitBuilder.api.joinGroupCon(Condition)
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
                        Toast.makeText(context,"조건에 맞는 모임이 없습니다!", Toast.LENGTH_LONG)
                    }

                }
            }

            override fun onFailure(call: Call<List<GroupVO>>, t: Throwable) {
                Log.d("반환값 에러", "?")
            }


        })
    }//fun 끝

}