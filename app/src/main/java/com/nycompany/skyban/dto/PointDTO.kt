package com.nycompany.skyban.dto

import java.util.ArrayList

data class PointDTO(
        var result: String? = null,
        var description: String? = null,
        var list:ArrayList<PointList>? = null
)

data class PointList(
        var point_seq: String? = null,
        var cell_no: String? = null,
        var order_seq: String? = null,
        var order_user_num: String? = null,
        var obtain_user_num: String? = null,
        var work_location: String? = null,
        var work_latitude: String? = null,
        var work_longitude: String? = null,
        var work_date: String? = null,
        var work_pay: String? = null,
        var work_contact: String? = null,
        var point_type: String? = null,
        var point: String? = null
)
