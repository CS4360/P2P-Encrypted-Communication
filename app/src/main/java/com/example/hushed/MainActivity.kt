package com.example.hushed

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class MainActivity : AppCompatActivity() {
    var dummyAddress = "Brian"
    val dummyData = hashMapOf(
        "Nicolas" to hashMapOf("Francisco" to "Wassup", "Wes" to "Howdy"),
        "Wes" to hashMapOf("Francisco" to "Yoo", "Nicolas" to "What it do???"),
        dummyAddress to hashMapOf("Francisco" to "Wasssuuup", "Nicolas" to "Helloooo")
    )
    val db = FirebaseFirestore.getInstance()
        .collection("db")
        .document("collection")
    val map = mutableMapOf<String, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {
            } else { ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 2) } }

        // CONNECT BUTTON **************************************************************************
        button_connect.setOnClickListener {
            Log.i("Button","Click: button_connect")
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        }

        // SETTINGS BUTTON **************************************************************************
        button_settings.setOnClickListener {
            Log.i("Button","Click: button_settings")
        }

        // ABOUT BUTTON **************************************************************************
        button_about.setOnClickListener {
            try{
                val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val IMEI = tm.getImei()
                if (IMEI != null)
                    Toast.makeText(this, "IMEI number: " + IMEI,
                        Toast.LENGTH_LONG).show()

            }catch (ex:Exception){
                Log.i("", "There was a problem")
            }
        }

        // Dummy BUTTON **************************************************************************
        button_dummy.setOnClickListener {
            db.set(dummyData, SetOptions.merge())
                .addOnSuccessListener { Log.d("Firebase", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("Firebase", "Error writing document", e) }

            Log.i("Button","Click: button_dummy")
        }

        // Dummy BUTTON **************************************************************************
        button_dummy.setOnClickListener {
            val dummyData = hashMapOf(
                "Nicolas" to hashMapOf("Francisco" to "Wassup", "Wes" to "Howdy"),
                "Wes" to hashMapOf("Francisco" to "Yoo", "Nicolas" to "What it do???"),
                dummyAddress to hashMapOf("Francisco" to "Yoo", "Nicolas" to "What it do???")
            )

            db.set(dummyData, SetOptions.merge())
                .addOnSuccessListener { Log.d("Firebase", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("Firebase", "Error writing document", e) }

            Log.i("Button","Click: button_dummy")
        }

        // Retrieve BUTTON **************************************************************************
        button_retrieve.setOnClickListener {
            db.get()
                .addOnSuccessListener { doc ->
                    map.put(dummyAddress, doc[dummyAddress])

                    if(map[dummyAddress] != null) {
                        textView4.setText(map[dummyAddress].toString())
                    }
                }
                .addOnFailureListener { e -> Log.w("Firebase", "Error retrieving document", e) }

            Log.i("Button","Click: button_retrieve")
        }
    }
}
