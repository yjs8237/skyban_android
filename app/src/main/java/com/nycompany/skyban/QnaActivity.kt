package com.nycompany.skyban

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.nycompany.skyban.adapter.QnaRecyclerViewAdapter
import com.nycompany.skyban.dto.QnaList
import com.nycompany.skyban.dto.QnalDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqQna
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_qna.*
import kotlinx.android.synthetic.main.recyclerview_qna_item.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class QnaActivity : AppCompatActivity() {

    private val Qnas: ArrayList<QnaList> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qna)

        imageView_Back.setOnClickListener {
            finish()
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_qna.setLayoutManager(layoutManager)
        recycler_qna.setHasFixedSize(true)

        val paramObject = JSONObject()
        paramObject.put("cell_no", getUserinfo()?.cell_no)
        var reqString = paramObject.toString()
        val util = ContextUtil(this)

        var server = RetrofitCreater.getMyInstance()?.create(ReqQna::class.java)
        var mAdapter = QnaRecyclerViewAdapter(Qnas)
        val loading: AlertDialog = SpotsDialog.Builder().setContext(this).build()
        loading.show()
        server?.postRequest(reqString)?.enqueue(object: Callback<QnalDTO> {
            override fun onFailure(call: Call<QnalDTO>, t: Throwable) {
                loading.dismiss()
                var msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                util.buildDialog("eror", msg).show()
            }

            override fun onResponse(call: Call<QnalDTO>, response: Response<QnalDTO>) {
                response.body()?.let {
                    if (response.body()?.result == ResCode.Success.Code) {
                        response.body()?.list?.let {
                            Qnas.addAll(it)
                            mAdapter.notifyDataSetChanged()
                            recycler_qna.adapter = mAdapter
                        }
                    } else {
                        it.description?.let {
                            util.buildDialog(it).show()
                        }
                    } ?: run {
                        Log.e(this::class.java.name, "postRequest method eror")
                    }
                    //(activity as MainActivity).startFinish()
                }
                loading.dismiss()
            }
        })

        mAdapter.setClickListener(View.OnClickListener { view ->
            val intent = Intent(this, QnaDetailActivity::class.java)
            intent.putExtra("seq", view.textView_qna_seq.text.toString())
            startActivity(intent)
        })
    }
}
