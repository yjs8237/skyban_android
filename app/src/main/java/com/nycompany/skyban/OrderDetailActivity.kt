package com.nycompany.skyban

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.dto.InOrderdetailDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.network.*
import com.nycompany.skyban.util.ContextUtil
import com.nycompany.skybanminitp.FragmentsAvailable
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_inorder_detail.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailActivity : FragmentActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private val util = ContextUtil(this)
    private val paramObject = JSONObject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inorder_detail)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        imageView_Back.setOnClickListener { finish() }

        val cell_no = getUserinfo()?.cell_no!!

        var intent = intent
        var orderseq = intent.getStringExtra("orderseq")

        paramObject.put("order_seq", orderseq)

        when (MainActivity.instance()?.getCurrentFarnment()) {
        //일반 수주화면
            FragmentsAvailable.ORDER -> {
                LinearLayout_order.visibility = View.VISIBLE
                textView_DetailTitle.text = "작업 상세정보"
            }
        //내 수주화면
            FragmentsAvailable.ORDER_HISTORY -> {
                LinearLayout_order_history.visibility = View.VISIBLE
                textView_DetailTitle.text = "수주작업 상세정보"
                ConstraintLayout_Contract.visibility = View.VISIBLE
                paramObject.put("cell_no", cell_no)
                paramObject.put("search_type", "1")
            }
            FragmentsAvailable.OUTORDER_HISTORY -> {
                LinearLayout_outorder_history.visibility = View.VISIBLE
                textView_DetailTitle.text = "발주작업 상세정보"
                ConstraintLayout_Contract.visibility = View.VISIBLE
                paramObject.put("cell_no", cell_no)
                paramObject.put("search_type", "2")
            }
        }

        ButtonOrder.setOnClickListener {
            val OderParam = JSONObject()
            OderParam.put("order_seq", orderseq)
            OderParam.put("cell_no", cell_no)

            val reqString = OderParam.toString()
            val server = RetrofitCreater.getMyInstance()?.create(ReqObtaInorder::class.java)
            server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                    util.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                    response.body()?.let {
                        if (it.result == ResCode.Success.Code) {
                            updateUserInfo(getUserinfo()?.cell_no, getUserinfo()?.password)
                            val db = util.buildDialog("성공", "성공적으로 수주 되었습니다 ")
                            db.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    MainActivity.instance()?.moveOrderHistory()
                                    finish()
                                }
                            })
                            db.setCancelable(false)
                            db.show()
                        } else {
                            it.description?.let {
                                util.buildDialog(it).show()
                            }
                        }?:run{
                            Log.e(this::class.java.name, getString(R.string.response_body_eror))
                        }
                    }
                }
            })
        }

        Button_Complete.setOnClickListener {
            val OderParam = JSONObject()
            OderParam.put("cell_no", cell_no)
            OderParam.put("order_seq", orderseq)
            OderParam.put("work_proc", "WP04 ")

            val reqString = OderParam.toString()
            val server = RetrofitCreater.getMyInstance()?.create(ReqUpdateorder::class.java)
            server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                    util.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                    response.body()?.let {
                        if (it.result == ResCode.Success.Code) {
                            updateUserInfo(getUserinfo()?.cell_no, getUserinfo()?.password)
                            val bd = util.buildDialog("성공", "작업완료 처리되었습니다 ")
                            bd.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    MainActivity.instance()?.moveOrderHistory()
                                    finish()
                                }
                            })
                            bd.setCancelable(false)
                            bd.show()
                        } else {
                            it.description?.let {
                                util.buildDialog(it).show()
                            }
                        }?:run{
                            Log.e(this::class.java.name, getString(R.string.response_body_eror))
                        }
                    }
                }
            })
        }

        Button_reorder.setOnClickListener {
            val OderParam = JSONObject()
            OderParam.put("cell_no", cell_no)
            OderParam.put("order_seq", orderseq)

            val reqString = OderParam.toString()
            val server = RetrofitCreater.getMyInstance()?.create(ReqRegisteragain::class.java)
            server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                    util.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                    response.body()?.let {
                        if (it.result == ResCode.Success.Code) {
                            updateUserInfo(getUserinfo()?.cell_no, getUserinfo()?.password)
                            val bd = util.buildDialog("성공", "재발주 되었습니다 ")
                            bd.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    finish()
                                }
                            })
                            bd.setCancelable(false)
                            bd.show()
                        } else {
                            it.description?.let {
                                util.buildDialog(it).show()
                            }
                        }?:run{
                            Log.e(this::class.java.name, getString(R.string.response_body_eror))
                        }
                    }
                }
            })
        }

        Button_OrderCancel.setOnClickListener {
            val OderParam = JSONObject()
            OderParam.put("cell_no", cell_no)
            OderParam.put("order_seq", orderseq)

            val reqString = OderParam.toString()
            val server = RetrofitCreater.getMyInstance()?.create(ReqOrdercancle::class.java)
            server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                    util.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                    response.body()?.let {
                        if (it.result == ResCode.Success.Code) {
                            updateUserInfo(getUserinfo()?.cell_no, getUserinfo()?.password)
                            val bd = util.buildDialog("성공", "취소 되었습니다 ")
                            bd.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    finish()
                                }
                            })
                            bd.setCancelable(false)
                            bd.show()
                        } else {
                            it.description?.let {
                                util.buildDialog(it).show()
                            }
                        }?:run{
                            Log.e(this::class.java.name, getString(R.string.response_body_eror))
                        }
                    }
                }
            })
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val loading: AlertDialog = SpotsDialog.Builder().setContext(this).build()
        loading.show()
        /*
        // 맵 터치 이벤트 구현 //
        mMap!!.setOnMapClickListener { point ->
            googleMap.clear()
            val mOptions = MarkerOptions()
            // 마커 타이틀
            mOptions.title("마커 좌표")
            val latitude = point.latitude // 위도
            val longitude = point.longitude // 경도
            // 마커의 스니펫(간단한 텍스트) 설정
            mOptions.snippet(latitude.toString() + ", " + longitude.toString())
            // LatLng: 위도 경도 쌍을 나타냄
            mOptions.position(LatLng(latitude, longitude))
            // 마커(핀) 추가
            googleMap.addMarker(mOptions)
        }*/
        val reqString = paramObject.toString()

        when (MainActivity.instance()?.getCurrentFarnment()) {
            FragmentsAvailable.ORDER -> {
                val reqString = paramObject.toString()
                val server = RetrofitCreater.getMyInstance()?.create(ReqOrderdetail::class.java)
                server?.postRequest(reqString)?.enqueue(object: Callback<InOrderdetailDTO> {
                    override fun onFailure(call: Call<InOrderdetailDTO>, t: Throwable) {
                        loading.dismiss()
                        val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                        util.buildDialog("eror", msg).show()
                    }

                    override fun onResponse(call: Call<InOrderdetailDTO>, response: Response<InOrderdetailDTO>) {
                        response.body()?.let {
                            loading.dismiss()
                            if(it.result == ResCode.Success.Code) {
                                setResponseData(it)
                            }else{
                                it.description?.let { util.buildDialog(it).show()
                                }
                            }
                        }?:run{
                            Log.e(this::class.java.name, getString(R.string.response_body_eror))
                        }
                    }
                })
            }

            FragmentsAvailable.ORDER_HISTORY, FragmentsAvailable.OUTORDER_HISTORY -> {
                val server = RetrofitCreater.getMyInstance()?.create(ReqMyOrderDetail::class.java)
                server?.postRequest(reqString)?.enqueue(object: Callback<InOrderdetailDTO> {
                    override fun onFailure(call: Call<InOrderdetailDTO>, t: Throwable) {
                        loading.dismiss()
                        val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                        util.buildDialog("eror", msg).show()
                    }

                    override fun onResponse(call: Call<InOrderdetailDTO>, response: Response<InOrderdetailDTO>) {
                        response.body()?.let {
                            loading.dismiss()
                            if(it.result == ResCode.Success.Code) {
                                setResponseData(it)
                            }else{
                                it.description?.let { util.buildDialog(it).show()
                                }
                            }
                        }?:run{
                            Log.e(this::class.java.name, getString(R.string.response_body_eror))
                        }
                    }
                })
            }
        }

        Button_JobCancel.setOnClickListener {
            util.buildDialog("관리자에게 문의하세요").show()
        }
    }

    //fun <T> setResponseData(dto:T){
    fun setResponseData(dto:InOrderdetailDTO){
        dto?.let {
            val location = LatLng( it.work_latitude!!.toDouble(), it.work_longitude!!.toDouble())
            if(MainActivity.instance()?.getCurrentFarnment() == FragmentsAvailable.ORDER_HISTORY){
                textView_OrderUserNum.text = "발주자연락처 : ${it?.let { it.order_user_num }?:run { "" }}"
                textView_WorkContact.text = "현장연락처 : ${it?.let { it.work_contact }?:run { "" }}"
            }else {
                textView_OrderUserNum.text = "수주자연락처 : ${it?.let { it.obtain_user_num}?:run { "" }}"
                textView_WorkContact.visibility = View.GONE
            }

            mMap?.addMarker(MarkerOptions().position(location).title("work area"))
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
            //mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            var carTypeMap = util.getHashmapFromResoureces(R.array.car_type)
            var carLengthMap = util.getHashmapFromResoureces(R.array.car_length)
            textView_Car.text = "차량 : ${carTypeMap[it.min_car_type]}" +
                    "${carLengthMap[it.min_car_length]?.let{ " - " + it}?:run{ "" }} ~ " +
                    "${carTypeMap[it.max_car_type]}" +
                    "${carLengthMap[it.min_car_length]?.let{ " - " + it}?:run{ "" }}"
            textView_WorkLocation.text = "장소 : ${it.work_location}"
            textView_WorkDate.text = "작업일시 : ${it.work_date}"
            var durationypeMap = util.getHashmapFromResoureces(R.array.work_duration)
            textView_WorkDuration.text = "작업시간 : ${durationypeMap[it.work_duration]}"

            var op_invertor =  if (it.op_danchuk == "Y") "인버터" else null
            var op_guljul =  if (it.op_guljul == "Y") "굴절" else null
            var op_winchi =  if (it.op_winchi == "Y") "윈찌" else null
            var op_danchuk =  if (it.op_danchuk == "Y") "단축" else null
            textView_Option.text = "옵션 : ${op_invertor?.let{it +" "}?:run{""}} ${op_guljul?.let{it +" "}?:run{""}} " +
                    "${op_winchi?.let{it +" "}?:run{""}} ${op_danchuk?.let{it +" "}?:run{""}}"

            var work_det_1=  if (it.work_det_1== "Y") "기타작업" else null
            var work_det_2=  if (it.work_det_2== "Y") "뿜칠(후끼)" else null
            var work_det_3=  if (it.work_det_3== "Y") "양중작업" else null
            var work_det_4=  if (it.work_det_4== "Y") "철거작업" else null
            var work_det_5=  if (it.work_det_5== "Y") "이삿짐" else null
            var work_det_6=  if (it.work_det_6== "Y") "거리작업" else null
            var work_det_7=  if (it.work_det_7== "Y") "전지작업" else null

            textView_Detail.text = "작업상세 : ${work_det_1?.let{it +" "}?:run{""}} ${work_det_2?.let{it +" "}?:run{""}} " +
                    "${work_det_3?.let{it +" "}?:run{""}} ${work_det_4?.let{it +" "}?:run{""}}" +
                    "${work_det_5?.let{it +" "}?:run{""}} ${work_det_6?.let{it +" "}?:run{""}}" +
                    "${work_det_7?.let{it +" "}?:run{""}}"

            var paydateMap = util.getHashmapFromResoureces(R.array.pay_date)
            textView_PayDate.text ="결제기간 : ${paydateMap[it.pay_date]}"

            textView_WorkContent.text =it.work_content
        }
    }
}
