package com.nycompany.skyban

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqPersonalInfoUpdateServer
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import kotlinx.android.synthetic.main.activity_update_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        imageView_Back.setOnClickListener {
            finish()
        }

        Button_Change.setOnClickListener {
            val util = ContextUtil(this)
            if(EditText_Pass.text.toString() == ""){
                util.buildDialog("변경할 비밀번호를 확인하세요").show()
                return@setOnClickListener
            }
            if(EditText_Pass.text.toString() != EditText_Confirm.text.toString()){
                util.buildDialog("비밀번호와 비밀번호확인이 일치하지 않습니다").show()
            }else {
                val paramObject = JSONObject()
                val server = RetrofitCreater.getMyInstance()?.create(ReqPersonalInfoUpdateServer::class.java)
                val util = ContextUtil(this)

                paramObject.put("cell_no", getUserinfo()?.cell_no)
                paramObject.put("user_pwd", EditText_Pass.text)

                val reqString = paramObject.toString()

                server?.postRequest(reqString)?.enqueue(object : Callback<CommonDTO> {
                    override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                        val msg = if (!util.isConnected()) getString(R.string.network_eror) else t.toString()
                        util.buildDialog("eror", msg).show()
                    }

                    override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                        response.body()?.let {
                            if (it.result == ResCode.Success.Code) {
                                updateUserInfo(getUserinfo()?.cell_no, paramObject.get("user_pwd").toString())
                                val db = util.buildDialog("완료", "변경 되었습니다")
                                db.setPositiveButton("확인", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        finish()
                                    }
                                })
                                db.setCancelable(false)
                                db.show()
                            } else {
                                it.description?.let {
                                    util.buildDialog(it).show()
                                }
                            }
                        } ?: run {
                            Log.e(this::class.java.name, getString(R.string.response_body_eror))
                        }
                    }
                })
            }
        }
    }
}
