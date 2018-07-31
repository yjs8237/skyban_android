package com.nycompany.skyban

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.nycompany.skyban.adapter.NotificationRecyclerViewAdapter
import com.nycompany.skyban.dto.NotiDetailDTO
import com.nycompany.skyban.dto.NotificationDTO
import com.nycompany.skyban.dto.NotificationList
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqNotificationList
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_order_history.*
import kotlinx.android.synthetic.main.recyclerview_notification_item.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class NotificationActivity : AppCompatActivity() {

    private val notiList: ArrayList<NotificationList> = ArrayList()
    private lateinit var myAdapter: NotificationRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val layoutManager = LinearLayoutManager(this.applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_order.setLayoutManager(layoutManager)
        recycler_order.setHasFixedSize(true)
        val paramObject = JSONObject()

        myAdapter = NotificationRecyclerViewAdapter(notiList)
        setRecyclerView(true, makeJson(true, paramObject))

        swipyrefreshlayout.setOnRefreshListener(SwipyRefreshLayout.OnRefreshListener { direction ->
            if (direction == SwipyRefreshLayoutDirection.TOP) setRecyclerView(true, makeJson(true, paramObject))
            else setRecyclerView(false, makeJson(false, paramObject))

            if (swipyrefreshlayout.isRefreshing) swipyrefreshlayout.isRefreshing = false
        })

        imageView_Back.setOnClickListener {
            finish()
        }
    }

    fun makeJson(isReset:Boolean, paramObject:JSONObject):JSONObject{
        paramObject.put("search_type", "1")
        if(isReset) paramObject.put("start_index", 0)
        else paramObject.put("start_index", paramObject.get("start_index") as Int + 20)
        paramObject.put("search_count", 20)

        return  paramObject
    }

    fun setRecyclerView(isReset:Boolean, paramObject:JSONObject){
        var server = RetrofitCreater.getMyInstance()?.create(ReqNotificationList::class.java)

        var reqString = paramObject.toString()
        val util = ContextUtil(this)

        val loading: AlertDialog = SpotsDialog.Builder().setContext(this).build()
        loading.show()

        server?.postRequest(reqString)?.enqueue(object: Callback<NotificationDTO> {
            override fun onFailure(call: Call<NotificationDTO>, t: Throwable) {
                loading.dismiss()
                var msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                util.buildDialog("eror", msg).show()
            }

            override fun onResponse(call: Call<NotificationDTO>, response: Response<NotificationDTO>) {
                response.body()?.let {
                    if (response.body()?.result == ResCode.Success.Code) {
                        if (isReset) {
                            response.body()?.list?.let {
                                notiList.clear()
                                notiList.addAll(it)
                                myAdapter.notifyDataSetChanged()
                            }
                            recycler_order?.let {
                                it.adapter = myAdapter
                            }
                        } else {
                            response.body()?.list?.let {
                                notiList.addAll<NotificationList>(it)
                                myAdapter.notifyDataSetChanged()
                            }
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

        myAdapter.setClickListener(View.OnClickListener { view ->
            val intent = Intent(this, NotificationDetailActivity::class.java)
            intent.putExtra("seq", view.textView_Notiseq.text.toString())
            startActivity(intent)
        })
    }
}
