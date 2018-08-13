package com.nycompany.skyban.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.nycompany.skyban.util.ContextUtil
import com.nycompany.skyban.dto.OrderList
import com.nycompany.skyban.R
import com.nycompany.skybanminitp.FragmentsAvailable

import java.util.ArrayList

class OrderRecyclerViewAdapter(private val inOrders: ArrayList<OrderList>?, FragmentType:FragmentsAvailable) : RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder>() {
    private var mListener: View.OnClickListener? = null
    private var context: Context? = null
    private val fType = FragmentType

    fun setClickListener(callback: View.OnClickListener) {
        this.mListener = callback
    }

    override fun onCreateViewHolder(v: ViewGroup, i: Int): ViewHolder {
        //xml 가져옴
        context = v.context
        val view = LayoutInflater.from(v.context).inflate(R.layout.recyclerview_inorder_item, v, false)
        return ViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        //xml 데아터 바인딩
        var util = ContextUtil(context!!)
        vh.textView_Orderseq.text = inOrders!![i].order_seq
        //if(fType == FragmentsAvailable.ORDER_HISTORY) {vh.textViewCommission_yn.visibility = View.GONE}
        vh.textViewDate.text = inOrders[i].work_date!!.split(" ")[0]?.let{it.substring(5, it.length)}
        vh.textViewTime.text = inOrders[i].work_date!!.split(" ")[1]

        var durationPayMap = util.getHashmapFromResoureces(R.array.work_duration)
        vh.textView_work_duration.text = "작업시간 : ${durationPayMap[inOrders[i].work_duration]?.let{it}?:run{""}}"

        var workProcMap = util.getHashmapFromResoureces(R.array.work_proc)
        vh.textViewWorkProc.text = workProcMap[inOrders[i].work_proc]
        var carTypeMap = util.getHashmapFromResoureces(R.array.car_type)
        var carLengthMap = util.getHashmapFromResoureces(R.array.car_length)
        vh.textView_car_length.text = "차량 : ${carTypeMap[inOrders[i].min_car_type]}" +
                "${carLengthMap[inOrders[i].min_car_length]?.let{ " - " + it}?:run{ "" }} ~ " +
                "${carTypeMap[inOrders[i].max_car_type]}" +
                "${carLengthMap[inOrders[i].min_car_length]?.let{ " - " + it}?:run{ "" }}"

        var location = if (fType == FragmentsAvailable.ORDER) "장소 : ${inOrders[i].work_location}"
        else "장소 : ${inOrders[i].work_location}  ${inOrders[i].work_location_detail}"
        vh.textView_work_location.text = location

        var op_invertor =  if (inOrders[i].op_invertor == "Y") "인버터 " else ""
        var op_guljul =  if (inOrders[i].op_guljul == "Y") "굴절 " else ""
        var op_winchi =  if (inOrders[i].op_winchi == "Y") "윈찌 " else ""
        var op_danchuk =  if (inOrders[i].op_danchuk == "Y") "단축" else ""
        var strOption = "${op_invertor}${op_guljul}${op_winchi}${op_danchuk}"
        if(strOption  == "") vh.textView_work_option.visibility = View.GONE else vh.textView_work_option.text = "옵션 : " + strOption

        var work_det_1=  if (inOrders[i].work_det_1== "Y") "기타작업 " else ""
        var work_det_2=  if (inOrders[i].work_det_2== "Y") "뿜칠(후끼) " else ""
        var work_det_3=  if (inOrders[i].work_det_3== "Y") "양중작업 " else ""
        var work_det_4=  if (inOrders[i].work_det_4== "Y") "철거작업 " else ""
        var work_det_5=  if (inOrders[i].work_det_5== "Y") "이삿짐 " else ""
        var work_det_6=  if (inOrders[i].work_det_6== "Y") "거리작업 " else ""
        var work_det_7=  if (inOrders[i].work_det_7== "Y") "전지작업 " else ""
        var work_det_8=  if (inOrders[i].work_det_8== "Y") "초보사절" else ""
        var strWorkDetail = "${work_det_1}${work_det_2}${work_det_3}${work_det_4}${work_det_5}${work_det_6}${work_det_7}${work_det_8}"
        if(strWorkDetail  == "") vh.textView_work_detail.visibility = View.GONE else vh.textView_work_detail.text = "작업상세 : " + strWorkDetail

        vh.textView_work_pay.text = "금액 : ${inOrders[i].work_pay}"
        //var payDateMap = util.getHashmapFromResoureces(R.array.pay_date)
        //vh.textView_word_duration.text = "결재기간 : ${payDateMap[inOrders[i].pay_date]?.let{it}?:run{""}}"
        var payPayMap = util.getHashmapFromResoureces(R.array.pay_type)
        vh.textView_pay_type.text = "결제방식 : ${payPayMap[inOrders[i].pay_type]?.let{it}?:run{""}}"

        if (inOrders[i].commission_yn =="Y") {
            vh.linearLayout_left.setBackgroundResource(R.color.pink)
            vh.textViewCommission_yn.text =  context?.getString(R.string.commission_y)
        } else{
            vh.linearLayout_left.setBackgroundResource(R.color.sky)
            vh.textViewCommission_yn.text = context?.getString(R.string.commission_n)
        }
        vh.linearLayout_right.setBackgroundResource(R.color.white)
        for (j in 0..(vh.linearLayout_left.childCount - 1)) {
            (vh.linearLayout_left.getChildAt(j) as TextView)?.let { it.setTextColor(context?.resources?.getColor(R.color.white)!!) }
        }
        inOrders[i].work_proc?.let {
            if ("WP04" == it) {
                vh.linearLayout_left.setBackgroundResource(R.color.grey)
                vh.linearLayout_right.setBackgroundResource(R.color.grey)
                for (j in 0..(vh.linearLayout_left.childCount - 1)) {
                    (vh.linearLayout_left.getChildAt(j) as TextView)?.let { it.setTextColor(context?.resources?.getColor(R.color.black)!!) }
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
        val textView_work_duration: TextView
        val textViewWorkProc: TextView
        val textView_Orderseq:TextView

        val textView_car_length: TextView
        val textView_work_location: TextView
        val textView_work_option: TextView
        val textView_work_detail: TextView
        val textView_work_pay: TextView
        val textView_pay_type: TextView
        val linearLayout_left: LinearLayout
        val linearLayout_right: LinearLayout

        init {
            v.setOnClickListener(this)

            textView_Orderseq = v.findViewById<View>(R.id.textView_Orderseq) as TextView
            textViewCommission_yn = v.findViewById<View>(R.id.textViewCommission_yn) as TextView
            textViewDate = v.findViewById<View>(R.id.textViewDate) as TextView
            textViewTime = v.findViewById<View>(R.id.textViewTime) as TextView
            textViewWorkProc = v.findViewById<View>(R.id.textViewWorkProc) as TextView
            textView_work_duration = v.findViewById<View>(R.id.textView_work_duration) as TextView

            textView_car_length = v.findViewById<View>(R.id.textView_car_length) as TextView
            textView_work_location = v.findViewById<View>(R.id.textView_work_location) as TextView
            textView_work_option = v.findViewById<View>(R.id.textView_work_option) as TextView
            textView_work_detail = v.findViewById<View>(R.id.textView_work_detail ) as TextView
            textView_work_pay = v.findViewById<View>(R.id.textView_work_pay) as TextView
            //textView_word_duration = v.findViewById<View>(R.id.textView_word_duration) as TextView
            textView_pay_type = v.findViewById<View>(R.id.textView_pay_type) as TextView
            linearLayout_left = v.findViewById<View>(R.id.linearLayout_left) as LinearLayout
            linearLayout_right = v.findViewById<View>(R.id.linearLayout_right) as LinearLayout
        }

        override fun onClick(view: View) {
            mListener.onClick(view)
        }
    }
}
