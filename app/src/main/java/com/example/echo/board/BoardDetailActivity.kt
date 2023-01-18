package com.example.echo.board

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.board.social.CmtListVO
import com.example.echo.board.social.CommentAdapter
import com.example.echo.board.social.CommentVO
import com.example.echo.board.social.RecoVO
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
    lateinit var adapter: CommentAdapter
    var cmtList = ArrayList<CmtListVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        binding = ActivityBoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val board_seq = intent.getStringExtra("board_seq")!!.toInt()
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
            getReco(loginUser,board_seq!!)
            Log.d("test-추천조회2", binding.imgBoardDetailReco.tag.toString())
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

        // 댓글
        var rvCmt = binding.rvCmt
        getComment(board_seq!!)
        adapter = CommentAdapter(this,cmtList)
        rvCmt.adapter = adapter
        rvCmt.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
        Log.d("test-댓글개수", cmtList.toString())

        // 작성된 내용이 있을 시에만 등록 버튼 노출
        binding.tvCmtWrite.visibility = View.INVISIBLE
        binding.etCmtWrite.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            binding.tvCmtWrite.visibility = View.VISIBLE
            if(binding.etCmtWrite.text.isEmpty()) {
                binding.tvCmtWrite.visibility = View.INVISIBLE
            }
            false
        })

        binding.tvCmtWrite.setOnClickListener {
            var newComment = CommentVO(null, board_seq!!, binding.etCmtWrite.text.toString(),
                null, loginUser)
            Log.d("test-댓글작성클릭", newComment.toString())
            addComment(board_seq,newComment)
            binding.etCmtWrite.setText("")
        }

        // 추천 조회
        Log.d("test-추천조회", binding.imgBoardDetailReco.tag.toString())

        // 추천 기능
        binding.imgBoardDetailReco.setOnClickListener {
            var recoCnt = binding.tvBoardDetailRecoCnt.text.toString().toInt()
            Log.d("test-추천수조회", recoCnt.toString())
            var reco = RecoVO(null, board_seq,loginUser,null)
            if(binding.imgBoardDetailReco.tag=="true"){
                deleteReco(loginUser,board_seq)
                binding.imgBoardDetailReco.tag="false"
                binding.imgBoardDetailReco.setImageResource(R.drawable.unbookmarkheart)
                binding.tvBoardDetailRecoCnt.text = (--recoCnt).toString()
            }else{
                addReco(board_seq,reco)
                binding.imgBoardDetailReco.tag="true"
                binding.imgBoardDetailReco.setImageResource(R.drawable.bookmarkheart)
                binding.tvBoardDetailRecoCnt.text = (++recoCnt).toString()
            }
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

    fun getComment(board_seq: Int){
        val call = RetrofitBuilder.socialApi.getComment(board_seq)
        call.enqueue(object : Callback<List<CmtListVO>> {
            override fun onResponse(call: Call<List<CmtListVO>>, response: Response<List<CmtListVO>>
            ) {
                Log.d("text-댓글전부조회", response.body().toString())
                if(response.isSuccessful&& response.body()?.size!!>0){
                    for(i in 0 until response.body()!!.size){
                        cmtList.add(response.body()!!.get(i))
                    }
                }
                cmtList.reverse()
                Log.d("test-댓글개수", cmtList.size.toString())
                binding.tvBoardDetailCmtCnt.text = cmtList.size.toString()
                adapter.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<List<CmtListVO>>, t: Throwable) {
                Log.d("test-가입실패", t.localizedMessage)
            }
        })
    }

    fun addComment(board_seq: Int, newComment:CommentVO){
        val call = RetrofitBuilder.socialApi.addComment(board_seq,newComment)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Toast.makeText(
                    this@BoardDetailActivity, "등록되었습니다",
                    Toast.LENGTH_SHORT
                ).show()

            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-가입실패", t.localizedMessage)
            }
        })
    }

    fun getReco(user_id:String, board_seq: Int){
        val call = RetrofitBuilder.socialApi.getReco(user_id, board_seq)
        call.enqueue(object : Callback<RecoVO> {
            override fun onResponse(call: Call<RecoVO>, response: Response<RecoVO>
            ) {
                Log.d("text-추천조회", response.body()!!.toString())

                var body = response.body()!!

                if(response.isSuccessful) {
                    binding.imgBoardDetailReco.setImageResource(R.drawable.bookmarkheart)
                    binding.imgBoardDetailReco.tag = "true"
                }else{
                    binding.imgBoardDetailReco.setImageResource(R.drawable.unbookmarkheart)
                    binding.imgBoardDetailReco.tag = "false"
                }
                Log.d("test-추천조회3", binding.imgBoardDetailReco.tag.toString())
            }
            override fun onFailure(call: Call<RecoVO>, t: Throwable) {
            }
        })
    }

    fun addReco(board_seq: Int,reco:RecoVO){
        val call = RetrofitBuilder.socialApi.addReco(board_seq,reco)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {

            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }

    fun deleteReco(user_id: String, board_seq: Int){
        val call = RetrofitBuilder.socialApi.deleteReco(user_id, board_seq)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {

            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }

}