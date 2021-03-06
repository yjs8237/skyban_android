package com.nycompany.skyban.util

import org.json.JSONObject


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

        fun clearJson(JSONobj:JSONObject){
            while(JSONobj.length()>0)
                JSONobj.remove(JSONobj.keys().next());
        }
    }
}

/*
val server = RetrofitCreater.getMyInstance()?.create(ReqOrderdetail::class.java)
        val util = ContextUtil(this)
        val paramObject = JSONObject()
        paramObject.put("cell_no", orderseq)
        val reqString = paramObject.toString()

        server?.postRequest(reqString)?.enqueue(object: Callback<InOrderdetailDTO> {
            override fun onFailure(call: Call<InOrderdetailDTO>, t: Throwable) {
                val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                util.buildDialog("eror", msg).show()
            }

            override fun onResponse(call: Call<InOrderdetailDTO>, response: Response<InOrderdetailDTO>) {
                response.body()?.let {
                    if(it.result == ResCode.Success.Code) {

                    }else{
                        it.description?.let { util.buildDialog(it).show()
                            //util.buildDialog(resCodeMap[response?.body()?.result]).show()
                        }
                    }
                }?:run{
                    Log.e(this::class.java.name, getString(R.string.response_body_eror))
                }
            }
        })

 */
