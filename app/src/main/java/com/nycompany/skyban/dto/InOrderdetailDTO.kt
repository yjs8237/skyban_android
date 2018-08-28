package com.nycompany.skyban.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InOrderdetailDTO(
        var result: String? = null,
        var description: String? = null,
        var order_seq: String? = null,
        var work_date: String? = null,
        var work_duration: String? = null,
        var work_pay: String? = null,
        var ext_charge: String? = null,
        var work_location: String? = null,
        var work_location_detail: String? = null,
        var work_latitude: String? = null,
        var work_longitude: String? = null,
        var min_car_type: String? = null,
        var min_car_length: String? = null,
        var max_car_type: String? = null,
        var max_car_length: String? = null,
        var car_height: String? = null,
        var op_invertor: String? = null,
        var op_guljul: String? = null,
        var op_winchi: String? = null,
        var op_danchuk: String? = null,
        var pay_type: String? = null,
        var pay_date: String? = null,
        var work_content: String? = null,
        var work_transfer: String? = null,
        var work_proc: String? = null,
        var work_det_1: String? = null,
        var work_det_2: String? = null,
        var work_det_3: String? = null,
        var work_det_4: String? = null,
        var work_det_5: String? = null,
        var work_det_6: String? = null,
        var work_det_7: String? = null,
        var work_det_8: String? = null,
        var reg_date: String? = null,
        var work_contact: String? = null,
        var order_user_num: String? = null,
        var obtain_user_num: String? = null,
        var commission_yn: String? = null
):Parcelable
