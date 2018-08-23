package com.nycompany.skyban

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqRegister
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import com.nycompany.skyban.util.MyUtil
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private val REQUEST_MAP = 101
    private val util by lazy{ContextUtil(this)}
    private var latitude:String? = null
    private var longitude:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imageView_Back.setOnClickListener{
            finish()
        }

        Button_commission_y.setOnCheckedChangedListener { bootstrapButton, isChecked ->
            if(isChecked){
                ConstraintLayout_CarType.visibility = View.VISIBLE
                EditText_Addr.visibility = View.VISIBLE
                EditText_CopNumber.visibility = View.VISIBLE
            } else {
                ConstraintLayout_CarType.visibility = View.GONE
                EditText_Addr.visibility = View.GONE
                EditText_CopNumber.visibility = View.GONE
            }
        }

        //주소 init
        EditText_Addr.setOnClickListener {
            val intent = Intent(this, AddrSearchActivity::class.java)
            startActivityForResult(intent, REQUEST_MAP)
        }

        //차량정보 버튼 init
        Button_SelectType.setOnClickListener {
            makeCarTypelengthDialog().show()
        }

        //전고 init
        val car_heights = util.getValueArryFromResoureces(R.array.car_height)
        val dialogHeight = android.support.v7.app.AlertDialog.Builder(this).apply {
            setTitle("전고")
            setItems(car_heights, DialogInterface.OnClickListener { dialogInterface, i ->
                Button_SelectHeight.text = car_heights[i]
            })
        }
        Button_SelectHeight.setOnClickListener {
            dialogHeight.show()
        }

        Button_Register.setOnClickListener {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this, OnSuccessListener<InstanceIdResult> { instanceIdResult ->
                val paramObject = JSONObject()
                paramObject.put("user_type", "2")
                paramObject.put("user_name", EditText_Name.text.toString().trim())
                paramObject.put("cell_no", EditText_CellNo.text.toString().trim())
                paramObject.put("user_pwd", EditText_Pass.text.toString().trim())
                paramObject.put("email", EditText_Email.text.toString().trim())
                paramObject.put("token", instanceIdResult.token)

                if(Button_commission_y.isSelected){
                    paramObject.put("cop_number", EditText_CopNumber.text.toString().trim())
                    latitude?.let { paramObject.put("latitude", it)}
                    longitude?.let { paramObject.put("longitude", it)}
                    paramObject.put("user_type", "1")
                    //차량정보
                    var dic = util.getHashmapFromResoureces(R.array.car_type)
                    MyUtil.getDicKeyFromValue(dic, Button_SelectType.text.toString())?.let {
                        paramObject.put("car_type", it)
                    }
                    dic = util.getHashmapFromResoureces(R.array.car_length)
                    MyUtil.getDicKeyFromValue(dic, Button_SelectLength.text.toString())?.let {
                        paramObject.put("car_length", it)
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

                    paramObject.put("recomm_cell_no", EditTextTime_recommNo.text.toString().trim())
                }

                val reqString = paramObject.toString()
                val server = RetrofitCreater.getMyInstance()?.create(ReqRegister::class.java)

                server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
                    override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                        val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                        util.buildDialog("eror", msg).show()
                    }

                    override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                        response.body()?.let {
                            if(it.result == ResCode.Success.Code) {
                                val bd = util.buildDialog("완료", "가입 되었습니다")
                                bd.run {
                                    setPositiveButton("확인", object : DialogInterface.OnClickListener {
                                        override fun onClick(p0: DialogInterface?, p1: Int) {
                                            startActivity(Intent().setClass(this@RegisterActivity, LoginActivity::class.java))
                                            finish()
                                        }
                                    })
                                    setCancelable(false)
                                    return@run show()
                                }
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
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(REQUEST_MAP == requestCode)
            if(resultCode == Activity.RESULT_OK){
                EditText_Addr.setText(data?.getStringExtra("addr"))
                //String.format("%.4f", data?.getDoubleExtra("latitude", 0.0))
                latitude = data?.getDoubleExtra("latitude", 0.0).toString()
                longitude = data?.getDoubleExtra("longitude", 0.0).toString()
            }
    }

    fun makeCarTypelengthDialog(): AlertDialog.Builder{
        var typeItems = resources.getStringArray(R.array.car_type_text)
        var typeTitle = "차량톤수"
        var lenghTitle = "차량길이"

        var dilog = AlertDialog.Builder(this).apply {
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
                Button_SelectType.text = typeItems[i]
                Button_SelectLength.text = lengthItems[0]

                var dialogLenth = AlertDialog.Builder(this@RegisterActivity).apply {
                    setTitle(lenghTitle);
                    setItems(lengthItems, DialogInterface.OnClickListener { dialogInterface, i ->
                        Button_SelectLength.text = lengthItems[i]
                    })
                }

                Button_SelectLength.setOnClickListener {
                    dialogLenth.show()
                }
            })
        }
        return dilog
    }
}
