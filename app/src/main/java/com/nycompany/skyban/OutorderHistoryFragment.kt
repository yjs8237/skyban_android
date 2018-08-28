package com.nycompany.skyban


import android.app.AlertDialog
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nycompany.skyban.dto.OrderList
import com.nycompany.skyban.dto.OderDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.adapter.OrderRecyclerViewAdapter
import com.nycompany.skyban.network.ReqMyOrderList
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_outorder_history.*
import kotlinx.android.synthetic.main.recyclerview_inorder_item.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 *
 */
class OutorderHistoryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outorder_history, container, false)
    }

    private val orderHistory: ArrayList<OrderList> = ArrayList()
    private lateinit var myAdapter: OrderRecyclerViewAdapter
    val reqFragType = MainActivity.instance()?.getCurrentFarnment()!!
    private  val paramObject by lazy { JSONObject() }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(activity.applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_order.setLayoutManager(layoutManager)
        recycler_order.setHasFixedSize(true)

        swipyrefreshlayout.setOnRefreshListener(SwipyRefreshLayout.OnRefreshListener { direction ->
            if (direction == SwipyRefreshLayoutDirection.TOP) setRecyclerView(true, makeJson(true, paramObject))
            else setRecyclerView(false, makeJson(false, paramObject))

            if (swipyrefreshlayout.isRefreshing) swipyrefreshlayout.isRefreshing = false
        })

        imageView_Back.setOnClickListener {
            val fragmentManager = activity.fragmentManager
            fragmentManager.popBackStackImmediate()
        }
    }

    override fun onResume() {
        super.onResume()
        paramObject.put("cell_no", getUserinfo()?.cell_no)

        myAdapter = OrderRecyclerViewAdapter(orderHistory, reqFragType)
        setRecyclerView(true, makeJson(true, paramObject))
    }

    fun makeJson(isReset:Boolean, paramObject: JSONObject): JSONObject {
        paramObject.put("search_type", "2")
        if(isReset) paramObject.put("start_index", 0)
        else paramObject.put("start_index", paramObject.get("start_index") as Int + 20)
        paramObject.put("search_count", 20)

        return  paramObject
    }

    fun setRecyclerView(isReset:Boolean, paramObject: JSONObject){
        var server = RetrofitCreater.getMyInstance()?.create(ReqMyOrderList::class.java)

        var reqString = paramObject.toString()
        val util = ContextUtil(activity)

        val loading: AlertDialog = SpotsDialog.Builder().setContext(activity).build()
        loading.show()

        server?.postRequest(reqString)?.enqueue(object: Callback<OderDTO> {
            override fun onFailure(call: Call<OderDTO>, t: Throwable) {
                loading.dismiss()
                var msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                util.buildDialog("eror", msg).show()
            }

            override fun onResponse(call: Call<OderDTO>, response: Response<OderDTO>) {
                response.body()?.let {
                    if (response.body()?.result == ResCode.Success.Code) {
                        if (isReset) {
                            response.body()?.list?.let {
                                orderHistory.clear()
                                orderHistory.addAll(it)
                                myAdapter.notifyDataSetChanged()
                            }
                            recycler_order?.let {
                                it.adapter = myAdapter
                            }
                        } else {
                            response.body()?.list?.let {
                                orderHistory.addAll<OrderList>(it)
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
            Toast.makeText(view.getContext(), "주문번호 ${view.textView_Orderseq.text.toString()}", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, OrderDetailActivity::class.java)
            intent.putExtra("orderseq", view.textView_Orderseq.text.toString())
            //startActivity(intent)
            activity.startActivityForResult(intent, 100)
        })
    }
}
