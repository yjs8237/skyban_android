package com.nycompany.skyban

import android.util.Log
import com.nycompany.skyban.dto.LoginDTO
import com.nycompany.skyban.dto.RealmUserInfo
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.ReqLogin
import com.nycompany.skyban.network.RetrofitCreater
import io.realm.Realm
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun updateUserInfo(cell:String?, pass:String?){
    val server = RetrofitCreater.getMyInstance()?.create(ReqLogin::class.java)
    val paramObject = JSONObject()

    paramObject.put("cell_no", cell)
    paramObject.put("user_pwd", pass)

    val reqString = paramObject.toString()

    server?.postRequest(reqString)?.enqueue(object: Callback<LoginDTO> {
        override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
            //val msg = if(!isConnected()) mContext?.getString(R.string.network_eror) else t.toString()
            Log.e(this::class.java.name, t.toString())
        }

        override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
            response.body()?.let {
                if(it.result == ResCode.Success.Code) {
                    resetUserinfoRealm(it, pass!!)
                }else{
                    Log.e(this::class.java.name, it.description)
                }
            }?:run{
                Log.e(this::class.java.name, "response body eror")
            }
        }
    })
}

fun getUserinfo(): RealmUserInfo? {
    Realm.getDefaultInstance().use {
        val userData = it.where(RealmUserInfo::class.java).findAll()
        if(userData.size > 0){
            return it.copyFromRealm(userData)[0]
        }
    }
    return null
}

fun resetUserinfoRealm(dto:LoginDTO?, pass:String){
    val userInfo: RealmUserInfo = RealmUserInfo().apply {
        cell_no = dto?.user?.cell_no
        password = pass
        user_type = dto?.user?.user_type
        user_name = dto?.user?.user_name
        user_level = dto?.user?.user_level
        order_point = dto?.user?.order_point
        obtain_point = dto?.user?.obtain_point
        cash = dto?.user?.cash
        location = dto?.user?.location
        cop_number = dto?.user?.cop_number
        email = dto?.user?.email
        latitude = dto?.user?.latitude
        longitude = dto?.user?.longitude
        car_type = dto?.user?.car_type
        car_length = dto?.user?.car_length
        car_height = dto?.user?.car_height
        op_invertor = dto?.user?.op_invertor
        longitude = dto?.user?.longitude
        op_guljul = dto?.user?.op_guljul
        op_winchi = dto?.user?.op_winchi
        op_danchuk = dto?.user?.op_danchuk
        recomm_cell_no = dto?.user?.recomm_cell_no
        order_cnt = dto?.user?.order_cnt
        obtain_cnt = dto?.user?.obtain_cnt
        reg_date = dto?.user?.reg_date
    }

    Realm.getDefaultInstance().use {
        it.beginTransaction()
        it.deleteAll()
        it.copyToRealm(userInfo)
        it.commitTransaction()
    }
}

fun isHaveUserinfo():Boolean{
    Realm.getDefaultInstance().use {
        if(it.where(RealmUserInfo::class.java).findAll().size > 0){
            return true
        }
    }
    return false
}
