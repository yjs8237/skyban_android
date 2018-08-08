package com.nycompany.skyban.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nycompany.skyban.R
import com.nycompany.skyban.dto.PointList

import java.util.ArrayList

class PointRecyclerViewAdapter(private val points: ArrayList<PointList>?) : RecyclerView.Adapter<PointRecyclerViewAdapter.ViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(v: ViewGroup, i: Int): ViewHolder {
        //xml 가져옴
        context = v.context
        val view = LayoutInflater.from(v.context).inflate(R.layout.recyclerview_pointlist_item, v, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        //xml 데아터 바인딩
        vh.textViewPointType.text = points!![i].point_type
        points[i].point_type?.let {
            if ("발주" == it) {
                vh.constraintLayout_left.setBackgroundResource(R.color.pink)
                vh.textViewPoint.text = "- ${points[i].point}P"
            } else {
                vh.constraintLayout_left.setBackgroundResource(R.color.sky)
                vh.textViewPoint.text = "+ ${points[i].point}P"
            }
        }

        vh.textView_order_num.text = "발주자 연락처 : ${points[i].order_user_num}"
        vh.textView_work_contact.text = "현장 연락처 : ${points[i].work_contact}"
        vh.textView_work_location.text = "작업장소 : ${points[i].work_location}"
        vh.textView_work_date.text = "작업일시 : ${points[i].work_date}"
        vh.textView_work_pay.text = "작업금액 : ${points[i].work_pay}"
    }

    override fun getItemCount(): Int {
        //아이템을 측정하는 카운터
        return points!!.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val textViewPointType:TextView
        val textViewPoint:TextView

        val textView_order_num:TextView
        val textView_work_contact:TextView
        val textView_work_location:TextView
        val textView_work_date:TextView
        val textView_work_pay:TextView
        val constraintLayout_left:ConstraintLayout

        init {
            textViewPointType = v.findViewById<View>(R.id.textViewPointType) as TextView
            textViewPoint = v.findViewById<View>(R.id.textViewPoint) as TextView

            textView_order_num = v.findViewById<View>(R.id.textView_order_num) as TextView
            textView_work_contact = v.findViewById<View>(R.id.textView_work_contact) as TextView
            textView_work_location = v.findViewById<View>(R.id.textView_work_location) as TextView
            textView_work_date = v.findViewById<View>(R.id.textView_work_date) as TextView
            textView_work_pay = v.findViewById<View>(R.id.textView_work_pay) as TextView
            constraintLayout_left = v.findViewById<View>(R.id.constraintLayout_left) as ConstraintLayout
        }
    }
}
