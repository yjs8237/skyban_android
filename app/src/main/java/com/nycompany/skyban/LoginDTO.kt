package com.nycompany.skyban

data class LoginDTO(
        var result: String? = null,
        var description: String? = null,
        var user:User? = null
)

data class User(
        var cell_no: String? = null,
        var user_type: String? = null,
        var user_name: String? = null,
        var user_level: String? = null,
        var order_point: Int? = null,
        var obtain_point: Int? = null,
        var cash: Int? = null,
        var location: String? = null,
        var cop_number: String? = null,
        var email: String? = null,
        var latitude: String? = null,
        var longitude: String? = null,
        var car_type: String? = null,
        var car_length: String? = null,
        var car_height: String? = null,
        var op_invertor: String? = null,
        var op_guljul: String? = null,
        var op_winchi: String? = null,
        var op_danchuk: String? = null,
        var recomm_cell_no: String? = null,
        var order_cnt: Int? = null,
        var obtain_cnt: Int? = null,
        var reg_date: String? = null
)