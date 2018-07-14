package com.nycompany.skyban

class MyUtil {
    companion object {
        @JvmStatic fun getKey(m:HashMap<String, String>, value: Any): Any? {
            for (o in m.keys) {
                if (m[o]!!.equals(value)) {
                    return o
                }
            }
            return null
        }
    }
}