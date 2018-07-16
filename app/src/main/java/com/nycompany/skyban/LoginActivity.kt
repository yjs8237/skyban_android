package com.nycompany.skyban

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nycompany.skyban.EnumClazz.*
import com.nycompany.skyban.DTO.LoginDTO

import io.realm.Realm
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var util:ContextUtil = ContextUtil(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(!util.isConnected()) util.buildDialog("eror",getString(R.string.network_eror)).show()

        val server = RetrofitCreater.getMyInstance()?.create(ReqLogin::class.java)
        loginBtn.setOnClickListener{
            val paramObject = JSONObject()
            editTextPhone.setText("01032228237")
            paramObject.put("cell_no", editTextPhone.text)
            val pass = editTextPassword.text.toString()
            paramObject.put("user_pwd", pass)
            val reqString = paramObject.toString()

            server?.postRequest(reqString)?.enqueue(object:Callback<LoginDTO>{
                override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                    val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                    util.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
                    response.body()?.let {
                        if(it.result == ResCode.Success.Code) {
                            Realm.getDefaultInstance().use {
                                val data = it.where(RealmUserInfo::class.java).findAll()
                                it.beginTransaction()
                                if (data.size > 0) it.deleteAll()
                                var userInfo = util.setUserinfo(response, pass)
                                it.copyToRealm(userInfo)
                                it.commitTransaction()
                            }
                            startActivity(Intent().setClass(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }else{
                            it.description?.let { util.buildDialog(it).show()
                                //util.buildDialog(resCodeMap[response?.body()?.result]).show()
                            }
                        }
                    }?:run{
                        Log.e(this::class.java.name, getString(R.string.response_body_eror))
                    }
                }
            })
        }

        regBtn.setOnClickListener{
            Realm.getDefaultInstance().use {
                it.beginTransaction()
                it.deleteAll()
                it.commitTransaction()
            }
            startActivity(Intent().setClass(this, termsActivity::class.java))
        }
    }
}

