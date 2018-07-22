package com.nycompany.skyban

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nycompany.skyban.dto.RealmUserInfo
import com.nycompany.skyban.util.ContextUtil
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_info.*

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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Realm.getDefaultInstance().use {
            val data = it.where(RealmUserInfo::class.java).findAll()
            if(data[0]?.cash.toString() != "") textView_cash.text = String.format("%s cash",data[0]?.cash)
            textView_InorderPoint.text = String.format("%s 수주 point",data[0]?.obtain_point)
            textView_OutorderPoint.text = String.format("%s 발주 point",data[0]?.order_point)
            Button_InOrder.text = String.format("수주완료 %s건",data[0]?.obtain_cnt)
            Button_OutOrder.text = String.format("발주완료 %s건",data[0]?.order_cnt)
            val level = util.getHashmapFromResoureces(R.array.user_level)[data[0]?.user_level]
            textView_User.text = String.format("%s 님 %s",data[0]?.user_name, level)
        }
        Button_InOrder.setOnClickListener {
            MainActivity.instance()?.moveOrderHistory()
        }

        Button_OutOrder.setOnClickListener{
            MainActivity.instance()?.displayOutorderHistory()
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
