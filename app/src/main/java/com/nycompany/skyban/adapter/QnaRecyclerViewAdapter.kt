package com.nycompany.skyban.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nycompany.skyban.R
import com.nycompany.skyban.dto.QnaList

import java.util.ArrayList

class QnaRecyclerViewAdapter(private val qnas: ArrayList<QnaList>?) : RecyclerView.Adapter<QnaRecyclerViewAdapter.ViewHolder>() {

    private var context: Context? = null
    private var mListener: View.OnClickListener? = null
    fun setClickListener(callback: View.OnClickListener) {
        this.mListener = callback
    }

    override fun onCreateViewHolder(v: ViewGroup, i: Int): ViewHolder {
        //xml 가져옴
        context = v.context
        val view = LayoutInflater.from(v.context).inflate(R.layout.recyclerview_qna_item, v, false)
        return ViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        //xml 데아터 바인딩
        qnas!![i].reply_yn?.let {
            if ("N" == it) {
                vh.constraintLayout_left.setBackgroundResource(R.color.pink)
                vh.textView_reply_yn.text = "미완료"
            } else {
                vh.constraintLayout_left.setBackgroundResource(R.color.sky)
                vh.textView_reply_yn.text = "답변완료"
            }
        }

        vh.textView_title.text = "제목 : ${qnas!![i].title}"
        vh.textView_qna_seq.text = qnas!![i].qna_seq
    }

    override fun getItemCount(): Int {
        //아이템을 측정하는 카운터
        return qnas!!.size
    }

    inner class ViewHolder(v: View, private val mListener: View.OnClickListener) : RecyclerView.ViewHolder(v), View.OnClickListener{
        val textView_reply_yn:TextView
        val textView_title:TextView
        val textView_qna_seq:TextView
        val constraintLayout_left:ConstraintLayout
        init {
            v.setOnClickListener(this)

            textView_reply_yn = v.findViewById<View>(R.id.textView_reply_yn) as TextView
            textView_title = v.findViewById<View>(R.id.textView_title) as TextView
            textView_qna_seq = v.findViewById<View>(R.id.textView_qna_seq) as TextView
            constraintLayout_left = v.findViewById<View>(R.id.constraintLayout_left) as ConstraintLayout
        }

        override fun onClick(view: View) {
            mListener.onClick(view)
        }
    }
}
