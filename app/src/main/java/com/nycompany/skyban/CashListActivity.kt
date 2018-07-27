package com.nycompany.skyban

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.nycompany.skyban.dto.CashDTO
import kotlinx.android.synthetic.main.activity_cash_list.*
import java.util.ArrayList

class CashListActivity : AppCompatActivity() {

    private val cashs: ArrayList<CashDTO> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_list)

        imageView_Back.setOnClickListener {
            finish()
        }

        
    }
}
