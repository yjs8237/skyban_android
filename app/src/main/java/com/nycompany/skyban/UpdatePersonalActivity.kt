package com.nycompany.skyban

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_update_personal.*
import org.json.JSONObject

class UpdatePersonalActivity : AppCompatActivity() {
    private val REQUEST_MAP = 101
    private  val paramObject by lazy{ JSONObject() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_personal)

        EditText_Addr.setOnClickListener {
            val intent = Intent(this, AddrSearchActivity::class.java)
            startActivityForResult(intent, REQUEST_MAP)
        }

        imageView_Back.setOnClickListener {
            finish()
        }

        Button_Change.setOnClickListener{
            paramObject.put("cell_no", getUserinfo()?.cell_no)
            paramObject.put("user_name", EditText_Name.text)


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(REQUEST_MAP == requestCode)
            if(resultCode == Activity.RESULT_OK){
                EditText_Addr.setText(data?.getStringExtra("addr"))
                //String.format("%.4f", data?.getDoubleExtra("latitude", 0.0))
                paramObject.put("work_latitude", data?.getDoubleExtra("latitude", 0.0).toString())
                paramObject.put("work_longitude", data?.getDoubleExtra("longitude", 0.0).toString())
            }
    }
}


