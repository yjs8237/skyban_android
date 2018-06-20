package com.nycompany.skyban

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_terms.*

class termsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        backBtn.setOnClickListener{
            finish()
        }

        agrrBtn.setOnClickListener{
            startActivity(Intent().setClass(this, RegisterActivity::class.java))
            finish()
        }
    }
}
