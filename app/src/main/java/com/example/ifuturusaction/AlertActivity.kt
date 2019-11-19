package com.example.ifuturusaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ifuturusaction.databinding.ActivityAlertBinding
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject
import kotlinx.android.synthetic.main.activity_alert.*

class AlertActivity : AppCompatActivity() {

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAA_E9Qmo8:APA91bH6njB4JgKgl_qXYnnNlu0MIOl5tDIAT2YII4iAnwpDbH2_8rUGqJRZeOvR0a2fBBIOkLtrT0NdNWm9BYhL-43-v0zbkqabe0lcUz0HuZXCRPGmzRb8xVvuxZgUIWfbzeHWaX11"
    private val contentType = "application/json"

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAlertBinding>(this, R.layout.activity_alert)

        FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_topic")

        binding.submit.setOnClickListener {
            if (!TextUtils.isEmpty(binding.msg.text)) {
                val topic = "/topics/Enter_topic" //topic has to match what the receiver subscribed to

                val notification = JSONObject()
                val notifcationBody = JSONObject()

                try {
                    notifcationBody.put("title", "Firebase Notification")
                    notifcationBody.put("message", binding.msg.text)
                    notification.put("to", topic)
                    notification.put("data", notifcationBody)
                    Log.e("TAG", "try")
                } catch (e: JSONException) {
                    Log.e("TAG", "onCreate: " + e.message)
                }

                sendNotification(notification)
            }
        }
    }


    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")
                msg.setText("")
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(this@AlertActivity, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }
}