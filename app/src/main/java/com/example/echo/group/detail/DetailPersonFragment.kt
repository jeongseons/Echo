package com.example.echo.group.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailPersonFragment : Fragment() {

    lateinit var adapter: PersonAdapter
    lateinit var id: String
    var personList = ArrayList<PersonVO>()

    override fun onResume() {
        super.onResume()
        refresh()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_detail_person, container, false)
        val title = requireActivity().intent.getStringExtra("title")
        val seq = requireActivity().intent.getIntExtra("num", 0)
        val auth = requireActivity().intent.getStringExtra("auth")
        val rvPersonList = view.findViewById<RecyclerView>(R.id.rvPersonList)

        Log.d("title확인", title.toString())
        Log.d("seq확인", seq.toString())

        GetPerson(seq)

        adapter = PersonAdapter(requireContext(), personList, title!!, auth!!)
        //어댑터 리스트로 띄워졌을때 해당 액티비티로 이동해야함.
        rvPersonList.adapter = adapter
        rvPersonList.layoutManager = GridLayoutManager(requireContext(), 3)

        return view
    }

    fun GetPerson(seq: Int) {//그룹 리스트 - 스프링 통신
        val call = RetrofitBuilder.api.getPerson(seq)
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
                            personList.add(
                                PersonVO(
                                    response.body()!!.get(i).user_profile_img,
                                    response.body()!!.get(i).group_auth,
                                    response.body()!!.get(i).user_nick,
                                    response.body()!!.get(i).user_id,
                                    response.body()!!.get(i).user_type
                                )
                            )

                        }
                        //리스트 추가후 어댑터 새로고침 필수!
                        adapter.notifyDataSetChanged()

                    } else {// 가입한 그룹이 없을 때
                        Toast.makeText(context, "가입한 모임이 없습니다!", Toast.LENGTH_LONG)
                    }

                }
            }

            override fun onFailure(call: Call<List<PersonVO>>, t: Throwable) {
                Log.d("불러오기실패", t.localizedMessage)
            }

        })


    }

    private fun refresh() {
        val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        ft.detach(this).attach(this).commit()
    }

}