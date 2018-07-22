package com.nycompany.skyban

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_addr_search.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import com.nycompany.skyban.util.ContextUtil


class AddrSearchActivity : AppCompatActivity(), OnMapReadyCallback {
    private val geocoder by lazy{Geocoder(this)}
    private val util by lazy{ ContextUtil(this) }
    private var mMap:GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addr_search)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val intent = Intent()

        Button_Search.setOnClickListener {
            val str = if(EditText_Search.text.length > 0) EditText_Search.text.toString() else " "
            var addressList: List<Address>? = geocoder.getFromLocationName(str, 10)
            addressList?.let {
                if(it.size > 0) {
                var addr = it.get(0).getAddressLine(0)
                val latitude = it.get(0).latitude
                val longitude = it.get(0).longitude
                val point = LatLng(latitude, longitude)

                val mOptions = MarkerOptions().run {
                    title("search result")
                    snippet(addr)
                    position(point)
                }
                    addr = addr.replace("대한민국", "").trim()
                    EditText_Result.setText(addr)
                    mMap?.addMarker(mOptions)
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14f))

                    intent.putExtra("addr", addr)
                    intent.putExtra("latitude", latitude)
                    intent.putExtra("longitude", longitude)
            }else{
                util.buildDialog("주소없음", "올바른 주소값이 아닙니다 다른 읍면동 명으로 검색하세요").show()
                }
            }?:run {
                Log.e(this::class.java.name, "getFromLocationName method eror")
            }

            val view = this.currentFocus
            view?.run{
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(windowToken, 0)
            }
        }

        Button_Confirm.setOnClickListener {
            intent.extras?.let { setResult(RESULT_OK, intent)
            }?:run {setResult(RESULT_CANCELED, intent)  }
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        val seoul = LatLng(37.566535, 126.97796919999999)
        mMap?.addMarker(MarkerOptions().position(seoul).title("서울"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 14f))

        /*
        mMap?.setOnMapClickListener {
            val mOptions = MarkerOptions()
            // 마커 타이틀
            mOptions.title("마커 좌표")
            val latitude = it.latitude // 위도
            val longitude = it.longitude // 경도
            // 마커의 스니펫(간단한 텍스트) 설정
            mOptions.snippet(latitude!!.toString() + ", " + longitude!!.toString())
            // LatLng: 위도 경도 쌍을 나타냄
            mOptions.position(LatLng(latitude!!, longitude!!))
            // 마커(핀) 추가
            googleMap?.addMarker(mOptions)
        }*/
    }
}
