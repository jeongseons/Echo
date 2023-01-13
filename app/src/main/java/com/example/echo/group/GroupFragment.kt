package com.example.echo.group

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.ResponseDTO
import com.example.echo.RetrofitBuilder
import com.example.echo.group.detail.PersonVO
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.acl.Group

class GroupFragment : Fragment() {

    lateinit var adapter: GroupListAdapter
    var id: String = ""

    var groupList = ArrayList<GroupVO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)

        val rvGroupList = view.findViewById<RecyclerView>(R.id.rvGroupList)
        val btnAddGroup = view.findViewById<Button>(R.id.btnAddGroup)
        val btnEnterGroup = view.findViewById<Button>(R.id.btnEnterGroup)


        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                id = user.id.toString()
                Log.d("id",id )
                GetGroup(id)
            }
        }


        adapter = GroupListAdapter(requireContext(), groupList)
        //어댑터 리스트로 띄워졌을때 해당 액티비티로 이동해야함.
        rvGroupList.adapter = adapter
        rvGroupList.layoutManager = LinearLayoutManager(requireContext())

        btnAddGroup.setOnClickListener {
            val intent = Intent(context, AddGroupActivity::class.java)
            intent.putExtra("user", id)
            startActivity(intent)
        }

        return view
    }

    fun GetGroup(id: String) {//그룹 리스트 - 스프링 통신
        val call = RetrofitBuilder.api.getGroup(id)
        call.enqueue(object : Callback<List<GroupVO>> {
            override fun onResponse(call: Call<List<GroupVO>>, response: Response<List<GroupVO>>) {
                if (response.isSuccessful) {//성공
                    Log.d("rty",response.body().toString())
                    if(response.body()?.size!=0) {//가입한 그룹이 있을 때
                        for (i: Int in 0 until response.body()!!.size) {
                            //그룹리스트 정보 담아줌.
                            groupList.add(
                                GroupVO(
                                    response.body()!!.get(i).group_seq,
                                    response.body()!!.get(i).group_profile_img,
                                    response.body()!!.get(i).group_auth,
                                    response.body()!!.get(i).group_name,
                                    response.body()!!.get(i).user_max,
                                    response.body()!!.get(i).group_current
                                )
                            )
                        }
                        //리스트 추가후 어댑터 새로고침 필수!
                        adapter.notifyDataSetChanged()
                    }
                    else{// 가입한 그룹이 없을 때
                        Toast.makeText(context,"가입한 모임이 없습니다!",Toast.LENGTH_LONG)
                    }

                }
            }

            override fun onFailure(call: Call<List<GroupVO>>, t: Throwable) {
                Log.d("불러오기실패", t.localizedMessage)
            }

        })
    }
}