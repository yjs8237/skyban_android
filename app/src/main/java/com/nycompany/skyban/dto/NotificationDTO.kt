package com.nycompany.skyban.dto

import java.util.ArrayList

data class NotificationDTO(
        var result: String? = null,
        var description: String? = null,
        var list:ArrayList<NotificationList>? = null
)

data class NotificationList(
        var noti_seq: String? = null,
        var title: String? = null,
        var content: String? = null
)
