package com.example.echo.group.detail

import android.app.Activity
import android.app.AlertDialog
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.databinding.FragmentDetailSettingBinding
import com.example.echo.group.AddGroupActivity
import com.example.echo.group.EditGroupInfoActivity
import com.example.echo.group.JoinGroupVO
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailSettingFragment : Fragment() {

    lateinit var adapter: JoinListAdapter
    var joinList = ArrayList<PersonVO>()
    var user_id : String = ""
    var group_profile_img : String = ""

    lateinit var tvGroupSettingMax : TextView
    lateinit var tvGroupSettingType : TextView
    lateinit var tvGroupSettingTitle : TextView
    lateinit var tvGroupSettingArea : TextView
    lateinit var tvGroupSettingAge : TextView
    lateinit var tvGroupSettingLevel : TextView
    lateinit var tvGroupSettingGender : TextView
    lateinit var tvGroupSettingDetail : TextView
    lateinit var imgGroupSettingProfile : ImageView
    lateinit var textView44 : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        binding = FragmentDetailSettingBinding.inflate(layoutInflater, container, false)

        val view = inflater.inflate(R.layout.fragment_detail_setting, container, false)

        //모임장확인
        val groupAuth = requireActivity().intent.getStringExtra("auth")
        val groupTitle = requireActivity().intent.getStringExtra("title")
        val groupSeq = requireActivity().intent.getIntExtra("num", 0)

        GetSignUpList(groupSeq)

        tvGroupSettingTitle = view.findViewById<TextView>(R.id.tvGroupSettingTitle)
        imgGroupSettingProfile = view.findViewById(R.id.imgGroupSettingProfile)
        val tvGroupSettingEdit = view.findViewById<TextView>(R.id.tvGroupSettingEdit)
        tvGroupSettingMax = view.findViewById(R.id.tvGroupSettingMax)
        tvGroupSettingType = view.findViewById(R.id.tvGroupSettingType)
        tvGroupSettingArea = view.findViewById(R.id.tvGroupSettingArea)
        tvGroupSettingLevel = view.findViewById(R.id.tvGroupSettingLevel)
        tvGroupSettingGender = view.findViewById(R.id.tvGroupSettingGender)
        tvGroupSettingAge = view.findViewById(R.id.tvGroupSettingAge)
        tvGroupSettingDetail = view.findViewById(R.id.tvGroupSettingDetail)
        val rvGroupSettingJoinList = view.findViewById<RecyclerView>(R.id.rvGroupSettingJoinList)
        val tvGroupSettingDel = view.findViewById<TextView>(R.id.tvGroupSettingDel)
        val tvGroupSettingOut = view.findViewById<TextView>(R.id.tvGroupSettingOut)
        textView44 = view.findViewById(R.id.textView44)


        //모임명 셋팅
        tvGroupSettingTitle.setText(groupTitle)

        val textViewGroupSetting7 = view.findViewById<TextView>(R.id.textViewGroupSetting7)
        if(groupAuth=="y"){ //모임장만 출력
            textViewGroupSetting7.visibility=View.VISIBLE
            tvGroupSettingDel.visibility=View.VISIBLE // 모임 삭제
            tvGroupSettingOut.visibility=View.GONE // 모임 탈퇴
            tvGroupSettingEdit.visibility=View.VISIBLE

        }else{
            textViewGroupSetting7.visibility=View.GONE
            textView44.visibility=View.GONE
            tvGroupSettingDel.visibility=View.GONE

            tvGroupSettingOut.visibility=View.VISIBLE
            tvGroupSettingEdit.visibility=View.GONE

        }

        tvGroupSettingOut.setOnClickListener {
            Log.d("test-탈퇴", groupSeq.toString())
            Log.d("test-탈퇴", user_id)
            val dialog: AlertDialog.Builder = AlertDialog.Builder(
                context,
                android.R.style.ThemeOverlay_Material_Dialog_Alert
            )
            dialog.setMessage("정말 탈퇴하시겠습니까?")
                .setTitle("모임 탈퇴 ")
                .setPositiveButton("아니오") { dialog, which ->
                    Log.i("Dialog", "취소")
                }
                .setNeutralButton("예"
                ) { dialog, which ->
                    quitGroup(groupSeq, user_id)
                }
                .show()
        }

        tvGroupSettingDel.setOnClickListener {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(
                context,
                android.R.style.ThemeOverlay_Material_Dialog_Alert
            )
            dialog.setMessage("정말 삭제하시겠습니까?")
                .setTitle("모임 삭제 ")
                .setPositiveButton("아니오") { dialog, which ->
                    Log.i("Dialog", "취소")
                }
                .setNeutralButton("예"
                ) { dialog, which ->
                    deleteGroup(groupSeq)
                }
                .show()
        }

        tvGroupSettingEdit.setOnClickListener{
            //정보 수정 클릭시 정보 수정 페이지로 이동
            val intent = Intent(context, EditGroupInfoActivity::class.java)
            intent.putExtra("group_profile_img",tvGroupSettingTitle.text)
            intent.putExtra("group_profile_img", group_profile_img)
            intent.putExtra("user_max", tvGroupSettingMax.text)
            intent.putExtra("group_name", tvGroupSettingTitle.text)
            intent.putExtra("group_area", tvGroupSettingArea.text)
            intent.putExtra("group_age", tvGroupSettingAge.text)
            intent.putExtra("group_level", tvGroupSettingLevel.text )
            intent.putExtra("group_gender", tvGroupSettingGender.text)
            intent.putExtra("group_detail", tvGroupSettingDetail.text)
            intent.putExtra("group_seq", groupSeq)
            startActivity(intent)
        }

//        joinList.add(PersonVO("https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/2615613938.png?alt=media", "n", "test1"))
//        joinList.add(PersonVO("https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/2615613938.png?alt=media", "n", "test2"))
//        joinList.add(PersonVO("https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/2615613938.png?alt=media", "n", "test3"))


        //가입 신청 리스트 출력
        adapter = JoinListAdapter(requireContext(), joinList, groupAuth!!, groupSeq)
        rvGroupSettingJoinList.adapter = adapter
        rvGroupSettingJoinList.layoutManager = GridLayoutManager(requireContext(), 3)


        UserApiClient.instance.me { user, error ->
            user_id = user?.id.toString()
            joinGroupPro(groupSeq, user_id)
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
                                    response.body()!!.get(i).user_id,
                                   "n"
                                )
                            )

                            if(joinList.size==0){
                                textView44.visibility=View.VISIBLE
                            }else{
                                textView44.visibility=View.GONE
                            }
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

    fun quitGroup(groupSeq: Int, user_id:String) { //회원 탈퇴용
        val call = RetrofitBuilder.api.quitGroup(groupSeq,user_id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "정상적으로 탈퇴되었습니다",Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }else{
                Toast.makeText(
                    context, "다시 시도해주세요",
                    Toast.LENGTH_SHORT
                ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("실패", t.localizedMessage)
            }

        })
    }

    fun joinGroupPro(num:Int, id:String){ // 모임 정보 조회
        val call = RetrofitBuilder.api.joinGroupPro(num, id)
        call.enqueue(object : Callback<JoinGroupVO> {
            override fun onResponse(call: Call<JoinGroupVO>, response: Response<JoinGroupVO>) {
                if (response.isSuccessful) {
                    var body = response.body()!!
                    Glide.with(this@DetailSettingFragment)
                        .load(body.group_profile_img)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imgGroupSettingProfile)
                    tvGroupSettingMax.text = "(${body.group_current}/${body.user_max})"
                    tvGroupSettingType.text = body.group_type
                    tvGroupSettingTitle.text = body.group_name
                    tvGroupSettingArea.text = body.group_area
                    tvGroupSettingAge.text = body.group_age
                    tvGroupSettingLevel.text = body.group_level
                    tvGroupSettingGender.text = body.group_gender
                    tvGroupSettingDetail.text = body.group_detail
                    group_profile_img = body.group_profile_img

                }
            }
            override fun onFailure(call: Call<JoinGroupVO>, t: Throwable) {
                Log.d("실패", t.localizedMessage)
            }

        })
    }

    fun deleteGroup(groupSeq:Int){
        val call = RetrofitBuilder.api.deleteGroup(groupSeq)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "정상적으로 삭제되었습니다",Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(
                        context, "다시 시도해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("실패", t.localizedMessage)
            }

        })
    }

}