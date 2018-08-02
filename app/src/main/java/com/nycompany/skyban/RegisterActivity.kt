package com.nycompany.skyban

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imageView_Back.setOnClickListener{
            //startActivity(Intent().setClass(this, MainActivity::class.java))
            finish()
        }

        //차량정보
        Button_SelectType.setOnClickListener {
            makeCarTypelengthDialog().show()
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
