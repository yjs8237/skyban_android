package com.nycompany.skyban


import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_charge.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ChargeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_charge, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Button_charge.setOnCheckedChangedListener { bootstrapButton, isChecked ->
            if(isChecked) {
                LinearLayout_charge.visibility = View.VISIBLE
                LinearLayout_refund.visibility = View.GONE
            }
        }

        Button_refund.setOnCheckedChangedListener { bootstrapButton, isChecked ->
            if(isChecked) {
                LinearLayout_charge.visibility = View.GONE
                LinearLayout_refund.visibility = View.VISIBLE
            }
        }
    }
}
