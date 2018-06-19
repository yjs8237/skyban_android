package com.nycompany.skyban

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.nycompany.skybanminitp.FragmentsAvailable


class MainActivity : AppCompatActivity(), OnClickListener {
    private var currentFragment:FragmentsAvailable? = null
    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            changeCurrentFragment(FragmentsAvailable.INORDER, getIntent().extras)
        } else{
            currentFragment = savedInstanceState.getSerializable("currentFragment") as FragmentsAvailable
        }
    }

    private fun changeCurrentFragment(newFragmentType: FragmentsAvailable?, extras: Bundle?) {
        fragment = null
        when(newFragmentType){
            FragmentsAvailable.INORDER -> fragment = InorderFragment()
        }
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
