package com.nycompany.skyban

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqPremium
import com.nycompany.skyban.network.ReqQnaRegister
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import kotlinx.android.synthetic.main.activity_premium.*
import kotlinx.android.synthetic.main.activity_qna_register.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QnaRegisterActivity : AppCompatActivity() {

    private  val paramObject by lazy{ JSONObject() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qna_register)

        Button_Qna.setOnClickListener {
            val userInfo = getUserinfo()

            paramObject.put("cell_no", userInfo?.cell_no)
            paramObject.put("title", EditText_title.text)
            paramObject.put("content", EditText_content.text)
            if(Button_Radio_open_y.isSelected) paramObject.put("open_yn", "Y")
            else paramObject.put("open_yn", "N")

            val server = RetrofitCreater.getMyInstance()?.create(ReqQnaRegister::class.java)
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
                            val bd = cUtil.buildDialog("완료", "등록 되었습니다")
                            bd.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    finish()
                                }
                            })
                            bd.setCancelable(false)
                            bd.show()
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
