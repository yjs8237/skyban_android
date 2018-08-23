package com.nycompany.skyban

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqPersonalInfoUpdateServer
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import kotlinx.android.synthetic.main.activity_update_personal.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePersonalActivity : AppCompatActivity() {
    private val REQUEST_MAP = 101
    private  val paramObject by lazy{ JSONObject() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_personal)

        EditText_Addr.setOnClickListener {
            val intent = Intent(this, AddrSearchActivity::class.java)
            startActivityForResult(intent, REQUEST_MAP)
        }

        imageView_Back.setOnClickListener {
            finish()
        }

        Button_Change.setOnClickListener{
            paramObject.put("cell_no", getUserinfo()?.cell_no)
            paramObject.put("user_name", EditText_Name.text)

            if(EditText_Addr.text.toString() != "" || EditText_Name.text.toString() != "") {
                val server = RetrofitCreater.getMyInstance()?.create(ReqPersonalInfoUpdateServer::class.java)
                val util = ContextUtil(this)
                val reqString = paramObject.toString()

                server?.postRequest(reqString)?.enqueue(object : Callback<CommonDTO> {
                    override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                        val msg = if (!util.isConnected()) getString(R.string.network_eror) else t.toString()
                        util.buildDialog("eror", msg).show()
                    }

                    override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                        response.body()?.let {
                            if (it.result == ResCode.Success.Code) {
                                updateUserInfo(getUserinfo()?.cell_no, getUserinfo()?.password)
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
            }else{
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(REQUEST_MAP == requestCode)
            if(resultCode == Activity.RESULT_OK){
                EditText_Addr.setText(data?.getStringExtra("addr"))
                //String.format("%.4f", data?.getDoubleExtra("latitude", 0.0))
                paramObject.put("work_latitude", data?.getDoubleExtra("latitude", 0.0).toString())
                paramObject.put("work_longitude", data?.getDoubleExtra("longitude", 0.0).toString())
            }
    }
}


