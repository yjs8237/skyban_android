package com.nycompany.skyban

import android.content.Context
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
    private var context: Context? = null

    fun setClickListener(callback: View.OnClickListener) {
        this.mListener = callback
    }

    override fun onCreateViewHolder(v: ViewGroup, i: Int): InorderRecyclerViewAdapter.ViewHolder {
        //xml 가져옴
        context = v.context
        val view = LayoutInflater.from(v.context).inflate(R.layout.inorder_recyclerview_item, v, false)
        return ViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(vh: InorderRecyclerViewAdapter.ViewHolder, i: Int) {
        //xml 데아터 바인딩
        var util = ContextUtil(context!!)
        vh.textViewCommission_yn.text = if (inOrders!![i].commission_yn =="Y") context?.getString(R.string.commission_y)  else context?.getString(R.string.commission_n)
        vh.textViewDate.text = inOrders!![i].work_date!!.split(" ")[0]
        vh.textViewTime.text = inOrders!![i].work_date!!.split(" ")[1]
        var workProcMap = util.parseStringArray(R.array.work_proc)
        vh.textViewWorkProc.text = workProcMap[inOrders!![i].work_proc]
    }

    override fun getItemCount(): Int {
        //아이템을 측정하는 카운터
        return inOrders!!.size
    }

    inner class ViewHolder(v: View, private val mListener: View.OnClickListener) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val textViewCommission_yn: TextView
        val textViewDate: TextView
        val textViewTime: TextView
        val textViewWorkProc: TextView

        init {
            v.setOnClickListener(this)

            textViewCommission_yn = v.findViewById<View>(R.id.textViewCommission_yn) as TextView
            textViewDate = v.findViewById<View>(R.id.textViewDate) as TextView
            textViewTime = v.findViewById<View>(R.id.textViewTime) as TextView
            textViewWorkProc = v.findViewById<View>(R.id.textViewWorkProc) as TextView
        }

        override fun onClick(view: View) {
            mListener.onClick(view)
        }
    }
}
