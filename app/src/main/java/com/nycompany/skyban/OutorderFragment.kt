package com.nycompany.skyban

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.app.Fragment
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beardedhen.androidbootstrap.BootstrapButton
import com.nycompany.skyban.dto.InOrderdetailDTO
import com.nycompany.skyban.dto.OrderRegisterDTO
import com.nycompany.skyban.dto.RegisteragainDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqOrderRegister
import com.nycompany.skyban.network.ReqRegisteragain
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import com.nycompany.skyban.util.MyUtil
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_outorder.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OutorderFragment : Fragment(), View.OnClickListener{
    private val REQUEST_MAP = 101
    private val paramObject by lazy{JSONObject()}
    private val util by lazy{ ContextUtil(activity) }
    private val mLoading by lazy{ SpotsDialog.Builder().setContext(activity).build()}
    private val userinfo by lazy{ getUserinfo() }
    private var oldOrderDTO:InOrderdetailDTO? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mLoading.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         arguments?.let{ oldOrderDTO = it.getParcelable("detailDTO") as InOrderdetailDTO }
        return inflater.inflate(R.layout.fragment_outorder, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoading.dismiss()
        if(!util.isConnected()) util.buildDialog("eror",getString(R.string.network_eror)).show()

        inttUI()
        oldOrderDTO?.let { setOldOredData(it) }
    }

    private fun inttUI(){
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
        EditText_Addr.setOnClickListener {
            val intent = Intent(activity, AddrSearchActivity::class.java)
            startActivityForResult(intent, REQUEST_MAP)
        }

        //작업금액
        Button_WorkPay1.setOnClickListener{
            EditWorkPay.setText((EditWorkPay.text.toString().toIntOrNull()?.let { it +  10000}?:run { 10000 }).toString())
        }

        Button_WorkPay5.setOnClickListener{
            EditWorkPay.setText((EditWorkPay.text.toString().toIntOrNull()?.let { it +  50000}?:run { 50000 }).toString())
        }

        Button_WorkPay10.setOnClickListener{
            EditWorkPay.setText((EditWorkPay.text.toString().toIntOrNull()?.let { it +  100000}?:run { 100000 }).toString())
        }

        Button_WorkPayDelete.setOnClickListener{
            EditWorkPay.setText("")
        }


        //차량정보
        Button_SelectMaxType.setOnClickListener {
            makeCarTypelengthDialog(true).show()
        }

        Button_SelectMinType.setOnClickListener {
            makeCarTypelengthDialog(false).show()
        }

        //전고
        val car_heights = util.getValueArryFromResoureces(R.array.car_height)
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
        Button_RadioPaySign.setOnCheckedChangedListener { bootstrapButton, isChecked ->
            Button_SelectPayDate.isEnabled = isChecked
            if(!isChecked) Button_SelectPayDate.text = "결제기간"
        }

        val arryList = util.getValueArryFromResoureces(R.array.pay_date).toCollection(ArrayList())
        arryList.removeAt(0)
        val pay_dates = arryList.toArray(arrayOfNulls<String>(arryList.size))
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
        Button_Order.setOnClickListener{
            paramObject.put("cell_no", userinfo?.cell_no)
            setJsonParmFromUI()
            val ad = util.buildDialog( "발주 하시겠습니까?")
            ad.setNegativeButton("아니요", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    return
                }
            })
            ad.setPositiveButton("예", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    oldOrderDTO?.let {
                        paramObject.put("order_seq", it.order_seq)
                        reqRegisterAgain(paramObject.toString())
                    }?:run {
                        reqRegister(paramObject.toString())
                    }
                }
            })
            ad.show()
        }
    }

    fun setOldOredData(data:InOrderdetailDTO){
        Button_commission_y.isSelected = (data.commission_yn == "Y")
        val date = data.work_date?.split(" ")?.get(0)?.trim()
        var time = data.work_date?.split(" ")?.get(1)?.trim()
        time = time?.removeRange(5, time?.length)
        EditTextDay.setText(date)
        EditTextTime.setText(time)

        Button_RadioDay.isSelected = false
        when(data.work_duration){
            "A001" -> Button_RadioDay.isSelected = true
            "A002" -> Button_RadioMorning.isSelected = true
            "A003" -> Button_RadioSunset.isSelected = true
            "A004" -> Button_Radio1h.isSelected = true
            "A005" -> Button_Radio2h.isSelected = true
            "A006" -> Button_RadioNight.isSelected = true
        }
        EditWorkPay.setText(data.work_pay)

        Button_RadioNo.isSelected = false
        when(data.ext_charge){
            "C001" -> Button_RadioNo.isSelected = true
            "C002" -> Button_Radio5.isSelected = true
            "C003" -> Button_Radio10.isSelected = true
        }

        EditText_Addr.setText(data.work_location)
        EditText_AddrD.setText(data.work_location_detail)

        var dic = util.getHashmapFromResoureces(R.array.car_type)
        Button_SelectMinType.text = dic.get(data.min_car_type)
        Button_SelectMaxType.text = dic.get(data.max_car_type)

        dic = util.getHashmapFromResoureces(R.array.car_length)
        Button_SelectMinLength.text = dic.get(data.min_car_length)
        Button_SelectMaxLength.text = dic.get(data.max_car_length)

        dic = util.getHashmapFromResoureces(R.array.car_height)
        Button_SelectHeight.text = dic.get(data.car_height)

        Button_CheckInvertor.isSelected = (data.op_invertor == "Y")
        Button_CheckGuljul.isSelected = (data.op_guljul == "Y")
        Button_CheckWinchi.isSelected = (data.op_winchi == "Y")
        Button_CheckDanchuk.isSelected = (data.op_danchuk == "Y")

        Button_RadioPay.isSelected = false
        when(data.pay_type){
            "M001" -> Button_RadioPay.isSelected = true
            "M002" -> {
                Button_SelectPayDate.isEnabled = true
                Button_RadioPaySign.isSelected = true
                dic = util.getHashmapFromResoureces(R.array.pay_date)
                Button_SelectPayDate.text = dic.get (data.pay_date)
            }
        }

        EditText_WorkContact.setText(data.work_contact)

        //추가옵션
        Button_work_det_1.isSelected = (data.work_det_1 == "Y")
        Button_work_det_2.isSelected = (data.work_det_2 == "Y")
        Button_work_det_3.isSelected = (data.work_det_3 == "Y")
        Button_work_det_4.isSelected = (data.work_det_4 == "Y")
        Button_work_det_5.isSelected = (data.work_det_5 == "Y")
        Button_work_det_6.isSelected = (data.work_det_6 == "Y")
        Button_work_det_7.isSelected = (data.work_det_7 == "Y")
        Button_work_det_8.isSelected = (data.work_det_8 == "Y")

        //메모
        EditText_WorkContent.setText(data.work_content)

        paramObject.put("work_latitude", data.work_latitude)
        paramObject.put("work_longitude", data.work_longitude)
    }

    fun setJsonParmFromUI(){
        //수수료
        val paramYN = if (Button_commission_y.isSelected) "Y" else "N"
        paramObject.put("commission_yn", paramYN)

        //작업일시
        val day = EditTextDay.text.toString()
        val time = EditTextTime.text.toString()
        if(day != "" &&   time != ""){
            paramObject.put("work_date", day + " " + time + ":00" )
        }

        //작업시간
        var dic = util.getHashmapFromResoureces(R.array.work_duration)
        (getSelectedView(ButtonGroup_JobTimeTop))?.let {
            var value = MyUtil.getDicKeyFromValue(dic, (it as BootstrapButton).text.toString())
            paramObject.put("work_duration", value)
        }?:run{
            (getSelectedView(ButtonGroup_JobTimeBottom))?.let {
                var value = MyUtil.getDicKeyFromValue(dic, (it as BootstrapButton).text.toString())
                paramObject.put("work_duration", value)
            }
        }

        //get 작업금액
        val pay = EditWorkPay.text.toString()
        if(pay != ""){
            paramObject.put("work_pay", pay)
        }

        //추가요금
        dic = util.getHashmapFromResoureces(R.array.ext_charge)
        (getSelectedView(ButtonGroup_Charge))?.let {
            var value = MyUtil.getDicKeyFromValue(dic, (it as BootstrapButton).text.toString())
            paramObject.put("ext_charge", value)
        }

        //get 장소
        val addr = EditText_Addr.text.toString().trim()
        if(addr != ""){
            paramObject.put("work_location", addr)
        }
        val addrD = EditText_AddrD.text.toString().trim()
        if(addrD != ""){
            paramObject.put("work_location_detail", addrD )
        }

        //차량정보
        dic = util.getHashmapFromResoureces(R.array.car_type)
        MyUtil.getDicKeyFromValue(dic, Button_SelectMinType.text.toString())?.let {
            paramObject.put("min_car_type", it)
        }
        MyUtil.getDicKeyFromValue(dic, Button_SelectMaxType.text.toString())?.let {
            paramObject.put("max_car_type", it)
        }
        dic = util.getHashmapFromResoureces(R.array.car_length)
        MyUtil.getDicKeyFromValue(dic, Button_SelectMinLength.text.toString())?.let {
            paramObject.put("min_car_length", it)
        }
        MyUtil.getDicKeyFromValue(dic, Button_SelectMaxLength.text.toString())?.let {
            paramObject.put("max_car_length", it)
        }

        //get 전고
        dic = util.getHashmapFromResoureces(R.array.car_height)
        MyUtil.getDicKeyFromValue(dic, Button_SelectHeight.text.toString())?.let {
            paramObject.put("car_height", it)
        }

        //차옵션
        lateinit var strXY: String
        if(Button_CheckInvertor.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("op_invertor", strXY)
        if(Button_CheckGuljul.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("op_guljul", strXY)
        if(Button_CheckWinchi.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("op_winchi", strXY)
        if(Button_CheckDanchuk.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("op_danchuk", strXY)

        //get 결제방법
        dic = util.getHashmapFromResoureces(R.array.pay_type)
        (getSelectedView(ButtonGroup_PayType))?.let {
            var value = MyUtil.getDicKeyFromValue(dic, (it as BootstrapButton).text.toString())
            paramObject.put("pay_type", value)
        }

        //get 결제기간
        dic = util.getHashmapFromResoureces(R.array.pay_date)
        MyUtil.getDicKeyFromValue(dic, Button_SelectPayDate.text.toString())?.let {
            paramObject.put("pay_date", it)
        }

        //get 현장연락처
        val contact = EditText_WorkContact.text.toString()
        if(pay != ""){
            paramObject.put("work_contact", contact)
        }

        //추가옵션
        if(Button_work_det_1.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("work_det_1", strXY)
        if(Button_work_det_2.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("work_det_2", strXY)
        if(Button_work_det_3.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("work_det_3", strXY)
        if(Button_work_det_4.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("work_det_4", strXY)
        if(Button_work_det_5.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("work_det_5", strXY)
        if(Button_work_det_6.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("work_det_6", strXY)
        if(Button_work_det_7.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("work_det_7", strXY)
        if(Button_work_det_8.isSelected) strXY = "Y" else strXY = "N"
        paramObject.put("work_det_8", strXY)

        //메모
        val content = EditText_WorkContent.text.toString()
        if(content!="") paramObject.put("work_content", content)

        //연결가능여부 UI없이 데이터만 바인딩 향후 적용여부 필요
        paramObject.put("work_transfer", "Y")
    }

    fun getSelectedView(group:ViewGroup):View? {
        for(i in 0..group.childCount - 1){
            if (group.getChildAt(i).isSelected){
                return group.getChildAt(i)
            }
        }
        return null
    }

    fun makeCarTypelengthDialog(isMaxBoolean: Boolean):AlertDialog.Builder{
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

        var dilog = AlertDialog.Builder(activity).apply {
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
        return dilog
    }

    private fun reqRegister(reqString:String){
        val server = RetrofitCreater.getMyInstance()?.create(ReqOrderRegister::class.java)
        server?.postRequest(reqString)?.enqueue(object: Callback<OrderRegisterDTO> {
            override fun onFailure(call: Call<OrderRegisterDTO>, t: Throwable) {
                val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                util.buildDialog("eror", msg).show()
            }

            override fun onResponse(call: Call<OrderRegisterDTO>, response: Response<OrderRegisterDTO>) {
                response.body()?.let {
                    if(it.result == ResCode.Success.Code) {
                        util.buildDialog("완료","성공적으로 발주 되었습니다").show()

                        val fm = fragmentManager.beginTransaction()
                        fm.remove(this@OutorderFragment).replace(R.id.fragmentContainer , OutorderFragment.newInstance()).commit()
                        updateUserInfo(userinfo?.cell_no, userinfo?.password)
                        //발주리스트로 이동
                        //startActivity(Intent().setClass(this@LoginActivity, MainActivity::class.java))
                        MainActivity.instance()?.moveOutorderHistory()
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

    private fun reqRegisterAgain(reqString:String){
        val server = RetrofitCreater.getMyInstance()?.create(ReqRegisteragain::class.java)
        server?.postRequest(reqString)?.enqueue(object: Callback<RegisteragainDTO> {
            override fun onFailure(call: Call<RegisteragainDTO>, t: Throwable) {
                val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                util.buildDialog("eror", msg).show()
            }

            override fun onResponse(call: Call<RegisteragainDTO>, response: Response<RegisteragainDTO>) {
                response.body()?.let {
                    if(it.result == ResCode.Success.Code) {
                        util.buildDialog("완료","성공적으로 발주 되었습니다").show()

                        val fm = fragmentManager.beginTransaction()
                        fm.remove(this@OutorderFragment).replace(R.id.fragmentContainer , OutorderFragment.newInstance()).commit()
                        updateUserInfo(userinfo?.cell_no, userinfo?.password)
                        //발주리스트로 이동
                        //startActivity(Intent().setClass(this@LoginActivity, MainActivity::class.java))
                        MainActivity.instance()?.moveOutorderHistory()
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
                EditText_Addr.setText(data?.getStringExtra("addr"))
                //String.format("%.4f", data?.getDoubleExtra("latitude", 0.0))
                paramObject.put("work_latitude", data?.getDoubleExtra("latitude", 0.0).toString())
                paramObject.put("work_longitude", data?.getDoubleExtra("longitude", 0.0).toString())
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
        fun newInstance() = OutorderFragment()
    }
}
