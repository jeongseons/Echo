package com.example.echo.myPage

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.echo.R
import com.example.echo.path.CourseInfo
import com.example.echo.path.CourseList
import com.example.echo.path.startLatLng

class MyCourseAdapter(var context: Context, var myCourseList: ArrayList<CourseList>)
    :RecyclerView.Adapter<MyCourseAdapter.ViewHolder>(){

    private val checkboxStatus = SparseBooleanArray()
    var allSelectCk = false
    val courseSeqList: ArrayList<Int> = ArrayList()

    // 리스너 커스텀
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    // 객체 저장 변수 선언
    lateinit var mOnItemClickListener: OnItemClickListener

    //객체 전달 메서드
    fun setOnItemClickListener(OnItemClickListener: OnItemClickListener) {
        mOnItemClickListener = OnItemClickListener
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val tvMyCourseTitle:TextView
        val tvMyCourseDate:TextView
        val tvMyCourseCount:TextView
        val tvMyCourseAddress:TextView
        val imgMyCoursePic:ImageView
        val ckMyCourse:CheckBox

        init{
            tvMyCourseTitle = itemView.findViewById(R.id.tvMyCourseTitle)
            tvMyCourseDate = itemView.findViewById(R.id.tvMyCourseDate)
            tvMyCourseCount = itemView.findViewById(R.id.tvMyCourseCount)
            tvMyCourseAddress = itemView.findViewById(R.id.tvMyCourseAddress)
            imgMyCoursePic = itemView.findViewById(R.id.imgMyCoursePic)
            ckMyCourse = itemView.findViewById(R.id.ckMyCourse)

            // 경로 내부로 이동
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(itemView, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.my_course_list,null)

        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMyCourseTitle.text = myCourseList[position].course_title
        holder.tvMyCourseDate.text = myCourseList[position].course_start_dt
        holder.tvMyCourseCount.text = myCourseList[position].course_distance

        val geocoder = Geocoder(context)
        var addr:String=""
        var addr2:String=""
        var addr3:String=""

        addr = geocoder.getFromLocation(myCourseList[position].start_lat, myCourseList[position].start_lng, 1).first().adminArea

        if(geocoder.getFromLocation(myCourseList[position].start_lat, myCourseList[position].start_lng, 1).first().subLocality==null) {
            addr3 = geocoder.getFromLocation(myCourseList[position].start_lat, myCourseList[position].start_lng, 1)
                .first().locality
            Log.d("test-맵",addr3.toString())
            holder.tvMyCourseAddress.text = "${addr} ${addr3}"
        }
        else{
            addr2 = geocoder.getFromLocation(myCourseList[position].start_lat, myCourseList[position].start_lng, 1).first().subLocality
            holder.tvMyCourseAddress.text = "${addr} ${addr2}"
            Log.d("test-맵",addr2.toString())
        }


        holder.ckMyCourse.isChecked = checkboxStatus[position]

        Glide.with(context)
            .load(myCourseList[position].course_img)
            .into(holder.imgMyCoursePic)

     holder.ckMyCourse.setOnClickListener {
            if (!holder.ckMyCourse.isChecked) {
                checkboxStatus.put(position, false)
                courseSeqList.remove(myCourseList[position].course_seq)
            }
            else{
                checkboxStatus.put(position, true)
                courseSeqList.add(myCourseList[position].course_seq)
            }
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return myCourseList.size
    }

    fun setCheckAll() {
        courseSeqList.clear()
        if(allSelectCk) {
            for(i in 0 until myCourseList.size){
                checkboxStatus.put(i, false)
            }
        }else{
            for(i in 0 until myCourseList.size){
                checkboxStatus.put(i, true)
                courseSeqList.add(myCourseList[i].course_seq!!)
            }
        }
        allSelectCk = !allSelectCk
        notifyDataSetChanged()
    }

    fun selectDelete(): ArrayList<Int> {
        for (i in 0 until courseSeqList.size) {
            Log.d("test-체크박스", courseSeqList[i].toString())
        }
        return courseSeqList
    }


}