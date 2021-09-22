package com.ultrawavergb.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myRef = database.getReference("message")
        myRef.setValue("There's a maggot!")

        val btnLed = findViewById<Button>(R.id.led_button)

        updateBtnColor(getLedData())

        btnLed.setOnClickListener {
//            turnLed()
            val value = getLedData()

            val ledRef = database.getReference("led")

            if (value == 1) {
                ledRef.setValue(0)
                Log.d("STATE", "Led set para 0.")
            } else if (value == 0) {
                ledRef.setValue(1)
                Log.d("STATE", "Led set para 1.")
            }

            updateBtnColor(value)
        }
    }

    private fun getLedData(): Int {
        var ledValue: Int = 2
        val ledRef = database.getReference("led")
        ledRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Nao sei o que esta acontecendo aqui
                val value = snapshot.getValue<Int>()
                if (value != null) {
                    Log.d("STATE", "Value: $value")
                    if (value == 1) {
                        ledValue = 0
                    } else if (value == 0) {
                        ledValue = 1
                    }
                } else {
                    ledValue = 0
                    Log.d("STATE", "Value  is null.")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // TODO
            }
        })
        Log.d("STATE", "ledValue: $ledValue")
        return ledValue
    }

    private fun updateBtnColor(value: Int) {
        val btnLed = findViewById<Button>(R.id.led_button)

        if (value == 0) {
            btnLed.setBackgroundColor(Color.rgb(0, 0, 0))
            btnLed.setTextColor(Color.rgb(255, 255, 255))
        } else if (value == 1) {
            btnLed.setBackgroundColor(Color.rgb(255, 255, 255))
            btnLed.setTextColor(Color.rgb(0, 0, 0))
        }

    }

}