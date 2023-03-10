package com.example.echo.board

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.board.social.CmtListVO
import com.example.echo.board.social.CommentAdapter
import com.example.echo.board.social.CommentVO
import com.example.echo.board.social.RecoVO
import com.example.echo.databinding.ActivityBoardDetailBinding
import com.example.echo.myPage.CourseMapActivity
import com.example.echo.path.MapVO
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
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
    var mapList = ArrayList<MapVO>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        binding = ActivityBoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBoardDetailMoveBack.setOnClickListener {
            finish()
        }

        val board_seq = intent.getStringExtra("board_seq")!!.toInt()
        val board_title = intent.getStringExtra("board_title")
        val board_content = intent.getStringExtra("board_content")
        val board_file = intent.getStringExtra("board_file")
        val user_nick = intent.getStringExtra("user_nick")
        val user_profile_img = intent.getStringExtra("user_profile_img")
        var board_dt = intent.getStringExtra("board_dt")
        var user_id = intent.getStringExtra("user_id")
        var mnt_name = intent.getStringExtra("mnt_name")
        var course_seq = intent.getIntExtra("course_seq",0)
        var course_img = intent.getStringExtra("course_img")
        var board_reco_cnt = intent.getStringExtra("board_reco_cnt")

        binding.tvBoardDetailTitle.text = board_title
        binding.tvBoardDetailContent.text = board_content
        binding.tvBoardDetailUserNick.text = user_nick
        binding.tvBoardDetailDate.text = board_dt!!.substring(0,board_dt.length-3)
        binding.tvBoardDetailMntName.text = mnt_name
        binding.tvBoardDetailRecoCnt.text = board_reco_cnt

        Glide.with(this)
            .load(user_profile_img)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.imgBoardDetailPropic)

        if(board_file!!.isEmpty()) {
            binding.imgBoardDetailPic.visibility = View.GONE
        } else {
            Glide.with(this)
                .load(board_file)
                .into(binding.imgBoardDetailPic)
        }

        if(course_seq!=0){
            Glide.with(this)
                .load(course_img)
                .into(binding.imgBoardDetailCourse)
            getMap(course_seq)
        }else{
            binding.imgBoardDetailCourse.visibility = View.GONE
        }

        binding.imgBoardDetailCourse.setOnClickListener {
            val intent = Intent(this, CourseMapActivity::class.java)
            Log.d("test-map?????????",mapList.toString())
            intent.putExtra("mapList",mapList)
            startActivity(intent)
        }

        // ????????? ??????
        UserApiClient.instance.me { user, error ->
            loginUser = user?.id.toString()
            getReco(loginUser,board_seq!!)
            Log.d("test-????????????2", binding.imgBoardDetailReco.tag.toString())
            if(user_id==loginUser){
                binding.tvBoardDetailModify.isVisible = true
                binding.tvBoardDetailDelete.isVisible = true
            }
        }

        // ??? ??????
        binding.tvBoardDetailModify.setOnClickListener {
            val intent = Intent(this, BoardWriteActivity::class.java)
            intent.putExtra("modifyCk","true")
            intent.putExtra("board_seq", board_seq)
            intent.putExtra("board_title", board_title)
            intent.putExtra("board_content", board_content)
            intent.putExtra("board_file", board_file)
            intent.putExtra("user_nick", user_nick)
            intent.putExtra("board_dt", board_dt)
            intent.putExtra("user_id", user_id)
            intent.putExtra("mnt_name", mnt_name)
            intent.putExtra("course_seq",course_seq)
            intent.putExtra("course_img",course_img)
            startActivity(intent)
        }

        binding.imgBoardDetailShare.setOnClickListener {
            val defaultFeed = FeedTemplate(
                content = Content(
                    title = board_title!!,
                    description = board_content,
                    imageUrl = board_file!!,
                    link = Link(
                        webUrl = "https://developers.kakao.com",
                        mobileWebUrl = "https://developers.kakao.com"
                    )
                ),
                buttons = listOf(
                    Button(
                        "????????? ??????",
                        Link(
                            webUrl = "https://developers.kakao.com",
                            mobileWebUrl = "https://developers.kakao.com"
                        )
                    ),
                    Button(
                        "????????? ??????",
                        Link(
                            androidExecutionParams = mapOf("key1" to "value1", "key2" to "value2"),
                            iosExecutionParams = mapOf("key1" to "value1", "key2" to "value2")
                        )
                    )
                )
            )

            if (ShareClient.instance.isKakaoTalkSharingAvailable(this)) {
                // ?????????????????? ???????????? ?????? ??????
                ShareClient.instance.shareDefault(this, defaultFeed) { sharingResult, error ->
                    if (error != null) {
                        Log.d("test-??????????????????", "???????????? ?????? ??????", error)
                    }
                    else if (sharingResult != null) {
                        Log.d("test-??????????????????", "???????????? ?????? ?????? ${sharingResult.intent}")
                        startActivity(sharingResult.intent)
                        // ???????????? ????????? ??????????????? ?????? ?????? ???????????? ????????? ?????? ?????? ???????????? ?????? ???????????? ?????? ??? ????????????.
                        Log.d("test-??????????????????", "Warning Msg: ${sharingResult.warningMsg}")
                        Log.d("test-??????????????????", "Argument Msg: ${sharingResult.argumentMsg}")
                    }
                }
            } else {
                // ???????????? ?????????: ??? ?????? ?????? ??????
                // ??? ?????? ?????? ??????
                val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

                // CustomTabs?????? ??? ???????????? ??????

                // 1. CustomTabsServiceConnection ?????? ???????????? ??????
                // ex) Chrome, ?????? ?????????, FireFox, ?????? ???
                try {
                    KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
                } catch(e: UnsupportedOperationException) {
                    // CustomTabsServiceConnection ?????? ??????????????? ?????? ??? ????????????
                }

                // 2. CustomTabsServiceConnection ????????? ???????????? ??????
                // ex) ??????, ????????? ???
                try {
                    KakaoCustomTabsClient.open(this, sharerUrl)
                } catch (e: ActivityNotFoundException) {
                    // ??????????????? ????????? ????????? ??????????????? ?????? ??? ????????????
                }
            }
        }

        // ??? ??????
        binding.tvBoardDetailDelete.setOnClickListener {
            Log.d("test-?????????", board_seq.toString())
            val dialog: AlertDialog.Builder = AlertDialog.Builder(
                this,
                android.R.style.ThemeOverlay_Material_Dialog_Alert
            )
            dialog.setMessage("?????? ?????????????????????????")
                .setTitle("??? ??????")
                .setPositiveButton("?????????", DialogInterface.OnClickListener { dialog, which ->
                    Log.i("Dialog", "??????")
                })
                .setNeutralButton("???",
                    DialogInterface.OnClickListener { dialog, which ->
                        deleteBoard(board_seq!!)
                    })
                .show()
        }


        // ??????
        var rvCmt = binding.rvCmt
        getComment(board_seq!!)
        adapter = CommentAdapter(this,cmtList)
        rvCmt.adapter = adapter
        rvCmt.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()

        Log.d("test-????????????", cmtList.toString())

        // ????????? ????????? ?????? ????????? ?????? ?????? ??????
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
            Log.d("test-??????????????????", newComment.toString())
            addComment(board_seq,newComment)
            binding.etCmtWrite.setText("")
        }


        // ?????? ??????
        Log.d("test-????????????", binding.imgBoardDetailReco.tag.toString())

        // ?????? ??????
        binding.imgBoardDetailReco.setOnClickListener {
            var recoCnt = binding.tvBoardDetailRecoCnt.text.toString().toInt()
            Log.d("test-???????????????", recoCnt.toString())
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
                Log.d("test-?????????", response.body().toString())
                if(response.isSuccessful) {
                    Toast.makeText(
                        this@BoardDetailActivity, "??????????????? ?????????????????????",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }else{
                    Toast.makeText(
                        this@BoardDetailActivity, "?????? ??????????????????",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-????????????", t.localizedMessage)
            }
        })
    }

    fun getComment(board_seq: Int){
        val call = RetrofitBuilder.socialApi.getComment(board_seq)
        call.enqueue(object : Callback<List<CmtListVO>> {
            override fun onResponse(call: Call<List<CmtListVO>>, response: Response<List<CmtListVO>>
            ) {
                Log.d("text-??????????????????", response.body().toString())
                if(response.isSuccessful&& response.body()?.size!!>0){
                    for(i in 0 until response.body()!!.size){
                        cmtList.add(response.body()!!.get(i))
                    }
                }
                cmtList.reverse()
                Log.d("test-????????????", cmtList.size.toString())
                binding.tvBoardDetailCmtCnt.text = cmtList.size.toString()
                adapter.notifyDataSetChanged()

                //?????? ?????? ??????

                if(cmtList.size==0){
                    binding.noComment1.visibility=View.VISIBLE
                    binding.noComment2.visibility=View.VISIBLE
                    binding.noComment3.visibility=View.VISIBLE
                    binding.noComment4.visibility=View.VISIBLE

                    binding.rvCmt.visibility=View.GONE
                }else{
                    binding.noComment1.visibility=View.GONE
                    binding.noComment2.visibility=View.GONE
                    binding.noComment3.visibility=View.GONE
                    binding.noComment4.visibility=View.GONE

                    binding.rvCmt.visibility=View.VISIBLE
                }

            }


            override fun onFailure(call: Call<List<CmtListVO>>, t: Throwable) {
                Log.d("test-????????????", t.localizedMessage)
            }
        })
    }

    fun addComment(board_seq: Int, newComment:CommentVO){
        val call = RetrofitBuilder.socialApi.addComment(board_seq,newComment)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Toast.makeText(
                    this@BoardDetailActivity, "?????????????????????",
                    Toast.LENGTH_SHORT
                ).show()
                refresh()
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-????????????", t.localizedMessage)
            }
        })
    }

    fun getReco(user_id:String, board_seq: Int){
        val call = RetrofitBuilder.socialApi.getReco(user_id, board_seq)
        call.enqueue(object : Callback<RecoVO> {
            override fun onResponse(call: Call<RecoVO>, response: Response<RecoVO>
            ) {
                Log.d("text-????????????", response.body()!!.toString())

                var body = response.body()!!

                if(response.isSuccessful) {
                    binding.imgBoardDetailReco.setImageResource(R.drawable.bookmarkheart)
                    binding.imgBoardDetailReco.tag = "true"
                }else{
                    binding.imgBoardDetailReco.setImageResource(R.drawable.unbookmarkheart)
                    binding.imgBoardDetailReco.tag = "false"
                }
                Log.d("test-????????????3", binding.imgBoardDetailReco.tag.toString())
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

    fun getMap(course_seq: Int) {
        mapList.clear()
        val call = RetrofitBuilder.courseApi.getMap(course_seq)
        call.enqueue(object : Callback<List<MapVO>> {
            override fun onResponse(call: Call<List<MapVO>>, response: Response<List<MapVO>>) {
                if(response.isSuccessful&& response.body()?.size!!>0){
                    for(i in 0 until response.body()!!.size){
                        mapList.add(response.body()!!.get(i))
                    }
                }
                Log.d("test-getMap",mapList.toString())
            }
            override fun onFailure(call: Call<List<MapVO>>, t: Throwable) {
                Log.d("test-getMap", t.localizedMessage)

            }
        })
    }

    fun refresh(){
        finish() //????????? ??????
        overridePendingTransition(0, 0)
        val intent = intent //?????????
        startActivity(intent) //???????????? ??????
        overridePendingTransition(0, 0)
    }

}