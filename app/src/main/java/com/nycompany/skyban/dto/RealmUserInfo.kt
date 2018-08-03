package com.nycompany.skyban.dto

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class RealmUserInfo : RealmModel {
    /*
    // Implicitly @Required
    var name: String = ""
    // Implicitly not @Required
    var name: String? = null
    */
    @PrimaryKey
    var cell_no: String? = ""
    var password: String? = ""
    var user_type: String? = ""
    var user_name: String? = ""
    var user_level: String? = null
    var order_point: String? = null
    var obtain_point: String? = null
    var cash: String? = null
    var location: String? = null
    var cop_number: String? = null
    var email: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var car_type: String? = null
    var car_length: String? = null
    var car_height: String? = null
    var op_invertor: String? = null
    var op_guljul: String? = null
    var op_winchi: String? = null
    var op_danchuk: String? = null
    var recomm_cell_no: String? = null
    var order_cnt: String? = null
    var obtain_cnt: String? = null
    var reg_date: String? = null
    //default setting
    var isAlarmYN:Boolean? = true
    var isAlarmSound:Boolean? = true
    var AlarmDistance:String? = "10000000"
}