package com.nycompany.skyban.util

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AlertDialog
import android.util.Log
import com.nycompany.skyban.dto.LoginDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.R
import com.nycompany.skyban.dto.RealmUserInfo
import com.nycompany.skyban.network.ReqLogin
import com.nycompany.skyban.network.RetrofitCreater
import io.realm.Realm
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContextUtil(context: Context) {
    var mContext:Context? = context

    fun isConnected(): Boolean {
        val cm = mContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if ( cm.activeNetworkInfo == null) { // connected to the internet
            return false
        }
        return true
    }

    fun buildDialog(title:String? ,msg: String?): AlertDialog.Builder {
        val builder = AlertDialog.Builder(mContext!!)
        builder.setTitle(title)
        builder.setMessage(msg)

        builder.setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }

        return builder
    }

    fun buildDialog(msg: String?): AlertDialog.Builder {
        val builder = AlertDialog.Builder(mContext!!)
        builder.setMessage(msg)

        builder.setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }

        return builder
    }

    //key|value string
    fun getValueArryFromResoureces(stringArrayResourceId:Int): Array<String?> {
        val stringArray = mContext!!.resources.getStringArray(stringArrayResourceId)
        val outputArray = arrayOfNulls<String>(stringArray.size)
        for (i in 0..stringArray.size - 1) {
            val splitResult = stringArray[i].split("\\|".toRegex(), 2).toTypedArray()
            outputArray[i] = splitResult[1]
        }
        return outputArray
    }

    //key|value string
    fun getHashmapFromResoureces(stringArrayResourceId:Int): HashMap<String, String> {
        val stringArray = mContext!!.resources.getStringArray(stringArrayResourceId)
        val outputArray = HashMap<String, String>(stringArray.size)
        for (entry in stringArray) {
            val splitResult = entry.split("\\|".toRegex(), 2).toTypedArray()
            outputArray.put(splitResult[0], splitResult[1])
        }
        return outputArray
    }
}

