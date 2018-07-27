package com.nycompany.skyban

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.nycompany.skyban.adapter.CashRecyclerViewAdapter
import com.nycompany.skyban.dto.CashDTO
import com.nycompany.skyban.dto.CashList
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqCashHistory
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_cash_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class CashListActivity : AppCompatActivity() {

    private val cashs: ArrayList<CashList> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_list)

        imageView_Back.setOnClickListener {
            finish()
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_cash.setLayoutManager(layoutManager)
        recycler_cash.setHasFixedSize(true)

        val paramObject = JSONObject()
        paramObject.put("cell_no", getUserinfo()?.cell_no)
        var reqString = paramObject.toString()
        val util = ContextUtil(this)

        var server = RetrofitCreater.getMyInstance()?.create(ReqCashHistory::class.java)
        val mAdapter = CashRecyclerViewAdapter(cashs)
        val loading: AlertDialog = SpotsDialog.Builder().setContext(this).build()
        loading.show()
        server?.postRequest(reqString)?.enqueue(object: Callback<CashDTO> {
            override fun onFailure(call: Call<CashDTO>, t: Throwable) {
                loading.dismiss()
                var msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                util.buildDialog("eror", msg).show()
            }

            override fun onResponse(call: Call<CashDTO>, response: Response<CashDTO>) {
                response.body()?.let {
                    if (response.body()?.result == ResCode.Success.Code) {
                        response.body()?.list?.let {
                            cashs.addAll(it)
                            mAdapter.notifyDataSetChanged()
                            recycler_cash.adapter = mAdapter
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
    }
}
