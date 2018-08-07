package com.nycompany.skyban

import android.app.AlertDialog
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nycompany.skyban.dto.LoginDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqLogin
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_info.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InfoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class InfoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private  val util by lazy{ ContextUtil(activity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        if(!util.isConnected()) util.buildDialog("eror",getString(R.string.network_eror)).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        setDataUI()
    }

    fun setDataUI(){
        val userinfo = getUserinfo()
        if(userinfo?.cash.toString() != "") textView_cash.text = String.format("%s cash",userinfo?.cash)
        textView_InorderPoint.text = String.format("%s 수주 point",userinfo?.obtain_point)
        textView_OutorderPoint.text = String.format("%s 발주 point",userinfo?.order_point)
        Button_InOrder.text = String.format("수주완료 %s건",userinfo?.obtain_cnt)
        Button_OutOrder.text = String.format("발주완료 %s건",userinfo?.order_cnt)
        val level = util.getHashmapFromResoureces(R.array.user_level)[userinfo?.user_level]
        textView_User.text = String.format("%s 님 %s",userinfo?.user_name, level)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Button_InOrder.setOnClickListener {
            MainActivity.instance()?.moveOrderHistory()
        }

        Button_OutOrder.setOnClickListener{
            MainActivity.instance()?.moveOutorderHistory()
        }

        ButtonPremium.setOnClickListener {
            startActivity(Intent(activity, PremiumActivity::class.java))
        }

        Button_Personal.setOnClickListener {
            startActivity(Intent(activity, UpdatePersonalActivity::class.java))
        }

        Button_pass.setOnClickListener {
            startActivity(Intent(activity, UpdatePasswordActivity::class.java))
        }

        Button_carInfo.setOnClickListener {
            startActivity(Intent(activity, UpdateCarActivity::class.java))
        }

        Button_Point.setOnClickListener {
            startActivity(Intent(activity, PointListActivity::class.java))
        }

        Button_Cash.setOnClickListener {
            startActivity(Intent(activity, CashListActivity::class.java))
        }

        imageView_Refresh.setOnClickListener {
            val loading: AlertDialog = SpotsDialog.Builder().setContext(activity).build()
            loading.show()

            val server = RetrofitCreater.getMyInstance()?.create(ReqLogin::class.java)
            val paramObject = JSONObject()
            val cUtil = ContextUtil(activity)

            paramObject.put("cell_no", getUserinfo()?.cell_no)
            paramObject.put("user_pwd", getUserinfo()?.password)

            val reqString = paramObject.toString()

            server?.postRequest(reqString)?.enqueue(object: Callback<LoginDTO> {
                override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                    val msg = if(!cUtil.isConnected()) activity?.getString(R.string.network_eror) else t.toString()
                    cUtil.buildDialog("eror", msg).show()
                    loading.dismiss()
                }

                override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
                    response.body()?.let {
                        if(it.result == ResCode.Success.Code) {
                            resetUserinfoRealm(it, getUserinfo()?.password!!)
                            setDataUI()
                        }else{
                            Log.e(this::class.java.name, it.description)
                        }
                        loading.dismiss()
                    }?:run{
                        loading.dismiss()
                        Log.e(this::class.java.name, "response body eror")
                    }
                }
            })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                OrderFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
