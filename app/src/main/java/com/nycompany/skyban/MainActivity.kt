package com.nycompany.skyban

import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
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
        var fm:FragmentManager =  fragmentManager
        var transaction :FragmentTransaction = fm.beginTransaction()

        if (newFragmentType != FragmentsAvailable.INORDER
                && newFragmentType != FragmentsAvailable.OUTORDER
                && newFragmentType != FragmentsAvailable.CHARGE
                && newFragmentType != FragmentsAvailable.INFO
                && newFragmentType != FragmentsAvailable.SETTING) {
            transaction.addToBackStack(newFragmentType.toString())
        } else {
            while (fm.backStackEntryCount > 0) {
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }

        transaction.replace(R.id.fragmentContainer, fragment, newFragmentType.toString())
        transaction.commitAllowingStateLoss()
        fm.executePendingTransactions()

        currentFragment = newFragmentType

    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
