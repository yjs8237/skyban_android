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

class UserInfoLocal {
    companion object {
        fun updateUserInfo(){
            val server = RetrofitCreater.getMyInstance()?.create(ReqLogin::class.java)
            val paramObject = JSONObject()
            lateinit var pass:String
            Realm.getDefaultInstance().use {
                val data = it.where(RealmUserInfo::class.java).findAll()
                paramObject.put("cell_no", data[0]?.cell_no)
                paramObject.put("user_pwd", data[0]?.password)
                pass = data[0]?.password.toString()
            }
            val reqString = paramObject.toString()

            server?.postRequest(reqString)?.enqueue(object: Callback<LoginDTO> {
                override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                    //val msg = if(!isConnected()) mContext?.getString(R.string.network_eror) else t.toString()
                    Log.e(this::class.java.name, t.toString())
                }

                override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
                    response.body()?.let {
                        if(it.result == ResCode.Success.Code) {
                            Realm.getDefaultInstance().use {
                                val data = it.where(RealmUserInfo::class.java).findAll()
                                it.beginTransaction()
                                if (data.size > 0) it.deleteAll()
                                var userInfo = makeRealmUserinfo(response, pass)
                                it.copyToRealm(userInfo)
                                it.commitTransaction()
                            }
                        }else{
                            Log.e(this::class.java.name, it.description)
                        }
                    }?:run{
                        Log.e(this::class.java.name, "response body eror")
                    }
                }
            })
        }

        fun makeRealmUserinfo(response:Response<LoginDTO>?, pass:String): RealmUserInfo {
            val userInfo: RealmUserInfo = RealmUserInfo().apply {
                cell_no = response?.body()?.user?.cell_no
                password = pass
                user_type = response?.body()?.user?.user_type
                user_name = response?.body()?.user?.user_name
                user_level = response?.body()?.user?.user_level
                order_point = response?.body()?.user?.order_point
                obtain_point = response?.body()?.user?.obtain_point
                cash = response?.body()?.user?.cash
                location = response?.body()?.user?.location
                cop_number = response?.body()?.user?.cop_number
                email = response?.body()?.user?.email
                latitude = response?.body()?.user?.latitude
                longitude = response?.body()?.user?.longitude
                car_type = response?.body()?.user?.car_type
                car_length = response?.body()?.user?.car_length
                car_height = response?.body()?.user?.car_height
                op_invertor = response?.body()?.user?.op_invertor
                longitude = response?.body()?.user?.longitude
                op_guljul = response?.body()?.user?.op_guljul
                op_winchi = response?.body()?.user?.op_winchi
                op_danchuk = response?.body()?.user?.op_danchuk
                recomm_cell_no = response?.body()?.user?.recomm_cell_no
                order_cnt = response?.body()?.user?.order_cnt
                obtain_cnt = response?.body()?.user?.obtain_cnt
                reg_date = response?.body()?.user?.reg_date
            }
            return userInfo
        }

        fun isHaveUserinfo():Boolean{
            Realm.getDefaultInstance().use {
                if(it.where(RealmUserInfo::class.java).findAll().size > 0){
                    return true
                }
                return false
            }
        }
    }
}