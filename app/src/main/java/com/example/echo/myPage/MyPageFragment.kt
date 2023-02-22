package com.example.echo.myPage

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.auth.IntroActivity
import com.example.echo.databinding.FragmentMyPageBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.jar.Manifest


lateinit var binding: FragmentMyPageBinding
var user_id = ""
var user_profile_img = ""
var mainActivity: MainActivity = MainActivity()

private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

class MyPageFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = (activity as MainActivity?)!!
    }

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


        //프로필 비공개 표시
        binding.imgLockPro
        binding.tvLockPro

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        receiveLocation {  }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val id = sharedPreferences.getBoolean("pfPush", false)
        Log.d("test-shared", id.toString())

        binding.tvMyPageModify.setOnClickListener {
            val intent = Intent(context, ReviseActivity::class.java)
            intent.putExtra("user_id", user_id)
            intent.putExtra("user_nick", binding.tvMyPageNick.text)
            intent.putExtra("user_profile_img", user_profile_img)
            startActivity(intent)
        }

        binding.tvMyPageSetting.setOnClickListener {

            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        binding.tvMyPageCourseStrg.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("user_id", user_id)
            val myCourseFragment = MyCourseFragment()
            myCourseFragment.arguments = bundle
            myCourseFragment.setArguments(bundle)
//            mainActivity.changeFragment(1)
            mainActivity.supportFragmentManager.beginTransaction().replace(
                R.id.flMain,
                MyCourseFragment()
                    .apply {
                        arguments = Bundle().apply {
                            putString("user_id", user_id)
                        }
                    }
            ).commit()

        }

        binding.tvMyPageBoardStrg.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("user_id", user_id)
            val myBoardFragment = MyBoardFragment()
            myBoardFragment.arguments = bundle
            myBoardFragment.setArguments(bundle)
            mainActivity.supportFragmentManager.beginTransaction().replace(
                R.id.flMain,
                MyBoardFragment()
                    .apply {
                        arguments = Bundle().apply {
                            putString("user_id", user_id)
                        }
                    }
            ).commit()
//            val intent = Intent(context, MyBoardActivity::class.java)
//            intent.putExtra("user_id", user_id)
//            startActivity(intent)
        }

        //회원탈퇴
        binding.tvDeleteUser.setOnClickListener {
            Log.d("test-탈퇴전", user_id.toString())
            val dialog: AlertDialog.Builder = AlertDialog.Builder(
                requireContext(),
                android.R.style.ThemeOverlay_Material_Dialog_Alert
            )
            dialog.setMessage("정말 탈퇴하시겠습니까?\n" +
            "본인이 그룹장인 그룹이 있으면 탈퇴할 수 없습니다")
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

        binding.tvMyPageLogout.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e("Hello", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Log.i("Hello", "로그아웃 성공. SDK에서 토큰 삭제됨")
                    Toast.makeText(
                        requireContext(), "로그아웃 성공",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(requireContext(), IntroActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    requireActivity().finish()

                }
            }
        }

        return binding.root
    }

    fun getMyPage(user_id: String) {
        val call = RetrofitBuilder.userApi.getMyPage(user_id)
        call.enqueue(object : Callback<MyPageVO> {
            override fun onResponse(
                call: Call<MyPageVO>, response: Response<MyPageVO>,
            ) {
                if (response.isSuccessful) {
                    Log.d("text-마이페이지", "실행중")
                    var body = response.body()!!
                    user_profile_img = body.user_profile_img
                    Log.d("text-마이페이지", body.toString())
                    binding.tvMyPageNick.text = body.user_nick
                    binding.tvMyPageBirth.text =
                        "${body.user_birthdate.substring(0, 4)}년 ${
                            body.user_birthdate.substring(4,
                                6)
                        }월 " +
                                "${body.user_birthdate.substring(6, 8)}일"
                    Glide.with(context!!)
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

    fun deleteUser(user_id: String) {
        val call = RetrofitBuilder.userApi.deleteUser(user_id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>,
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
                } else {
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

    fun receiveLocation(block: (location: Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                block(location)
                Log.d("test-location", location.toString())
            }
    }

}