package com.nycompany.skyban

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqPersonalInfoUpdateServer
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import com.nycompany.skyban.util.MyUtil
import kotlinx.android.synthetic.main.activity_update_car.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateCarActivity : AppCompatActivity() {
    private  val paramObject by lazy{ JSONObject() }
    private  val util by lazy{ ContextUtil(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_car)

        imageView_Back.setOnClickListener {
            finish()
        }

        //전고
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

        Button_SelectType.setOnClickListener {
            makeCarTypeDialog().show()
        }

        Button_Change.setOnClickListener {
            paramObject.put("cell_no", getUserinfo()?.cell_no)
            var dic = util.getHashmapFromResoureces(R.array.car_type)
            //차량정보
            MyUtil.getDicKeyFromValue(dic, Button_SelectType.text.toString())?.let {
                paramObject.put("car_type", it)
            }
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

            val reqString = paramObject.toString()
            val server = RetrofitCreater.getMyInstance()?.create(ReqPersonalInfoUpdateServer::class.java)

            server?.postRequest(reqString)?.enqueue(object : Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    val msg = if (!util.isConnected()) getString(R.string.network_eror) else t.toString()
                    util.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                    response.body()?.let {
                        if (it.result == ResCode.Success.Code) {
                            updateUserInfo(getUserinfo()?.cell_no, getUserinfo()?.password)
                            val db = util.buildDialog("완료", "변경 되었습니다")
                            db.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    finish()
                                }
                            })
                            db.setCancelable(false)
                            db.show()
                        } else {
                            it.description?.let {
                                util.buildDialog(it).show()
                            }
                        }
                    } ?: run {
                        Log.e(this::class.java.name, getString(R.string.response_body_eror))
                    }
                }
            })
        }
    }

    fun makeCarTypeDialog(): AlertDialog.Builder{
        var typeItems = resources.getStringArray(R.array.car_type_text)

        var dilog = AlertDialog.Builder(this).apply {
            setTitle("차량톤수")
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

                var dialogLenth = AlertDialog.Builder(this@UpdateCarActivity).apply {
                    setTitle("차량길이");
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
