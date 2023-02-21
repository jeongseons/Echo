package com.example.echo.myPage

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.board.BoardDetailActivity
import com.example.echo.board.BoardListVO
import com.example.echo.databinding.FragmentMyBoardBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBoardFragment : Fragment() {
    var myBoardList = ArrayList<BoardListVO>()
    lateinit var adapter: MyBoardAdapter
    private lateinit var binding: FragmentMyBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var user_id = arguments?.getString("user_id").toString()
        Log.d("test", user_id)
        getMyBoard(user_id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val mainActivity = (activity as MainActivity)
                mainActivity.changeFragment(2)
            }
        })

        binding = FragmentMyBoardBinding.inflate(layoutInflater)
        adapter = MyBoardAdapter(requireContext(), myBoardList)
        binding.rvMyBoard.adapter = adapter
        binding.rvMyBoard.layoutManager = LinearLayoutManager(requireContext())

//         각 게시글 클릭 이벤트 - 게시글 내부로 이동
        adapter.setOnItemClickListener(object : MyBoardAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(requireContext(), BoardDetailActivity::class.java)
                intent.putExtra("board_seq", myBoardList[position].board_seq.toString())
                intent.putExtra("board_title", myBoardList[position].board_title)
                intent.putExtra("board_content", myBoardList[position].board_content)
                intent.putExtra("board_file", myBoardList[position].board_file)
                intent.putExtra("user_nick", myBoardList[position].user_nick)
                intent.putExtra("board_dt", myBoardList[position].board_dt)
                intent.putExtra("user_id", myBoardList[position].user_id)
                intent.putExtra("mnt_name", myBoardList[position].mnt_name)
                intent.putExtra("board_reco_cnt", myBoardList[position].board_reco_cnt.toString())
                intent.putExtra("user_profile_img", myBoardList[position].user_profile_img)
                intent.putExtra("course_seq", myBoardList[position].course_seq)
                intent.putExtra("course_img", myBoardList[position].course_img)
                startActivity(intent)
            }
        })

        binding.tvMyBoardDelete.setOnClickListener {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(
                requireContext(),
                android.R.style.ThemeOverlay_Material_Dialog_Alert
            )
            dialog.setMessage("정말로 삭제하시겠습니까?")
                .setTitle("글 삭제")
                .setPositiveButton("아니오", DialogInterface.OnClickListener { dialog, which ->
                    Log.i("Dialog", "취소")
                })
                .setNeutralButton("예",
                    DialogInterface.OnClickListener { dialog, which ->
                        deleteSelectedBoard(adapter.selectDelete())
                    })
                .show()
        }

        binding.tvMyBoardSelect.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                adapter.setCheckAll()
                adapter.notifyDataSetChanged()
            }
        })

        return binding.root
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
                        requireContext(), "정상적으로 삭제되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
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