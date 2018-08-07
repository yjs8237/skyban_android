package com.nycompany.skyban

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nycompany.skyban.dto.QnaDetailDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqQnaDetail
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_qna_detail.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QnaDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qna_detail)

        var intent = intent
        var seq = intent.getStringExtra("seq")
        val util = ContextUtil(this)
        val paramObject = JSONObject()
        paramObject.put("cell_no", getUserinfo()?.cell_no)
        paramObject.put("qna_seq", seq)
        val loading: AlertDialog = SpotsDialog.Builder().setContext(this).build()
        loading.show()

        val reqString = paramObject.toString()
        val server = RetrofitCreater.getMyInstance()?.create(ReqQnaDetail::class.java)
        server?.postRequest(reqString)?.enqueue(object : Callback<QnaDetailDTO> {
            override fun onFailure(call: Call<QnaDetailDTO>, t: Throwable) {
                loading.dismiss()
                val msg = if (!util.isConnected()) getString(R.string.network_eror) else t.toString()
                util.buildDialog("eror", msg).show()
            }

            override fun onResponse(call: Call<QnaDetailDTO>, response: Response<QnaDetailDTO>) {
                response.body()?.let {
                    loading.dismiss()
                    if (it.result == ResCode.Success.Code) {
                        textView_title.text = it?.title?:run { "" }
                        textView_reg_date.text = it?.reg_date?:run { "" }
                        textView_content.text = it?.content?:run { "" }
                        textView_reply.text = it?.reply?:run { "" }

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
