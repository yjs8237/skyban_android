package com.nycompany.skyban

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.nycompany.skyban.DTO.List
import com.nycompany.skybanminitp.FragmentsAvailable

import java.util.ArrayList

class OrderRecyclerViewAdapter(private val inOrders: ArrayList<List>?, FragmentType:FragmentsAvailable) : RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder>() {
    private var mListener: View.OnClickListener? = null
    private var context: Context? = null
    private val fType = FragmentType

    fun setClickListener(callback: View.OnClickListener) {
        this.mListener = callback
    }

    override fun onCreateViewHolder(v: ViewGroup, i: Int): OrderRecyclerViewAdapter.ViewHolder {
        //xml 가져옴
        context = v.context
        val view = LayoutInflater.from(v.context).inflate(R.layout.inorder_recyclerview_item, v, false)
        return ViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(vh: OrderRecyclerViewAdapter.ViewHolder, i: Int) {
        //xml 데아터 바인딩
        var util = ContextUtil(context!!)
        vh.textView_Orderseq.text = inOrders!![i].order_seq
        vh.textViewCommission_yn.text = if (inOrders!![i].commission_yn =="Y") context?.getString(R.string.commission_y)  else context?.getString(R.string.commission_n)
        //if(fType == FragmentsAvailable.ORDER_HISTORY) {vh.textViewCommission_yn.visibility = View.GONE}
        vh.textViewDate.text = inOrders!![i].work_date!!.split(" ")[0]
        vh.textViewTime.text = inOrders!![i].work_date!!.split(" ")[1]
        var workProcMap = util.getHashmapFromResoureces(R.array.work_proc)
        vh.textViewWorkProc.text = workProcMap[inOrders!![i].work_proc]
        var carTypeMap = util.getHashmapFromResoureces(R.array.car_type)
        var carLengthMap = util.getHashmapFromResoureces(R.array.car_length)
        vh.textView_car_length.text = "차량 : ${carTypeMap[inOrders!![i].min_car_type]}" +
                "${carLengthMap[inOrders!![i].min_car_length]?.let{ " - " + it}?:run{ "" }} ~ " +
                "${carTypeMap[inOrders!![i].max_car_type]}" +
                "${carLengthMap[inOrders!![i].min_car_length]?.let{ " - " + it}?:run{ "" }}"
        vh.textView_work_location.text = "장소 : ${inOrders!![i].work_location}"
        var op_invertor =  if (inOrders!![i].op_danchuk == "Y") "인버터" else null
        var op_guljul =  if (inOrders!![i].op_guljul == "Y") "굴절" else null
        var op_winchi =  if (inOrders!![i].op_winchi == "Y") "윈찌" else null
        var op_danchuk =  if (inOrders!![i].op_danchuk == "Y") "단축" else null
        vh.textView_work_option.text = "옵션 : ${op_invertor?.let{it +" "}?:run{""}} ${op_guljul?.let{it +" "}?:run{""}} " +
                "${op_winchi?.let{it +" "}?:run{""}} ${op_danchuk?.let{it +" "}?:run{""}}"
        vh.textView_work_pay.text = "수당 : ${inOrders!![i].work_pay}"
        var payDateMap = util.getHashmapFromResoureces(R.array.pay_date)
        vh.textView_word_duration.text = "결재기간 : ${payDateMap[inOrders!![i].pay_date]?.let{it}?:run{""}}"

        inOrders!![i].work_proc?.let {
            if ("WP04" == it) {
                vh.linearLayout_left.setBackgroundResource(R.color.grey)
                vh.linearLayout_right.setBackgroundResource(R.color.grey)
                for (i in 0..(vh.linearLayout_left.childCount - 1)) {
                    (vh.linearLayout_left.getChildAt(i) as TextView)?.let { it.setTextColor(context?.resources?.getColor(R.color.black)!!) }
                }
            } else {
                vh.linearLayout_left.setBackgroundResource(R.color.sky)
                vh.linearLayout_right.setBackgroundResource(R.color.white)
                for (i in 0..(vh.linearLayout_left.childCount - 1)) {
                    (vh.linearLayout_left.getChildAt(i) as TextView)?.let { it.setTextColor(context?.resources?.getColor(R.color.white)!!) }
                }
            }
        }
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
        val textView_Orderseq:TextView

        val textView_car_length: TextView
        val textView_work_location: TextView
        val textView_work_option: TextView
        val textView_work_pay: TextView
        val textView_word_duration: TextView
        val linearLayout_left: LinearLayout
        val linearLayout_right: LinearLayout

        init {
            v.setOnClickListener(this)

            textView_Orderseq = v.findViewById<View>(R.id.textView_Orderseq) as TextView
            textViewCommission_yn = v.findViewById<View>(R.id.textViewCommission_yn) as TextView
            textViewDate = v.findViewById<View>(R.id.textViewDate) as TextView
            textViewTime = v.findViewById<View>(R.id.textViewTime) as TextView
            textViewWorkProc = v.findViewById<View>(R.id.textViewWorkProc) as TextView

            textView_car_length = v.findViewById<View>(R.id.textView_car_length) as TextView
            textView_work_location = v.findViewById<View>(R.id.textView_work_location) as TextView
            textView_work_option = v.findViewById<View>(R.id.textView_work_option) as TextView
            textView_work_pay = v.findViewById<View>(R.id.textView_work_pay) as TextView
            textView_word_duration = v.findViewById<View>(R.id.textView_word_duration) as TextView
            linearLayout_left = v.findViewById<View>(R.id.linearLayout_left) as LinearLayout
            linearLayout_right = v.findViewById<View>(R.id.linearLayout_right) as LinearLayout
        }

        override fun onClick(view: View) {
            mListener.onClick(view)
        }
    }
}
