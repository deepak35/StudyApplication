package com.example.studyapplication

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studyapplication.databinding.FragmentMainBinding
import com.example.studyapplication.model.getSubjectData
import com.example.studyapplication.service.RecyclerViewAdapter

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding

    private val smsManager = SmsManager.getDefault()

    private val smsStatusIntent = Intent(ACTION_SMS_STATUS)

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val result = resultCode
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        context?.registerReceiver(broadcastReceiver, IntentFilter(ACTION_SMS_STATUS))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainFragment.context)
            setHasFixedSize(true)
            adapter = RecyclerViewAdapter(getSubjectData())
        }
    }

    private fun initListener() {
//        binding.button.setOnClickListener {
//            checkLocationPermissions()
//        }
    }

    private fun checkLocationPermissions() {
        context?.let { it ->
            if (ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.SEND_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    activity?.requestPermissions(
                        arrayOf(
                            Manifest.permission.SEND_SMS
                        ),
                        APP_REQ_CODE
                    )
                }
                return
            }
            sendSMS("test msg")

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            APP_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS("permission granted")
                } else {
                    showToastMsg("no permission granted")
                }
            }
            else -> Unit
        }

    }




    private fun sendSMS(text: String) {
        smsManager.sendTextMessage(MOBILE_NUMBER, null, text, getPendingIntent(), null)
    }

    private fun getPendingIntent(): PendingIntent? {
        return PendingIntent.getBroadcast(context, APP_REQ_CODE, smsStatusIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun showToastMsg(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    companion object {
        const val APP_REQ_CODE = 1
        const val TAG = "STUDY_APP"
        const val ACTION_SMS_STATUS = "ACTION_SMS_STATUS"
    }
}