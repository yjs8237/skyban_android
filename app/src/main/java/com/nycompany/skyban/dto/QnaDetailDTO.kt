package com.nycompany.skyban.dto

import java.util.ArrayList

data class QnaDetailDTO(
        var result: String? = null,
        var description: String? = null,
        var qna_seq: String? = null,
        var open_yn: String? = null,
        var title: String? = null,
        var content: String? = null,
        var reply: String? = null,
        var reg_date: String? = null,
        var reply_date: String? = null
)
