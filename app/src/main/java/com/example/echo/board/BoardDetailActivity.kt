package com.example.echo.board

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.databinding.ActivityBoardDetailBinding
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private lateinit var binding: ActivityBoardDetailBinding
class BoardDetailActivity : AppCompatActivity() {
    var loginUser = ""
    var board_seq = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        binding = ActivityBoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val board_seq = intent.getStringExtra("board_seq")?.toInt()
        val board_title = intent.getStringExtra("board_title")
        val board_content = intent.getStringExtra("board_content")
        val board_file = intent.getStringExtra("board_file")
        val user_nick = intent.getStringExtra("user_nick")
        var board_dt = intent.getStringExtra("board_dt")
        var user_id = intent.getStringExtra("user_id")
        var mnt_name = intent.getStringExtra("mnt_name")
        var board_reco_cnt = intent.getStringExtra("board_reco_cnt")

        binding.tvBoardDetailTitle.text = board_title
        binding.tvBoardDetailContent.text = board_content
        binding.tvBoardDetailUserNick.text = user_nick
        binding.tvBoardDetailDate.text = board_dt
        binding.tvBoardDetailMntName.text = mnt_name
        binding.tvBoardDetailRecoCnt.text = board_reco_cnt
        Glide.with(this)
            .load(board_file)
            .into(binding.imgBoardDetailPic) //지역변수

        // 작성자 확인
        UserApiClient.instance.me { user, error ->
            loginUser = user?.id.toString()
            if(user_id==loginUser){
                binding.tvBoardDetailModify.isVisible = true
                binding.tvBoardDetailDelete.isVisible = true
            }
        }

        // 글 수정
        binding.tvBoardDetailModify.setOnClickListener {

        }

        // 글 삭제
        binding.tvBoardDetailDelete.setOnClickListener {
            Log.d("test-삭제전", board_seq.toString())
            val dialog: AlertDialog.Builder = AlertDialog.Builder(
                this,
                android.R.style.Theme_DeviceDefault_Light_Dialog
            )
            dialog.setMessage("글을 삭제하시겠습니까?")
                .setTitle("글 삭제")
                .setPositiveButton("아니오", DialogInterface.OnClickListener { dialog, which ->
                    Log.i("Dialog", "취소")
                })
                .setNeutralButton("예",
                    DialogInterface.OnClickListener { dialog, which ->
                        deleteBoard(board_seq!!)
                    })
//                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show()
        }
    }

    fun deleteBoard(board_seq:Int){
        val call = RetrofitBuilder.boardApi.deleteBoard(board_seq)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Log.d("test-삭제후", response.body().toString())
                if(response.isSuccessful) {
                    Toast.makeText(
                        this@BoardDetailActivity, "정상적으로 삭제되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()

                }else{
                    Toast.makeText(
                        this@BoardDetailActivity, "다시 시도해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-가입실패", t.localizedMessage)
            }
        })

    }
}