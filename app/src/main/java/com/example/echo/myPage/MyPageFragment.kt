package com.example.echo.myPage

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.auth.IntroActivity
import com.example.echo.board.BoardWriteActivity
import com.example.echo.databinding.FragmentMyPageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

lateinit var binding: FragmentMyPageBinding
var user_id = ""
var user_profile_img = ""
class MyPageFragment : Fragment() {

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View
    ? {

        binding = FragmentMyPageBinding.inflate(layoutInflater, container, false)
        binding.imgMyPagePic.setImageResource(R.drawable.p1)
        UserApiClient.instance.me { user, error ->
                user_id = user?.id.toString()
                getMyPage(user_id)
        }

        val storageReference = Firebase.storage.reference.child("$user_id.png")

//        storageReference.downloadUrl.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Glide.with(this)
//                    .load(task.result)
//                    .into(binding.imgMyPagePic)
//            }
//        }

        binding.tvMyPageModify.setOnClickListener {
            val intent = Intent(context, ReviseActivity::class.java)
            intent.putExtra("user_id", user_id)
            intent.putExtra("user_nick", binding.tvMyPageNick.text)
            intent.putExtra("user_profile_img", user_profile_img)
            startActivity(intent)
        }

        binding.tvMyPageCourseStrg.setOnClickListener {

        }

        binding.tvMyPageBoardStrg.setOnClickListener {
            val intent = Intent(context, MyBoardActivity::class.java)
            intent.putExtra("user_id", user_id)
            startActivity(intent)
        }

        //회원탈퇴
        binding.tvDeleteUser.setOnClickListener {
            Log.d("test-탈퇴전", user_id.toString())
            val dialog: AlertDialog.Builder = AlertDialog.Builder(
                requireContext(),
                android.R.style.Theme_DeviceDefault_Light_Dialog
            )
            dialog.setMessage("정말 탈퇴하시겠습니까?")
                .setTitle("회원 탈퇴")
                .setPositiveButton("아니오", DialogInterface.OnClickListener { dialog, which ->
                    Log.i("Dialog", "취소")
                })
                .setNeutralButton("예",
                    DialogInterface.OnClickListener { dialog, which ->
                        deleteUser(user_id)
                    })
                .show()
        }

        return binding.root
    }

    fun getMyPage(user_id:String){
        val call = RetrofitBuilder.userApi.getMyPage(user_id)
        call.enqueue(object : Callback<MyPageVO> {
            override fun onResponse(call: Call<MyPageVO>, response: Response<MyPageVO>
            ) {
                if (response.isSuccessful) {
                    Log.d("text-마이페이지","실행중")
                    var body = response.body()!!
                    user_profile_img = body.user_profile_img
                    Log.d("text-마이페이지",body.toString())
                    binding.tvMyPageNick.text = body.user_nick
                    binding.tvMyPageBirth.text =
                        "${body.user_birthdate.substring(0,4)}년 ${body.user_birthdate.substring(4,6)}월 " +
                                "${body.user_birthdate.substring(6,8)}일"
                    Glide.with(requireContext())
                        .load(user_profile_img)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.imgMyPagePic)
                    binding.tvMyPageBoardCnt.text = body.user_board_cnt
                    binding.tvMyPageCourseCnt.text = body.user_course_cnt
                }
            }
            override fun onFailure(call: Call<MyPageVO>, t: Throwable) {
            }
        })
    }

    fun deleteUser(user_id:String) {
        val call = RetrofitBuilder.userApi.deleteUser(user_id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.d("test-삭제후", response.body().toString())
                        UserApiClient.instance.unlink { }
                        Toast.makeText(
                            requireContext(), "정상적으로 탈퇴되었습니다",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(requireContext(), IntroActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            requireContext(), "다시 시도해주세요",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }



}