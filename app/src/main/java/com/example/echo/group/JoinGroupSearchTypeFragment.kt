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

        JoinGroupList()

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
        //????????? ???????????? ??????????????? ?????? ??????????????? ???????????????.
        rvJoinGroupType.adapter = adapter
        rvJoinGroupType.layoutManager = LinearLayoutManager(requireContext())

        btnJoinGroupTypeFind.setOnClickListener {
            val group_age = spJoinGroupTypeAge.selectedItem.toString()
            val group_type = spJoinGroupTypeType.selectedItem.toString()
            val group_gender = spJoinGroupTypeGender.selectedItem.toString()
            val group_area = spJoinGroupTypeArea.selectedItem.toString()
            val group_level = spJoinGroupTypeLevel.selectedItem.toString()

            val searchType = FindGroupVO(group_type,group_age,group_gender,group_level,group_area)
            Log.d("?????????-???????????? ??????",searchType.toString())

            JoinGroupCon(searchType)
        }

        return view

    }
    fun JoinGroupList(){
        val call = RetrofitBuilder.api.joinGroupList()
        call.enqueue(object : Callback<List<GroupVO>> {
            override fun onResponse(
                call: Call<List<GroupVO>>,
                response: Response<List<GroupVO>>
            ) {
                if (response.isSuccessful) {//??????
                    Log.d("rty",response.body().toString())
                    groupList.clear()
                    if(response.body()?.size!=0) {//????????? ????????? ?????? ???
                        for (i: Int in 0 until response.body()!!.size) {
                            //??????????????? ?????? ?????????.
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
                        //????????? ????????? ????????? ???????????? ??????!
                        // ???????????? ?????? : API ?????? ?????? ??????????????? ???????????? ?????????
                        // ??????????????? ????????? ????????? ???????????? ??????
                        adapter.notifyDataSetChanged()

                    }
                    else{// ????????? ????????? ?????? ???
                        Toast.makeText(context,"????????? ?????? ????????? ????????????!", Toast.LENGTH_LONG)
                    }

                }
            }

            override fun onFailure(call: Call<List<GroupVO>>, t: Throwable) {
                Log.d("????????? ??????", "?")
            }


        })
    }

    fun JoinGroupCon(Condition: FindGroupVO) {//?????? ???????????? ????????? - ????????? ??????
        val call = RetrofitBuilder.api.joinGroupCon(Condition)
        call.enqueue(object : Callback<List<GroupVO>> {
            override fun onResponse(
                call: Call<List<GroupVO>>,
                response: Response<List<GroupVO>>
            ) {
                if (response.isSuccessful) {//??????
                    Log.d("rty",response.body().toString())
                    groupList.clear()
                    if(response.body()?.size!=0) {//????????? ????????? ?????? ???
                        for (i: Int in 0 until response.body()!!.size) {
                            //??????????????? ?????? ?????????.
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
                        //????????? ????????? ????????? ???????????? ??????!
                        // ???????????? ?????? : API ?????? ?????? ??????????????? ???????????? ?????????
                        // ??????????????? ????????? ????????? ???????????? ??????
                        adapter.notifyDataSetChanged()

                    }
                    else{// ????????? ????????? ?????? ???
                        groupList.clear()
                        Toast.makeText(context,"????????? ?????? ????????? ????????????!", Toast.LENGTH_LONG)
                        adapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onFailure(call: Call<List<GroupVO>>, t: Throwable) {
                Log.d("????????? ??????", "?")
            }


        })
    }//fun ???

}