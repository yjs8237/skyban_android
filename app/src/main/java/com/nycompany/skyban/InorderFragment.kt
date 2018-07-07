package com.nycompany.skyban

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.nycompany.skyban.DTO.InoderDTO
import com.nycompany.skyban.DTO.List
import com.nycompany.skyban.EnumClazz.ResCode
import com.nycompany.skyban.EnumClazz.codeToStr
import kotlinx.android.synthetic.main.fragment_inorder.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InorderFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InorderFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class InorderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var myAdapter: InorderRecyclerViewAdapter? = null
    private var inOrders: ArrayList<List>? = ArrayList()
    private var app:Application = Application()
    val paramObject = JSONObject()
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //var context: Context = activity
        val server = RetrofitCreater.getInstance(view!!.context)?.create(ReqOderList::class.java)

        recycler_inorder.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity.applicationContext)
        recycler_inorder.setLayoutManager(layoutManager)
        initRecyclerViewData(server)

        swipyrefreshlayout.setOnRefreshListener(SwipyRefreshLayout.OnRefreshListener { direction ->
            if (direction == SwipyRefreshLayoutDirection.BOTTOM){
                paramObject.put("search_type", "1")
                paramObject.put("start_index", paramObject.get("start_index") as Int + 5)
                paramObject.put("search_count",  5)
                var reqString = paramObject.toString()
                server?.postRequest(reqString)?.enqueue(object: Callback<InoderDTO> {
                    override fun onFailure(call: Call<InoderDTO>?, t: Throwable?) {
                        var msg = if(!app.isConnected(view!!.context)) getString(R.string.network_eror) else t.toString()
                        app.buildDialog(view!!.context, "eror", msg).show()
                    }

                    override fun onResponse(call: Call<InoderDTO>?, response: Response<InoderDTO>?) {
                        if(response?.body()?.result == ResCode.Success.Code) {
                            inOrders?.addAll<List>(response?.body()?.list!!)
                            myAdapter?.notifyDataSetChanged()

                            if (swipyrefreshlayout.isRefreshing()) swipyrefreshlayout.setRefreshing(false)
                        }else{
                            app.buildDialog(view!!.context, codeToStr(response?.body()?.result)).show()
                        }
                    }
                })
            }else{
                initRecyclerViewData(server)
                if (swipyrefreshlayout.isRefreshing()) swipyrefreshlayout.setRefreshing(false)
            }
        })
    }

    fun initRecyclerViewData(server:ReqOderList?){
        paramObject.put("search_type", "1")
        paramObject.put("start_index", 0)
        paramObject.put("search_count", 5)
        var reqString = paramObject.toString()

        server?.postRequest(reqString)?.enqueue(object: Callback<InoderDTO> {
            override fun onFailure(call: Call<InoderDTO>?, t: Throwable?) {
                var msg = if(!app.isConnected(view!!.context)) getString(R.string.network_eror) else t.toString()
                app.buildDialog(view!!.context, "eror", msg).show()
            }

            override fun onResponse(call: Call<InoderDTO>?, response: Response<InoderDTO>?) {
                if(response?.body()?.result == ResCode.Success.Code) {
                    inOrders = response?.body()?.list

                    myAdapter = InorderRecyclerViewAdapter(inOrders)
                    myAdapter?.setClickListener (View.OnClickListener { view ->
                        Toast.makeText(view.getContext(), "Position ${recycler_inorder.indexOfChild(view)}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, InorderDetailActivity::class.java)
                        startActivity(intent)
                    })
                    recycler_inorder.adapter = myAdapter
                }else{
                    app.buildDialog(view!!.context, codeToStr(response?.body()?.result)).show()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inorder, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InorderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                InorderFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
