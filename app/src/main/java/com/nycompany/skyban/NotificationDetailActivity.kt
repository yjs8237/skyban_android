package com.nycompany.skyban

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nycompany.skyban.dto.NotiDetailDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqNotiDetail
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_notification_detail.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_detail)

        var intent = intent
        var seq = intent.getStringExtra("seq")
        val util = ContextUtil(this)
        val paramObject = JSONObject()
        paramObject.put("noti_seq", seq)
        val loading: AlertDialog = SpotsDialog.Builder().setContext(this).build()
        loading.show()

        val reqString = paramObject.toString()
        val server = RetrofitCreater.getMyInstance()?.create(ReqNotiDetail::class.java)
        server?.postRequest(reqString)?.enqueue(object : Callback<NotiDetailDTO> {
            override fun onFailure(call: Call<NotiDetailDTO>, t: Throwable) {
                loading.dismiss()
                val msg = if (!util.isConnected()) getString(R.string.network_eror) else t.toString()
                util.buildDialog("eror", msg).show()
            }

            override fun onResponse(call: Call<NotiDetailDTO>, response: Response<NotiDetailDTO>) {
                response.body()?.let {
                    loading.dismiss()
                    if (it.result == ResCode.Success.Code) {
                        textView_title.text = it.title
                        textView_reg_date.text = it.reg_date
                        textView_content.text = it.content
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

        imageView_Back.setOnClickListener {
            finish()
        }
    }

}