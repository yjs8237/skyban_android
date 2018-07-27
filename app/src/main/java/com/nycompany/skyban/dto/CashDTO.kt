package com.nycompany.skyban.dto

import java.util.ArrayList

data class CashDTO(
        var result: String? = null,
        var description: String? = null,
        var list:ArrayList<CashList>? = null
)

data class CashList(
        var cash_seq: String? = null,
        var cell_no: String? = null,
        var req_type: String? = null,
        var result_yn: String? = null,
        var cash: String? = null,
        var acc_name: String? = null,
        var reg_date: String? = null,
        var result_date: String? = null
)
