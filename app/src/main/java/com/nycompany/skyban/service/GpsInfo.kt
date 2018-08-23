package com.nycompany.skyban.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.nycompany.skyban.MainActivity
import com.nycompany.skyban.enums.PermissionCode
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.nycompany.skyban.R
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.getUserinfo
import com.nycompany.skyban.network.ReqLocation
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GpsInfo(private val mContext: Context): GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    // 최소 GPS 정보 업데이트 거리 100미터
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES:Float = 100.0f
    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 2분
    private val MIN_TIME_INTRVAL = (1000 * 60 * 2).toLong()
    //10초
    private val MIN_TIME_FASTEST_INTRVAL = (1000 * 10).toLong()
    // 현재 GPS 사용유무
    private var isGPSEnabled = false
    // 네트워크 사용유무
    var isNetworkEnabled = false
    // GPS 상태값
    var isGetLocation = false
    private var lat: Double = 0.toDouble() // 위도
    private var lon: Double = 0.toDouble() // 경도
    private lateinit var locationManager:LocationManager
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocation: Location

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M  &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.instance() as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PermissionCode.ACCESS_FINE_LOCATION_CODE.Code)
        }
    }

    @SuppressLint("MissingPermission")
    fun setLocationTool(){
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return
        }

        try {
            locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // GPS 정보
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            // 현재 네트워크 상태 값
            //isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled) {
                showSettingsAlert()
                return
            }

            mGoogleApiClient = GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
            mGoogleApiClient.connect()
        } catch (e: Exception) {
            Log.e(this::class.java.name, e.toString())
        }
    }

    /**
     * GPS 정보를 가져오지 못했을때
     * 설정값으로 갈지 물어보는 alert 창
     */
    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)

        alertDialog.setTitle("GPS 사용유무셋팅")
        alertDialog.setMessage("GPS 사용설정이 되지 않았을 수 있습니다. \n 설정창으로 가시겠습니까?")

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        }
        alertDialog.setNegativeButton("취소") { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }

    /**
     * GPS 종료
     */
    fun removeLocationTool() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        //locationManager?.let { it.removeUpdates(this@GpsInfo) }
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(MIN_TIME_INTRVAL)
                .setFastestInterval(MIN_TIME_FASTEST_INTRVAL)
                .setSmallestDisplacement(MIN_DISTANCE_CHANGE_FOR_UPDATES)
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }

    override fun onLocationChanged(location: Location) {
        // TODO Auto-generated method stub
        Log.i(this::class.java.name, "on location update")
        isGetLocation = true
        mLocation = location
        location.run {
            lat = latitude
            lon = longitude
        }

        val OderParam = JSONObject()
        OderParam.put("cell_no", getUserinfo()?.cell_no)
        OderParam.put("latitude", location.latitude)
        OderParam.put("longitude", location.longitude)

        val util = ContextUtil(mContext)
        val reqString = OderParam.toString()
        val server = RetrofitCreater.getMyInstance()?.create(ReqLocation::class.java)
        server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
            override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                val msg = if(!util.isConnected()) mContext.getString(R.string.network_eror) else t.toString()
                Log.e(this::class.java.name, msg)
            }

            override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                response.body()?.let {
                    if (it.result == ResCode.Success.Code) {
                        Log.i(this::class.java.name, "Location update")
                    } else {
                        it.description?.let {
                            Log.e(this::class.java.name, it)
                        }
                    }?:run{
                        Log.e(this::class.java.name, mContext.getString(R.string.response_body_eror))
                    }
                }
            }
        })
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
    }
}
