package com.example.echo.board

import android.app.FragmentTransaction
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.echo.RetrofitBuilder
import com.example.echo.databinding.ActivityBoardWriteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.lang.System.currentTimeMillis

private lateinit var binding: ActivityBoardWriteBinding
class BoardWriteActivity : AppCompatActivity() {
    var user_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserApiClient.instance.me { user, error ->
            user_id = user?.id.toString()
        }

        binding.imgBoardWritePic.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            launcher.launch(intent)
        }

        binding.btnBoardWrtePost.setOnClickListener {

            var board_title = binding.etBoardWriteTitle.text.toString()
            var board_content = binding.etBoardWrtieContent.text.toString()
            var mnt_name = binding.etBoardWriteMnt.text.toString()
            var key = "${user_id}${mnt_name}${board_title}${currentTimeMillis()}"
            imgUpload(key)
            var board_file = "https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/${key}.png?alt=media"

            var board = BoardVO(null,board_title,board_content,board_file,null,user_id,mnt_name)
            Log.d("test-글작성시", board.toString())
            addBoard(board)

        }

        // 글 수정 경우
        val modifyCk = intent.getStringExtra("modifyCk")
//        if(modifyCk=="true"){
//            val board_seq = intent.getStringExtra("boardVO")!!.toInt()
//            val board_title = intent.getStringExtra("board_title")
//            val board_content = intent.getStringExtra("board_content")
//            val board_file = intent.getStringExtra("board_file")
//            val user_nick = intent.getStringExtra("user_nick")
//            var board_dt = intent.getStringExtra("board_dt")
//            var user_id = intent.getStringExtra("user_id")
//            var mnt_name = intent.getStringExtra("mnt_name")
//            var board_reco_cnt = intent.getStringExtra("board_reco_cnt")
//
//            binding.etBoardWriteTitle.setText(board_title)
//            binding.etBoardWrtieContent.setText(board_content)
//            binding.etBoardWriteMnt.setText(mnt_name)
//            Glide.with(this)
//                .load(board_file)
//                .into(binding.imgBoardWritePic) //지역변수
//        }

    }
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //받아올 결과값이 맞는지 확인 과정
        if (it.resultCode == RESULT_OK) binding.imgBoardWritePic.setImageURI(it.data?.data)
    }

    fun imgUpload(key : String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        binding.imgBoardWritePic.isDrawingCacheEnabled = true
        binding.imgBoardWritePic.buildDrawingCache()
        val bitmap = (binding.imgBoardWritePic.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        //quality:압축 퀄리티 1~100.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        //mountainsRef : 스토리지 경로 지정하는 키워드.
        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

//    정선씨,.. 파이팅 하세요....
    // 전 양심이 있어서 주석으로 달았습니다...

    fun addBoard(board : BoardVO){
        val call = RetrofitBuilder.boardApi.addBoard(board)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Toast.makeText(
                    this@BoardWriteActivity, "등록되었습니다",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-가입실패", t.localizedMessage)
            }
        })
    }

}