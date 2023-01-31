package com.example.echo.myPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echo.RetrofitBuilder
import com.example.echo.board.BoardDetailActivity
import com.example.echo.board.BoardListVO
import com.example.echo.databinding.ActivityMyBoardBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var myBoardList = ArrayList<BoardListVO>()
lateinit var adapter: MyBoardAdapter

class MyBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_my_board)
        val user_id = intent.getStringExtra("user_id")

        getMyBoard(user_id!!)
        adapter = MyBoardAdapter(this, myBoardList)
        binding.rvMyBoard.adapter = adapter
        binding.rvMyBoard.layoutManager = LinearLayoutManager(this)

//         각 게시글 클릭 이벤트 - 게시글 내부로 이동
        adapter.setOnItemClickListener(object : MyBoardAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@MyBoardActivity, BoardDetailActivity::class.java)
                intent.putExtra("board_seq", myBoardList[position].board_seq.toString())
                intent.putExtra("board_title", myBoardList[position].board_title)
                intent.putExtra("board_content", myBoardList[position].board_content)
                intent.putExtra("board_file", myBoardList[position].board_file)
                intent.putExtra("user_nick", myBoardList[position].user_nick)
                intent.putExtra("board_dt", myBoardList[position].board_dt)
                intent.putExtra("user_id", myBoardList[position].user_id)
                intent.putExtra("mnt_name", myBoardList[position].mnt_name)
                intent.putExtra("board_reco_cnt", myBoardList[position].board_reco_cnt.toString())
                startActivity(intent)
            }
        })

        binding.tvMyBoardDelete.setOnClickListener {
            deleteSelectedBoard(adapter.selectDelete())
        }

        binding.tvMyBoardSelect.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                adapter.setCheckAll()
                adapter.notifyDataSetChanged()
            }
        })


    }

    fun getMyBoard(user_id:String){
        myBoardList.clear()
        val call = RetrofitBuilder.boardApi.getBoard(user_id)
        call.enqueue(object : Callback<List<BoardListVO>> {
            override fun onResponse(call: Call<List<BoardListVO>>, response: Response<List<BoardListVO>>) {
                if(response.isSuccessful&& response.body()?.size!!>0){
                    for(i in 0 until response.body()!!.size){
                        myBoardList.add(response.body()!!.get(i))
                    }
                }
                myBoardList.reverse()
                adapter.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<List<BoardListVO>>, t: Throwable) {

            }
        })
    }

    fun deleteSelectedBoard(boardSeqList: ArrayList<Int>) {
        val call = RetrofitBuilder.boardApi.deleteSelectedBoard(boardSeqList)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Log.d("test-삭제후", response.body().toString())
                if(response.isSuccessful) {
                    Toast.makeText(
                        applicationContext, "정상적으로 삭제되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }else{
                    Toast.makeText(
                        applicationContext, "다시 시도해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }

}