package com.nycompany.skyban

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqPremium
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import kotlinx.android.synthetic.main.activity_premium.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PremiumActivity : AppCompatActivity() {

    private  val paramObject by lazy{ JSONObject() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)

        val userInfo = getUserinfo()

        paramObject.put("cell_no", userInfo?.cell_no)
        textView_Cash.text = userInfo?.cash

        Button_RadioCash.setOnCheckedChangedListener { bootstrapButton, isChecked ->
            if(isChecked) {
                EditText_Cash.setText("")
                EditText_Name.setText("")
                View_Deposit.visibility = View.GONE
                LinearLayout.requestFocus()
            }
        }

        Button_RadioDeposit.setOnCheckedChangedListener { bootstrapButton, isChecked ->
            if(isChecked) {
                View_Deposit.visibility = View.VISIBLE
            }
        }

        Button_Order.setOnClickListener {
            paramObject.put("month", EditText_Term.text)
            paramObject.put("req_type", "1")

            if(Button_RadioDeposit.isSelected){
                paramObject.put("req_type", "2")
                paramObject.put("cash", EditText_Cash.text)
                paramObject.put("acc_name", EditText_Name.text)
            }

            val server = RetrofitCreater.getMyInstance()?.create(ReqPremium::class.java)
            val cUtil = ContextUtil(this)
            val reqString = paramObject.toString()

            server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    val msg = if(!cUtil.isConnected()) getString(R.string.network_eror) else t.toString()
                    cUtil.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                    response.body()?.let {
                        if(it.result == ResCode.Success.Code) {
                            updateUserInfo(userInfo?.cell_no, userInfo?.password)
                            val db = cUtil.buildDialog("완료", "등록 되었습니다")
                            db.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    finish()
                                }
                            })
                            db.setCancelable(false)
                            db.show()
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

        imageView_Back.setOnClickListener {
            finish()
        }
    }
}
