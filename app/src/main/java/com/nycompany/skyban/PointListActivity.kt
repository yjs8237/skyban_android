package com.nycompany.skyban

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.nycompany.skyban.adapter.PointRecyclerViewAdapter
import com.nycompany.skyban.dto.PointDTO
import com.nycompany.skyban.dto.PointList
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqPointHistory
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_point_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PointListActivity : AppCompatActivity() {

    private val points: ArrayList<PointList> = ArrayList()
    private lateinit var mAdapter: PointRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_list)

        imageView_Back.setOnClickListener {
            finish()
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_point.setLayoutManager(layoutManager)
        recycler_point.setHasFixedSize(true)

        mAdapter = PointRecyclerViewAdapter(points)

        val paramObject = JSONObject()
        paramObject.put("cell_no", getUserinfo()?.cell_no)
        paramObject.put("user_pwd", getUserinfo()?.password)
        var reqString = paramObject.toString()
        val util = ContextUtil(this)

        var server = RetrofitCreater.getMyInstance()?.create(ReqPointHistory::class.java)
        val loading: AlertDialog = SpotsDialog.Builder().setContext(this).build()
        loading.show()
        server?.postRequest(reqString)?.enqueue(object: Callback<PointDTO> {
            override fun onFailure(call: Call<PointDTO>, t: Throwable) {
                loading.dismiss()
                var msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                util.buildDialog("eror", msg).show()
            }

            override fun onResponse(call: Call<PointDTO>, response: Response<PointDTO>) {
                response.body()?.let {
                    if (response.body()?.result == ResCode.Success.Code) {
                        response.body()?.list?.let {
                            points.addAll(it)
                            mAdapter.notifyDataSetChanged()
                            recycler_point.adapter = mAdapter
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
