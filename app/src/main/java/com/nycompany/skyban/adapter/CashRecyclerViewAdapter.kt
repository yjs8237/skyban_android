package com.nycompany.skyban.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nycompany.skyban.R
import com.nycompany.skyban.dto.CashList

import java.util.ArrayList

class CashRecyclerViewAdapter(private val cashs: ArrayList<CashList>?) : RecyclerView.Adapter<CashRecyclerViewAdapter.ViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(v: ViewGroup, i: Int): ViewHolder {
        //xml 가져옴
        context = v.context
        val view = LayoutInflater.from(v.context).inflate(R.layout.recyclerview_cashlist_item, v, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        //xml 데아터 바인딩
        cashs!![i].req_type?.let {
            if ("1" == it) {
                vh.constraintLayout_left.setBackgroundResource(R.color.sky)
                vh.textView_ReqType.text = "충전"
            } else {
                vh.constraintLayout_left.setBackgroundResource(R.color.pink)
                vh.textView_ReqType.text = "환전"
            }
        }
        vh.textView_Cash.text = cashs[i].cash

        vh.textView_RegDate.text = "요청일시 : ${cashs[i].reg_date?.let { it }?:run { "" }}"
        vh.textView_ResultDate.text = "처리일시 : ${cashs[i].result_date?.let { it }?:run { "" }}"
    }

    override fun getItemCount(): Int {
        //아이템을 측정하는 카운터
        return cashs!!.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val textView_ReqType:TextView
        val textView_Cash:TextView

        val textView_RegDate:TextView
        val textView_ResultDate:TextView
        val constraintLayout_left:ConstraintLayout

        init {
            textView_ReqType = v.findViewById<View>(R.id.textView_ReqType) as TextView
            textView_Cash = v.findViewById<View>(R.id.textView_Cash) as TextView

            textView_RegDate = v.findViewById<View>(R.id.textView_RegDate) as TextView
            textView_ResultDate = v.findViewById<View>(R.id.textView_ResultDate) as TextView
            constraintLayout_left = v.findViewById<View>(R.id.constraintLayout_left) as ConstraintLayout
        }
    }
}
