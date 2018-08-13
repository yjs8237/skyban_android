package com.nycompany.skyban

import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.nycompany.skyban.enums.PermissionCode
import com.nycompany.skyban.service.GpsInfo
import com.nycompany.skyban.util.ContextUtil
import com.nycompany.skybanminitp.FragmentsAvailable
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnClickListener {
    private var currentFragment:FragmentsAvailable? = null
    private var fragment: Fragment? = null
    lateinit var gps:GpsInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this, OnSuccessListener<InstanceIdResult> { instanceIdResult ->
            val newToken = instanceIdResult.token
        })

        setContentView(R.layout.activity_main)
        instance = this
        gps = GpsInfo(this)
        if(!gps.isGetLocation) gps.setLocationTool()

        if(savedInstanceState == null){
            changeCurrentFragment(FragmentsAvailable.ORDER, null)
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
        lateinit var fragment:Fragment
        currentFragment = newFragmentType

        when(newFragmentType){
            FragmentsAvailable.ORDER -> fragment = OrderFragment()
            FragmentsAvailable.OUTORDER -> fragment = OutorderFragment()
            FragmentsAvailable.INFO -> fragment = InfoFragment()
            FragmentsAvailable.CHARGE -> fragment = ChargeFragment()
            FragmentsAvailable.SETTING -> fragment = SettingFragment()
            FragmentsAvailable.ORDER_HISTORY -> fragment = OrderHistoryFragment()
            FragmentsAvailable.OUTORDER_HISTORY -> fragment = OutorderHistoryFragment()
        }
        var fm:FragmentManager =  fragmentManager
        var transaction :FragmentTransaction = fm.beginTransaction()

        if (newFragmentType != FragmentsAvailable.ORDER
                && newFragmentType != FragmentsAvailable.OUTORDER
                && newFragmentType != FragmentsAvailable.CHARGE
                && newFragmentType != FragmentsAvailable.INFO
                && newFragmentType != FragmentsAvailable.SETTING) {
            transaction.addToBackStack(newFragmentType.toString())
        } else {
            while (fm.backStackEntryCount > 0) {
                //백스택 모두 삭제
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }

        transaction.replace(R.id.fragmentContainer, fragment, newFragmentType.toString())
        transaction.commitAllowingStateLoss()
        fm.executePendingTransactions()
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.inorder -> {
                if(currentFragment != FragmentsAvailable.ORDER) {
                    changeCurrentFragment(FragmentsAvailable.ORDER, null)
                    selectMenu(FragmentsAvailable.ORDER)
                }
            }
            R.id.outorder -> {
                if(currentFragment != FragmentsAvailable.OUTORDER) {
                    changeCurrentFragment(FragmentsAvailable.OUTORDER, null)
                    selectMenu(FragmentsAvailable.OUTORDER)
                }
            }
            R.id.info-> {
                if(currentFragment != FragmentsAvailable.INFO) {
                    changeCurrentFragment(FragmentsAvailable.INFO, null)
                    selectMenu(FragmentsAvailable.INFO)
                }
            }
            R.id.charge-> {
                if(currentFragment != FragmentsAvailable.CHARGE) {
                    changeCurrentFragment(FragmentsAvailable.CHARGE, null)
                    selectMenu(FragmentsAvailable.CHARGE)
                }
            }
            R.id.setting-> {
                if(currentFragment != FragmentsAvailable.SETTING) {
                    changeCurrentFragment(FragmentsAvailable.SETTING, null)
                    selectMenu(FragmentsAvailable.SETTING)
                }
            }
        }
    }

    override fun onBackPressed() {
        ContextUtil(this).buildDialog("종료 하시겠습니까?")?.apply {
            setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    finish()
                }
            })
            setNegativeButton("cancel", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    return
                }
            })
            setCancelable(false)
            show()
        }
    }

    private fun selectMenu(menuToSelect: FragmentsAvailable?) {
        resetSelection()
        when (menuToSelect) {
            FragmentsAvailable.ORDER -> inorder_select.visibility = View.VISIBLE
            FragmentsAvailable.OUTORDER -> outorder_select.visibility = View.VISIBLE
            FragmentsAvailable.INFO-> info_select.visibility = View.VISIBLE
            FragmentsAvailable.CHARGE-> charge_select.visibility = View.VISIBLE
            FragmentsAvailable.SETTING-> setting_select.visibility = View.VISIBLE
        }
    }

    fun moveOrderHistory(){
        changeCurrentFragment(FragmentsAvailable.INFO , null)
        changeCurrentFragment(FragmentsAvailable.ORDER_HISTORY, null)
        selectMenu(FragmentsAvailable.INFO)
    }

    fun moveOutorderHistory(){
        changeCurrentFragment(FragmentsAvailable.INFO , null)
        changeCurrentFragment(FragmentsAvailable.OUTORDER_HISTORY, null)
        selectMenu(FragmentsAvailable.INFO)
    }

    fun movePremium(){
        changeCurrentFragment(FragmentsAvailable.INFO , null)
        startActivity(Intent().setClass(this, PremiumActivity::class.java))
        selectMenu(FragmentsAvailable.INFO)
    }

    fun logout(){
        Realm.getDefaultInstance().use {
            it.beginTransaction()
            it.deleteAll()
            it.commitTransaction()
        }
        startActivity(Intent().setClass(this, LoginActivity::class.java))
        finish()
    }

    fun getCurrentFarnment():FragmentsAvailable?{
        return currentFragment
    }

    private fun resetSelection() {
        inorder_select!!.visibility = View.INVISIBLE
        outorder_select!!.visibility = View.INVISIBLE
        info_select!!.visibility = View.INVISIBLE
        charge_select!!.visibility = View.INVISIBLE
        setting_select!!.visibility = View.INVISIBLE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //퍼미션 거절/승인에따른 처리
        if(requestCode == PermissionCode.ACCESS_FINE_LOCATION_CODE.Code){
            gps.setLocationTool()
        }
    }

    companion object {
        private var instance: MainActivity? = null

        fun instance():MainActivity?{
            return instance
        }
    }
}
