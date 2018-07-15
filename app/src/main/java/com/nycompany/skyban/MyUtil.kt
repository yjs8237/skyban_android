package com.nycompany.skyban

import com.nycompany.skyban.DTO.LoginDTO

class MyUtil {
    companion object {
        @JvmStatic fun getDicKeyFromValue(m:HashMap<String, String>, value: Any): Any? {
            for (o in m.keys) {
                if (m[o]!!.equals(value)) {
                    return o
                }
            }
            return null
        }
    }
}
