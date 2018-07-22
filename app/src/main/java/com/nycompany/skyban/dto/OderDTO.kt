package com.nycompany.skyban.dto

import java.util.ArrayList

data class OderDTO(
        var result: String? = null,
        var description: String? = null,
        var list:ArrayList<List>? = null
)

data class List(
        var order_seq: String? = null,
        var order_user_num: String? = null,
        var work_date: String? = null,
        var word_duration: String? = null,
        var work_pay: String? = null,
        var ext_charge: String? = null,
        var work_location: String? = null,
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
        var work_proc: String? = null,
        var commission_yn: String? = null
)
