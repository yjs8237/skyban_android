package com.nycompany.skyban

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.beardedhen.androidbootstrap.BootstrapButton
import kotlinx.android.synthetic.main.fragment_outorder.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OutorderFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OutorderFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class OutorderFragment : Fragment(), View.OnClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outorder, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cal = GregorianCalendar()
        val mYear = cal.get(Calendar.YEAR)
        val mMonth = cal.get(Calendar.MONTH)
        val mDay = cal.get(Calendar.DAY_OF_MONTH)
        val mHour = cal.get(Calendar.HOUR_OF_DAY)
        val mMinute = cal.get(Calendar.MINUTE)

        val mDateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            EditTextDay.setText(String.format("%d-%d-%d",year, monthOfYear + 1, dayOfMonth))
        }

        val mTimeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
            EditTextTime.setText(String.format("%d:%s", hourOfDay, if(minute > 9) minute.toString() else "0" + minute.toString()))
        }

        EditTextDay.setOnClickListener{
            DatePickerDialog(activity, mDateSetListener, mYear, mMonth, mDay).show()
        }

        EditTextTime.setOnClickListener{
            TimePickerDialog(activity, mTimeSetListener, mHour, mMinute, false).show()
        }

        Button_RadioDay.setOnClickListener(this)
        Button_RadioMorning.setOnClickListener(this)
        Button_RadioSunset.setOnClickListener(this)
        Button_Radio1h.setOnClickListener(this)
        Button_Radio2h.setOnClickListener(this)
        Button_RadioNight.setOnClickListener(this)
        Button_RadioDay.isSelected = true
    }

    override fun onClick(btn: View?) {
        Button_RadioDay.isSelected = false
        Button_RadioMorning.isSelected = false
        Button_RadioSunset.isSelected = false
        Button_Radio1h.isSelected = false
        Button_Radio2h.isSelected = false
        Button_RadioNight.isSelected = false
        btn?.isSelected = true
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OutorderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                OutorderFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
