package com.example.echo.path

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.echo.R
import com.example.echo.databinding.DialogCourseModifyBinding

class DialogCourseModify (private val context : AppCompatActivity) {

    private lateinit var binding : DialogCourseModifyBinding
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    private lateinit var listener : MyDialogOKClickedListener

    fun show(content : String) {
        binding = DialogCourseModifyBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        binding.rdoCourseModifyType.setOnCheckedChangeListener { _, checkedId ->
            Log.d(" ", "RadioButton is Clicked")
            when (checkedId) {
                R.id.rdoCourseModifyPublic-> {
                    course_open = "y"
                }
                R.id.rdoCourseModifyClosed -> {
                    course_open = "n"
                }
            }
            Log.d("test-경로공개여부", course_open.toString())
        }

        //ok 버튼 동작
        binding.btnCourseModifyOk.setOnClickListener {

            listener.onOKClicked("확인을 눌렀습니다.")

            dlg.dismiss()
        }

        //cancel 버튼 동작
        binding.btnCourseModifyCancel.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()

    }

    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object: MyDialogOKClickedListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }


    interface MyDialogOKClickedListener {
        fun onOKClicked(content : String)

    }

}