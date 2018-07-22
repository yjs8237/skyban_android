package com.nycompany.skyban.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitCreater private constructor(){
    companion object {
        private var instance: Retrofit? = null
        private const val URL_BASE = "https://skyban.co.kr"

        @JvmStatic @Synchronized fun getMyInstance():Retrofit? {
            instance ?:run { instance = Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build() }

            return instance
        }
    }
}