package com.nycompany.skyban


import android.os.Bundle
import android.app.Fragment
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nycompany.skyban.util.ContextUtil
import kotlinx.android.synthetic.main.fragment_setting.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SettingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarm_distance =  ContextUtil(activity).getArryFromResoureces(R.array.alarm_distance)
        val dialogDistance = android.support.v7.app.AlertDialog.Builder(activity).apply {
            setTitle("알람받기 반경")
            setItems(alarm_distance, DialogInterface.OnClickListener { dialogInterface, i ->
                Button_AlarmArea.text = alarm_distance[i]
            })
        }
        Button_AlarmArea.setOnClickListener {
            dialogDistance.show()
        }

        ConstraintLayout_Logout.setOnClickListener {
            ContextUtil(activity).buildDialog("로그아웃 하시겠습니까?")?.apply {
                setPositiveButton("OK", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        MainActivity.instance()?.logout()
                    }
                })
                setNegativeButton("cancel", object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        return
                    }
                })
                setCancelable(false)
                show()
            }
        }

        ConstraintLayout_Notification.setOnClickListener {
            startActivity(Intent().setClass(activity, NotificationActivity::class.java))
        }

        ConstraintLayout_qna.setOnClickListener {
            startActivity(Intent().setClass(activity, QnaActivity::class.java))
        }

        ConstraintLayout_register_qna.setOnClickListener {
            startActivity(Intent().setClass(activity, QnaRegisterActivity::class.java))
        }
    }
}
