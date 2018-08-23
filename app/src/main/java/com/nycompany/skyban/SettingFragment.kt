package com.nycompany.skyban


import android.os.Bundle
import android.app.Fragment
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beardedhen.androidbootstrap.BootstrapButton
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.dto.RealmUserInfo
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqLeave
import com.nycompany.skyban.network.ReqLogout
import com.nycompany.skyban.network.ReqUpdateDistance
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import com.nycompany.skyban.util.MyUtil
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_setting.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SettingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    private  val alarmDistanceDic by lazy{ContextUtil(activity).getHashmapFromResoureces(R.array.alarm_distance)}
    private val util by lazy{ContextUtil(activity)}
    private val server by lazy{RetrofitCreater.getMyInstance()?.create(ReqUpdateDistance::class.java)}
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserinfo()?.let {
            it.isAlarmYN?.let {
                switch_alarmYN.isChecked = it
                setAlarmSubUI(it)
            }
            it.isAlarmSound?.let {
                switch_alarmWay.isChecked = it
            }
            it.AlarmDistance?.let {
                TextView_Distance.text = alarmDistanceDic[it]
            }
        }

        switch_alarmYN.setOnClickListener {
            val paramObject = JSONObject()
            if(switch_alarmYN.isChecked){
                paramObject.put("cell_no", getUserinfo()?.cell_no)
                paramObject.put("distance", "10000000")
            }else{
                paramObject.put("cell_no", getUserinfo()?.cell_no)
                paramObject.put("distance", "0")
            }
            val reqString = paramObject.toString()

            server?.postRequest(reqString)?.enqueue(object : Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    val msg = if (!util.isConnected()) getString(R.string.network_eror) else t.toString()
                    util.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                    response.body()?.let {
                        if (it.result == ResCode.Success.Code) {
                            Realm.getDefaultInstance().use {
                                val edit = it.where(RealmUserInfo::class.java).findAll()[0]
                                it.beginTransaction()
                                edit?.isAlarmYN = switch_alarmYN.isChecked
                                edit?.AlarmDistance = if(switch_alarmYN.isChecked) "10000000" else "0"
                                it.commitTransaction()
                            }
                            setAlarmSubUI(switch_alarmYN.isChecked)
                        } else {
                            it.description?.let {
                                Log.e(this::class.java.name, it)
                                switch_alarmYN.toggle()
                            }
                        }
                    } ?: run {
                        Log.e(this::class.java.name, getString(R.string.response_body_eror))
                        switch_alarmYN.toggle()
                    }
                }
            })
        }

        switch_alarmWay.setOnCheckedChangeListener { compoundButton, isChecked ->
            Realm.getDefaultInstance().use {
                val edit = it.where(RealmUserInfo::class.java).findAll()[0]
                it.beginTransaction()
                edit?.isAlarmSound = isChecked
                it.commitTransaction()
            }
        }

        val alarmDistanceValue =  ContextUtil(activity).getValueArryFromResoureces(R.array.alarm_distance)
        val dialogDistance = android.support.v7.app.AlertDialog.Builder(activity).apply {
            setTitle("알람받기 반경")
            setItems(alarmDistanceValue, DialogInterface.OnClickListener { dialogInterface, i ->
                alarmDistanceValue[i]?.let {alarmDistanceValue ->
                    val dist = MyUtil.getDicKeyFromValue(alarmDistanceDic,alarmDistanceValue) as String
                    TextView_Distance.text = alarmDistanceValue

                    val paramObject = JSONObject()
                    paramObject.put("cell_no", getUserinfo()?.cell_no)
                    paramObject.put("distance", dist)
                    val reqString = paramObject.toString()

                    server?.postRequest(reqString)?.enqueue(object : Callback<CommonDTO> {
                        override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                            val msg = if (!util.isConnected()) getString(R.string.network_eror) else t.toString()
                            util.buildDialog("eror", msg).show()
                        }

                        override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                            response.body()?.let {
                                if (it.result == ResCode.Success.Code) {
                                    Realm.getDefaultInstance().use {
                                        val edit = it.where(RealmUserInfo::class.java).findAll()[0]
                                        it.beginTransaction()
                                        edit?.AlarmDistance = dist
                                        it.commitTransaction()
                                    }
                                } else {
                                    it.description?.let {
                                        Log.e(this::class.java.name, it)
                                    }
                                }
                            } ?: run {
                                Log.e(this::class.java.name, getString(R.string.response_body_eror))
                            }
                        }
                    })

                    Realm.getDefaultInstance().use {
                        val edit = it.where(RealmUserInfo::class.java).findAll()[0]
                        it.beginTransaction()
                        edit?.AlarmDistance = dist
                        it.commitTransaction()
                    }
                }
            })
        }
        ConstraintLayout_Distance.setOnClickListener {
            dialogDistance.show()
        }

        ConstraintLayout_Logout.setOnClickListener {
            ContextUtil(activity).buildDialog("로그아웃 하시겠습니까?")?.apply {
                setPositiveButton("확인", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val paramObject = JSONObject()
                        paramObject.put("cell_no", getUserinfo()?.cell_no)
                        paramObject.put("user_pwd", getUserinfo()?.password)
                        val reqString = paramObject.toString()

                        val server = RetrofitCreater.getMyInstance()?.create(ReqLogout::class.java)
                        server?.postRequest(reqString)?.enqueue(object : Callback<CommonDTO> {
                            override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                                val msg = if (!util.isConnected()) getString(R.string.network_eror) else t.toString()
                                util.buildDialog("eror", msg).show()
                            }

                            override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                                response.body()?.let {
                                    if (it.result == ResCode.Success.Code) {
                                        MainActivity.instance()?.logout()
                                    } else {
                                        it.description?.let {
                                            Log.e(this::class.java.name, it)
                                        }
                                    }
                                } ?: run {
                                    Log.e(this::class.java.name, getString(R.string.response_body_eror))
                                }
                            }
                        })
                    }
                })
                setNegativeButton("취소", object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        return
                    }
                })
                //setCancelable(false)
                show()
            }
        }

        ConstraintLayout_Notification.setOnClickListener {
            startActivity(Intent().setClass(activity, NotificationActivity::class.java))
        }

        ConstraintLayout_qna.setOnClickListener {
            startActivity(Intent().setClass(activity, QnaActivity::class.java))
        }

        ConstraintLayout_register_qna.setOnClickListener {
            startActivity(Intent().setClass(activity, QnaRegisterActivity::class.java))
        }

        ConstraintLayout_Leave.setOnClickListener {
            ContextUtil(activity).buildDialog("정말 회원탈퇴 하시겠습니까?")?.apply {
                setPositiveButton("확인", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val paramObject = JSONObject()
                        paramObject.put("cell_no", getUserinfo()?.cell_no)
                        paramObject.put("user_pwd", getUserinfo()?.password)
                        val reqString = paramObject.toString()

                        val server = RetrofitCreater.getMyInstance()?.create(ReqLeave::class.java)
                        server?.postRequest(reqString)?.enqueue(object : Callback<CommonDTO> {
                            override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                                val msg = if (!util.isConnected()) getString(R.string.network_eror) else t.toString()
                                util.buildDialog("eror", msg).show()
                            }

                            override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                                response.body()?.let {
                                    if (it.result == ResCode.Success.Code) {
                                        MainActivity.instance()?.logout()
                                    } else {
                                        it.description?.let {
                                            Log.e(this::class.java.name, it)
                                        }
                                    }
                                } ?: run {
                                    Log.e(this::class.java.name, getString(R.string.response_body_eror))
                                }
                            }
                        })
                    }
                })
                setNegativeButton("취소", object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        return
                    }
                })
                //setCancelable(false)
                show()
            }
        }
    }

    private fun setAlarmSubUI(isChecked:Boolean){
        if(isChecked){
            LinearLayout_AlarmSub.visibility = View.VISIBLE
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ConstraintLayout_alarmWay.visibility = View.GONE
            }
            TextView_Distance.text = alarmDistanceDic[getUserinfo()?.AlarmDistance]
        } else{
            LinearLayout_AlarmSub.visibility = View.GONE
        }
    }
}
