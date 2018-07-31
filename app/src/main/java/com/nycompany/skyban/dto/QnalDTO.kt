package com.nycompany.skyban.dto

import java.util.ArrayList

data class QnalDTO(
        var result: String? = null,
        var description: String? = null,
        var list:ArrayList<QnaList>? = null
)

data class QnaList(
        var qna_seq: String? = null,
        var title: String? = null,
        var reply_yn: String? = null,
        var reg_date: String? = null
)
