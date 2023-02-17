package com.example.echo.group.detail

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.echo.R
import com.example.echo.path.CourseList

class PersonCourseAdapter(var context: Context, var courseList:ArrayList<CourseList>)
    : RecyclerView.Adapter<PersonCourseAdapter.ViewHolder>()  {

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

        val tvCourseTitle: TextView
        val tvCourseDate: TextView
        val tvCourseCount: TextView
        val tvCourseAddress: TextView
        val imgCoursePic: ImageView

        init{
            tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle)
            tvCourseDate = itemView.findViewById(R.id.tvCourseDate)
            tvCourseCount = itemView.findViewById(R.id.tvCourseCount)
            tvCourseAddress = itemView.findViewById(R.id.tvCourseAddress)
            imgCoursePic = itemView.findViewById(R.id.imgCoursePic)

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
        val view = inflater.inflate(R.layout.course_list,null)

        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCourseTitle.text = courseList[position].course_title
        holder.tvCourseDate.text = courseList[position].course_start_dt
        holder.tvCourseCount.text = courseList[position].course_distance

        val geocoder = Geocoder(context)
        var addr:String=""
        var addr2:String=""
        var addr3:String=""

        addr = geocoder.getFromLocation(courseList[position].start_lat, courseList[position].start_lng, 1).first().adminArea

        if(geocoder.getFromLocation(courseList[position].start_lat, courseList[position].start_lng, 1).first().subLocality==null) {
            addr3 = geocoder.getFromLocation(courseList[position].start_lat, courseList[position].start_lng, 1)
                .first().locality
            Log.d("test-맵",addr3.toString())
            holder.tvCourseAddress.text = "${addr} ${addr3}"
        }
        else{
            addr2 = geocoder.getFromLocation(courseList[position].start_lat, courseList[position].start_lng, 1).first().subLocality
            holder.tvCourseAddress.text = "${addr} ${addr2}"
            Log.d("test-맵",addr2.toString())
        }

        Glide.with(context)
            .load(courseList[position].course_img)
            .into(holder.imgCoursePic)


    }

    override fun getItemCount(): Int {
        return courseList.size
    }

}

