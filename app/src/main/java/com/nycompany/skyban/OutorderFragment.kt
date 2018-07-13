package com.nycompany.skyban

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.app.Fragment
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nycompany.skyban.DTO.OrderRegisterDTO
import com.nycompany.skyban.EnumClazz.ResCode
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_outorder.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    private  val util by lazy{ContextUtil(activity)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!util.isConnected()) util.buildDialog("eror",getString(R.string.network_eror)).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outorder, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Realm.getDefaultInstance().use {
            val data = it.where(RealmUserInfo::class.java).findAll()
            paramObject.put("cell_no", data[0]?.cell_no)
        }
        inttUI()
    }

    fun inttUI(){
        val cal = GregorianCalendar()
        val mYear = cal.get(Calendar.YEAR)
        val mMonth = cal.get(Calendar.MONTH)
        val mDay = cal.get(Calendar.DAY_OF_MONTH)
        val mHour = cal.get(Calendar.HOUR_OF_DAY)
        val mMinute = cal.get(Calendar.MINUTE)
        //날짜
        val mDateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            EditTextDay.setText(String.format("%d-%d-%d",year, monthOfYear + 1, dayOfMonth))
            var dayTime= EditTextDay.text.toString() + " " + EditTextTime.text.toString()
        }
        EditTextDay.setOnClickListener{
            DatePickerDialog(activity, mDateSetListener, mYear, mMonth, mDay).show()
        }

        //시간
        val mTimeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
            EditTextTime.setText(String.format("%d:%s", hourOfDay, if(minute > 9) minute.toString() else "0" + minute.toString()))
            var dayTime= EditTextDay.text.toString() + " " + EditTextTime.text.toString()
        }
        EditTextTime.setOnClickListener{
            TimePickerDialog(activity, mTimeSetListener, mHour, mMinute, false).show()
        }

        //작업시간
        Button_RadioDay.setOnClickListener(this)
        Button_RadioMorning.setOnClickListener(this)
        Button_RadioSunset.setOnClickListener(this)
        Button_Radio1h.setOnClickListener(this)
        Button_Radio2h.setOnClickListener(this)
        Button_RadioNight.setOnClickListener(this)
        Button_RadioDay.isSelected = true

        //작업장소
        EditText_Map.setOnClickListener {
            val intent = Intent(activity, AddrSearchActivity::class.java)
            startActivityForResult(intent, REQUEST_MAP)
        }

        //차량정보
        Button_SelectMaxType.setOnClickListener {
            makeCarMinMaxDialog(true).show()
        }

        Button_SelectMinType.setOnClickListener {
            makeCarMinMaxDialog(false).show()
        }

        //전고
        val car_heights = util.getArryFromResoureces(R.array.car_height)
        val dialogHeight = android.support.v7.app.AlertDialog.Builder(activity).apply {
            setTitle("전고")
            setItems(car_heights, DialogInterface.OnClickListener { dialogInterface, i ->
                Button_SelectHeight.text = car_heights[i]
            })
        }
        Button_SelectHeight.setOnClickListener {
            dialogHeight.show()
        }

        //결제기간
        val pay_dates = util.getArryFromResoureces(R.array.pay_date)
        val dialogDate = android.support.v7.app.AlertDialog.Builder(activity).apply {
            setTitle("결제기간")
            setItems(pay_dates, DialogInterface.OnClickListener { dialogInterface, i ->
                Button_SelectPayDate.text = pay_dates[i]
            })
        }
        Button_SelectPayDate.setOnClickListener {
            dialogDate.show()
        }

        //발주버튼
        val server = RetrofitCreater.getInstance(activity)?.create(ReqOrderRegister::class.java)
        Button_Order.setOnClickListener{
            //paramObject.put("user_pwd", editTextPassword.text)
            var paramYN = if (Button_commission_y.isSelected) "Y" else "N"
            paramObject.put("commission_yn", paramYN)
            paramObject.put("work_date", EditTextDay.text.toString() + " " +  EditTextTime.text.toString() + ":00" )
            paramObject.put("work_duration", "100")
            val reqString = paramObject.toString()
            //시간만 받으면 오전 오후는 어떻게?,,,,작업수당 ui가없음,,,,,,,수당 추가요금은 숫자로?,,,,,작업일시 벨리데이션 맴버만 있고 값이 없어도 체크안됨
            server?.postRequest(reqString)?.enqueue(object: Callback<OrderRegisterDTO> {
                override fun onFailure(call: Call<OrderRegisterDTO>, t: Throwable) {
                    val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                    util.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<OrderRegisterDTO>, response: Response<OrderRegisterDTO>) {
                    response.body()?.let {
                        if(it.result == ResCode.Success.Code) {
                            val fm = fragmentManager.beginTransaction()
                            fm.remove(this@OutorderFragment).replace(R.id.fragmentContainer , OutorderFragment.newInstance()).commit()
                            //발주리스트로 이동, UIreset 필요
                            //startActivity(Intent().setClass(this@LoginActivity, MainActivity::class.java))
                        }else{
                            it.description?.let {
                                util.buildDialog(it).show()
                            }
                        }
                    }?:run{
                        Log.e(this::class.java.name, getString(R.string.response_body_eror))
                    }
                }
            })
        }
    }


    fun makeCarMinMaxDialog(isMaxBoolean: Boolean):AlertDialog.Builder{
        var typeItems = resources.getStringArray(R.array.car_type_text)
        var typeTitle = "최소톤수"
        var lenghTitle = "최소길이"
        var  typeButton = Button_SelectMinType
        var  lenghButton = Button_SelectMinLength

        if(isMaxBoolean){
            typeTitle = "최대톤수"
            lenghTitle = "최대길이"
            typeButton = Button_SelectMaxType
            lenghButton = Button_SelectMaxLength
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

    override fun onClick(btn: View?) {
        Button_RadioDay.isSelected = false
        Button_RadioMorning.isSelected = false
        Button_RadioSunset.isSelected = false
        Button_Radio1h.isSelected = false
        Button_Radio2h.isSelected = false
        Button_RadioNight.isSelected = false
        btn?.isSelected = true
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

    override fun onDetach() {
        super.onDetach()
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
        fun newInstance() = OutorderFragment()
    }
}
