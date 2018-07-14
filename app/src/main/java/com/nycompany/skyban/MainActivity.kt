package com.nycompany.skyban

import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.View.OnClickListener
import com.nycompany.skybanminitp.FragmentsAvailable
import kotlinx.android.synthetic.main.activity_main.*


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
            changeCurrentFragment(currentFragment, getIntent().extras)
        }

        inorder.setOnClickListener(this)
        outorder.setOnClickListener(this)
        info.setOnClickListener(this)
        charge.setOnClickListener(this)
        setting.setOnClickListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putSerializable("currentFragment", currentFragment)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun changeCurrentFragment(newFragmentType: FragmentsAvailable?, extras: Bundle?) {
        fragment = null
        when(newFragmentType){
            FragmentsAvailable.INORDER -> fragment = InorderFragment()
            FragmentsAvailable.OUTORDER -> fragment = OutorderFragment()
            FragmentsAvailable.INFO -> fragment = InfoFragment()
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

    override fun onClick(v: View) {
        resetSelection()
        when(v.id) {
            R.id.inorder -> {
                changeCurrentFragment(FragmentsAvailable.INORDER, null)
                inorder_select.visibility = View.VISIBLE
            }
            R.id.outorder -> {
                changeCurrentFragment(FragmentsAvailable.OUTORDER, null)
                outorder_select.visibility = View.VISIBLE
            }
            R.id.info-> {
                changeCurrentFragment(FragmentsAvailable.INFO, null)
                info_select.visibility = View.VISIBLE
            }
        }
    }

    private fun resetSelection() {
        inorder_select!!.visibility = View.INVISIBLE
        outorder_select!!.visibility = View.INVISIBLE
        info_select!!.visibility = View.INVISIBLE
    }
}
