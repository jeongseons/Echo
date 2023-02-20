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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.MainActivity
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
    var board_file = ""
    var course_img = ""
    var modifyCk = ""
    var fileCk = false
    var board_seq = 0
    var course_seq = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBoardWriteMpveBack.setOnClickListener {
            finish()
        }

        UserApiClient.instance.me { user, error ->
            user_id = user?.id.toString()
        }

        binding.btnBoardWritePic.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            launcher.launch(intent)
            Log.d("test-이미지업로드", binding.imgBoardWritePic.drawable.toString())
        }

        binding.btnBoardWriteCourse.setOnClickListener {
            val intent = Intent(this, BoardCourseActivity::class.java)
            courseLauncher.launch(intent)
            Log.d("test-이미지업로드", binding.imgBoardWriteRoute.drawable.toString())
        }

        // 글 수정시 내용 불러오기
        modifyCk = intent.getStringExtra("modifyCk").toString()
        if(modifyCk=="true") {

            board_seq = intent.getIntExtra("board_seq", 0)
            val board_title = intent.getStringExtra("board_title")
            val board_content = intent.getStringExtra("board_content")
            board_file = intent.getStringExtra("board_file").toString()
            val user_nick = intent.getStringExtra("user_nick")
            var board_dt = intent.getStringExtra("board_dt")
            var user_id = intent.getStringExtra("user_id")
            var mnt_name = intent.getStringExtra("mnt_name")
            course_seq = intent.getIntExtra("course_seq", 0)
            course_img = intent.getStringExtra("course_img").toString()
            binding.tvBoardWriteLine.text = "게시글 수정"
            binding.btnBoardWrtePost.text = "게시글 수정"
            binding.etBoardWriteTitle.setText(board_title)
            binding.etBoardWrtieContent.setText(board_content)
            binding.etBoardWriteMnt.setText(mnt_name)
            if (board_file!!.isNotEmpty()) {
                Glide.with(this)
                    .load(board_file)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.imgBoardWritePic)
            }
            if (course_img!!.isNotEmpty()) {
                Glide.with(this)
                    .load(course_img)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.imgBoardWriteRoute)
            }
        }

        // 글 등록
        binding.btnBoardWrtePost.setOnClickListener {
            var emptyCk = true

            var board_title = binding.etBoardWriteTitle.text.toString()
            var board_content = binding.etBoardWrtieContent.text.toString()
            var mnt_name = binding.etBoardWriteMnt.text.toString()

            if(fileCk||modifyCk == "true" && board_file.isNotEmpty()){
            var key = "${user_id}${mnt_name}${board_title}${currentTimeMillis()}"
            imgUpload(key)
                board_file = "https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/${key}.png?alt=media"
            }

            if(board_title.isEmpty()){
                emptyCk = false
                Toast.makeText(this,"제목을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            if(board_content.isEmpty()){
                emptyCk = false
                Toast.makeText(this,"내용을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            if(mnt_name.isEmpty()){
                emptyCk = false
                Toast.makeText(this,"산 이름을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            if(mnt_name.substring(mnt_name.length-1)!="산"){
                emptyCk = false
                Toast.makeText(this,"산 이름을 'OO산'형식으로 정확히 입력해주세요",Toast.LENGTH_SHORT).show()
            }

                // 글 수정 경우
                if (emptyCk && modifyCk == "true") {
                    var board = BoardVO(
                        board_seq,
                        board_title,
                        board_content,
                        board_file,
                        null,
                        user_id,
                        mnt_name,
                        course_seq
                    )
                    Log.d("test-글수정시", board.toString())
                    modifyBoard(board)
                } else if (emptyCk && modifyCk != "true"){
                    var board = BoardVO(
                        null,
                        board_title,
                        board_content,
                        board_file,
                        null,
                        user_id,
                        mnt_name,
                        course_seq
                    )
                    Log.d("test-글작성시", board.toString())
                    addBoard(board)
                }
            }

    }
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //받아올 결과값이 맞는지 확인 과정
        if (it.resultCode == RESULT_OK) binding.imgBoardWritePic.setImageURI(it.data?.data)
        if (it.data?.data.toString()!=null) fileCk = true
    }

    val courseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val intent = it.data
            course_seq = intent!!.getIntExtra("course_seq", 0)
            val course_img = intent!!.getStringExtra("course_img")
            Log.d("test-런처", course_seq.toString())
            Log.d("test-런처", course_img!!)
            Glide.with(this)
                .load(course_img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imgBoardWriteRoute)
        }
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
                Log.d("test-글등록실패", t.localizedMessage)
            }
        })
    }

    fun modifyBoard(board: BoardVO){
        val call = RetrofitBuilder.boardApi.modifyBoard(board)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Toast.makeText(
                    this@BoardWriteActivity, "수정되었습니다",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-글수정실패", t.localizedMessage)
            }
        })
    }

}