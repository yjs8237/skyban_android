package com.nycompany.skyban.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nycompany.skyban.R
import com.nycompany.skyban.dto.NotificationList

import java.util.ArrayList

class NotificationRecyclerViewAdapter(private val notis: ArrayList<NotificationList>?) : RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder>() {

    private var context: Context? = null
    private var mListener: View.OnClickListener? = null
    fun setClickListener(callback: View.OnClickListener) {
        this.mListener = callback
    }

    override fun onCreateViewHolder(v: ViewGroup, i: Int): ViewHolder {
        //xml 가져옴
        context = v.context
        val view = LayoutInflater.from(v.context).inflate(R.layout.recyclerview_notification_item, v, false)
        return ViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        //xml 데아터 바인딩
        vh.textView_Title.text = notis!![i].title
        vh.textView_Notiseq.text = notis!![i].noti_seq
    }

    override fun getItemCount(): Int {
        //아이템을 측정하는 카운터
        return notis!!.size
    }

    inner class ViewHolder(v: View, private val mListener: View.OnClickListener) : RecyclerView.ViewHolder(v), View.OnClickListener{
        val textView_Title:TextView
        val textView_Notiseq:TextView
        init {
            v.setOnClickListener(this)

            textView_Title = v.findViewById<View>(R.id.textView_Title) as TextView
            textView_Notiseq = v.findViewById<View>(R.id.textView_Notiseq) as TextView
        }

        override fun onClick(view: View) {
            mListener.onClick(view)
        }
    }
}
