package com.nycompany.skyban


import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.*
import com.nycompany.skyban.util.ContextUtil
import com.nycompany.skyban.util.MyUtil
import kotlinx.android.synthetic.main.fragment_charge.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChargeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_charge, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Button_Radio_charge.setOnCheckedChangedListener { bootstrapButton, isChecked ->
            if(isChecked) {
                LinearLayout_charge.visibility = View.VISIBLE
                LinearLayout_refund.visibility = View.GONE
            }
        }

        Button_Radio_refund.setOnCheckedChangedListener { bootstrapButton, isChecked ->
            if(isChecked) {
                LinearLayout_charge.visibility = View.GONE
                LinearLayout_refund.visibility = View.VISIBLE
            }
        }

        Button_charge.setOnClickListener {
            val paramObject by lazy{ JSONObject() }
            paramObject.put("cell_no", getUserinfo()?.cell_no)
            paramObject.put("cash", EditText_Price.text)
            paramObject.put("acc_name", EditText_ChargeName.text)

            reqServer(true, paramObject.toString())
        }

        Button_refund.setOnClickListener {
            val paramObject by lazy{ JSONObject() }
            paramObject.put("cell_no", getUserinfo()?.cell_no)
            paramObject.put("cash", EditText_ReturnPrice.text)
            paramObject.put("bank_name", EditText_Bank.text)
            paramObject.put("acc_name", EditText_AccountName.text)
            paramObject.put("acc_no", EditText_AccountNo.text)

            reqServer(false, paramObject.toString())
        }
    }

    fun clearEditText(){
        EditText_Price.setText("")
        EditText_ChargeName.setText("")

        EditText_ReturnPrice.setText("")
        EditText_Bank.setText("")
        EditText_AccountName.setText("")
        EditText_AccountNo.setText("")
    }

    fun reqServer(isCahrge:Boolean, reqString:String){
        val cUtil = ContextUtil(activity)
        if(isCahrge){
            val server = RetrofitCreater.getMyInstance()?.create(ReqCashCharge::class.java)
            server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    val msg = if(!cUtil.isConnected()) getString(R.string.network_eror) else t.toString()
                    cUtil.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                    response.body()?.let {
                        if(it.result == ResCode.Success.Code) {
                            cUtil.buildDialog("완료", "등록 되었습니다").show()
                            clearEditText()
                        }else{
                            it.description?.let {
                                cUtil.buildDialog(it).show()
                            }
                        }
                    }?:run{
                        Log.e(this::class.java.name, getString(R.string.response_body_eror))
                    }
                }
            })
        }else{
            val server = RetrofitCreater.getMyInstance()?.create(ReqCashRefund::class.java)
            server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    val msg = if(!cUtil.isConnected()) getString(R.string.network_eror) else t.toString()
                    cUtil.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                    response.body()?.let {
                        if(it.result == ResCode.Success.Code) {
                            cUtil.buildDialog("완료", "등록 되었습니다").show()
                            clearEditText()
                        }else{
                            it.description?.let {
                                cUtil.buildDialog(it).show()
                            }
                        }
                    }?:run{
                        Log.e(this::class.java.name, getString(R.string.response_body_eror))
                    }
                }
            })
        }

    }
}
