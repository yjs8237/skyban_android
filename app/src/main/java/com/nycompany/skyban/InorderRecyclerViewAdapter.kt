package com.nycompany.skyban

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nycompany.skyban.DTO.InoderDTO
import com.nycompany.skyban.DTO.List

import java.util.ArrayList

class InorderRecyclerViewAdapter(private val inOrders: ArrayList<List>?) : RecyclerView.Adapter<InorderRecyclerViewAdapter.ViewHolder>() {
    private var mListener: View.OnClickListener? = null

    fun setClickListener(callback: View.OnClickListener) {
        this.mListener = callback
    }

    override fun onCreateViewHolder(v: ViewGroup, i: Int): InorderRecyclerViewAdapter.ViewHolder {
        //xml 가져옴
        val view = LayoutInflater.from(v.context).inflate(R.layout.inorder_recyclerview_item, v, false)
        return ViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(vh: InorderRecyclerViewAdapter.ViewHolder, i: Int) {
        //xml 데아터 바인딩
        vh.tv_name.text = inOrders!![i].order_user_num
        vh.tv_version.text = inOrders!![i].min_car_type
        vh.tv_api_level.text = inOrders!![i].work_location
    }

    override fun getItemCount(): Int {
        //아이템을 측정하는 카운터
        return inOrders!!.size
    }

    inner class ViewHolder(v: View, private val mListener: View.OnClickListener) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val tv_name: TextView
        val tv_version: TextView
        val tv_api_level: TextView

        init {
            v.setOnClickListener(this)

            tv_name = v.findViewById<View>(R.id.textView13) as TextView
            tv_version = v.findViewById<View>(R.id.textView15) as TextView
            tv_api_level = v.findViewById<View>(R.id.textView16) as TextView
        }

        override fun onClick(view: View) {
            mListener.onClick(view)
        }
    }
}
