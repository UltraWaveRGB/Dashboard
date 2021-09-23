package com.ultrawavergb.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val database = Firebase.database
    private var ledValue: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myRef = database.getReference("message")
        myRef.setValue("There's a maggot!")

        val btnLed = findViewById<Button>(R.id.led_button)

        getLedData()
        updateLdr()

        btnLed.setOnClickListener {

            val ledRef = database.getReference("led")

            if (ledValue == 1) {
                ledRef.setValue(0)
                Log.d("STATE", "Led set to 0.")
            } else if (ledValue == 0) {
                ledRef.setValue(1)
                Log.d("STATE", "Led set to 1.")
            }

            getLedData()
        }
    }

    private fun getLedData() {
        val ledRef = database.getReference("led")
        ledRef.get().addOnSuccessListener {
            ledValue = it.value.toString().toInt()
            Log.d("CHANGE", "Set ledValue to ${ledValue}.")
            Log.d("STATE", "Value: ${it.value}")
            updateBtnColor()
        }.addOnFailureListener {
            Log.e("ERROR", "Error getting data", it)
        }
    }

    private fun updateBtnColor() {
        val btnLed = findViewById<Button>(R.id.led_button)

        if (ledValue == 0) {
            btnLed.setBackgroundColor(Color.rgb(0, 0, 0))
            btnLed.setTextColor(Color.rgb(255, 255, 255))
            Log.d("CHANGE", "Set button to dark.")
        } else if (ledValue == 1) {
            btnLed.setBackgroundColor(Color.rgb(255, 255, 255))
            btnLed.setTextColor(Color.rgb(0, 0, 0))
            Log.d("CHANGE", "Set button to light.")
        }

    }

    private fun updateLdr() {
        val refLdr = database.getReference("light")
        val textViewLdrValue = findViewById<TextView>(R.id.text_view_ldr_value)
        refLdr.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<Int>()
                Log.d("STATE", "LDR Value is: " + value)
                textViewLdrValue.text = value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ERRO", "Failed to read value.", error.toException())
            }
        })
    }

}