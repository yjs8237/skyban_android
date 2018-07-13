package com.nycompany.skyban

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.beardedhen.androidbootstrap.BootstrapButton
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_outorder.*
import org.json.JSONObject
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
    private val REQUEST_MAP = 101
    private  val paramObject by lazy{JSONObject()}

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Realm.getDefaultInstance().use {
            val data = it.where(RealmUserInfo::class.java).findAll()
            paramObject.put("cell_no", data[0]?.cell_no)
        }

        val cal = GregorianCalendar()
        val mYear = cal.get(Calendar.YEAR)
        val mMonth = cal.get(Calendar.MONTH)
        val mDay = cal.get(Calendar.DAY_OF_MONTH)
        val mHour = cal.get(Calendar.HOUR_OF_DAY)
        val mMinute = cal.get(Calendar.MINUTE)


        val mDateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            EditTextDay.setText(String.format("%d-%d-%d",year, monthOfYear + 1, dayOfMonth))
            var dayTime= EditTextDay.text.toString() + " " + EditTextTime.text.toString()
            paramObject.put("work_date", dayTime)
        }

        val mTimeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
            EditTextTime.setText(String.format("%d:%s", hourOfDay, if(minute > 9) minute.toString() else "0" + minute.toString()))
            var dayTime= EditTextDay.text.toString() + " " + EditTextTime.text.toString()
            paramObject.put("work_date", dayTime)
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

        EditText_Map.setOnClickListener {
            val intent = Intent(activity, AddrSearchActivity::class.java)
            startActivityForResult(intent, REQUEST_MAP)
        }


        Button_MaxType.setOnClickListener {
            makeCarMinMaxDialog(true).show()
        }

        Button_MinType.setOnClickListener {
            makeCarMinMaxDialog(false).show()
        }
    }

    fun makeCarMinMaxDialog(isMaxBoolean: Boolean):AlertDialog.Builder{
        var typeItems = resources.getStringArray(R.array.car_type_text)
        var typeTitle = "최소톤수"
        var lenghTitle = "최소길이"
        var  typeButton = Button_MinType
        var  lenghButton = Button_MinLength

        if(isMaxBoolean){
            typeTitle = "최대톤수"
            lenghTitle = "최대길이"
            typeButton = Button_MaxType
            lenghButton = Button_MaxLength
        }

        var DilogType = AlertDialog.Builder(activity).apply {
            setTitle(typeTitle)
            lateinit var lengthItems: Array<String>
            setItems(typeItems, DialogInterface.OnClickListener { dialogInterface, i ->
                when (i) {
                    0 -> lengthItems = resources.getStringArray(R.array.car_length_T001)
                    1 -> lengthItems = resources.getStringArray(R.array.car_length_T002)
                    2 -> lengthItems = resources.getStringArray(R.array.car_length_T003)
                    3 -> lengthItems = resources.getStringArray(R.array.car_length_T004)
                    4 -> lengthItems = resources.getStringArray(R.array.car_length_T005)
                    5 -> lengthItems = resources.getStringArray(R.array.car_length_T006)
                }
                typeButton.text = typeItems[i]
                lenghButton.text = lengthItems[0]

                var dialogLenth = AlertDialog.Builder(activity).apply {
                    setTitle(lenghTitle);
                    setItems(lengthItems, DialogInterface.OnClickListener { dialogInterface, i ->
                        lenghButton.text = lengthItems[i]
                    })
                }

                lenghButton.setOnClickListener {
                    dialogLenth.show()
                }
            })
        }
        return DilogType
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outorder, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(REQUEST_MAP == requestCode)
            if(resultCode == RESULT_OK){
                EditText_Map.setText(data?.getStringExtra("addr"))
                println(data?.getDoubleExtra("latitude", 0.0))
                println(data?.getDoubleExtra("longitude", 0.0))
            }
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
    }
}
