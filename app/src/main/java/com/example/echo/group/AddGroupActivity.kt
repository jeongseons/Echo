package com.example.echo.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import retrofit2.Callback

class AddGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        val etAddGroupTitle = findViewById<EditText>(R.id.etAddGroupTitle)
        val etAddGroupContent = findViewById<EditText>(R.id.etAddGroupContent)
        val etAddGroupMax = findViewById<EditText>(R.id.etAddGroupMax)

        val imgAddGroupImg = findViewById<ImageView>(R.id.imgAddGroupImg)
        val imgAddGroupDate = findViewById<ImageView>(R.id.imgAddGroupDate)

        val btnAddGroupBack = findViewById<Button>(R.id.btnAddGroupBack)
        val btnAddGroupSubmit = findViewById<Button>(R.id.btnAddGroupSubmit)

        val spinnerGroupAge = findViewById<Spinner>(R.id.spinnerGroupAge)
        val spinnerGroupArea = findViewById<Spinner>(R.id.spinnerGroupArea)
        val spinnerGroupGen = findViewById<Spinner>(R.id.spinnerGroupGen)

        val ckAddGroupClimb = findViewById<CheckBox>(R.id.ckAddGroupClimb)
        val ckAddGroupHike = findViewById<CheckBox>(R.id.ckAddGroupHike)

        val ckAddGroupHigh = findViewById<CheckBox>(R.id.ckAddGroupHigh)
        val ckAddGroupMedium = findViewById<CheckBox>(R.id.ckAddGroupMedium)
        val ckAddGroupLow = findViewById<CheckBox>(R.id.ckAddGroupLow)
    }
//            fun join(a:String, b:String, c:String, d:String, e:String){ //최초 회원가입
//            val call = RetrofitBuilder.api.postJoin(a,b,c,d,e)
//            call.enqueue(object : Callback<ResponseDTO> {
//                override fun onResponse(
//                    call: Call<ResponseDTO>,
//                    response: Response<ResponseDTO>
//                ) {
//                    if (response.isSuccessful) {
//                         Log.i(TAG,response.body()?.result.toString())
//                    }
//                }
//                override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {
//                    tvHello.text = "응답실패"
//                    Log.d("외않되", t.localizedMessage)
//                }
//
//            })
//        }
}