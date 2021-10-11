package com.ultrawavergb.dashboard

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // teste da conexao
        val myRef = database.getReference("message")
        myRef.setValue("There's a maggot!")

        val btnPipoca = findViewById<Button>(R.id.btn_pipoca)
        val btnLasanha = findViewById<Button>(R.id.btn_lasanha)
        val btnBrigadeiro = findViewById<Button>(R.id.btn_brigadeiro)
        val btnVegetais = findViewById<Button>(R.id.btn_vegetais)
        val btnArroz = findViewById<Button>(R.id.btn_arroz)
        val btnCarne = findViewById<Button>(R.id.btn_carne)

        val btnStart = findViewById<Button>(R.id.btn_start)
        val btnStop = findViewById<Button>(R.id.btn_stop_cancel)

        val seekBar = findViewById<SeekBar>(R.id.skbar_potencia)

        database.getReference("power").get().addOnSuccessListener {
            val intValue = it.value.toString().toInt()
            seekBar.setProgress(intValue)
            updateSeekBarPotencia(intValue)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data.", it)
        }

        /* --- FUNCOES DOS WIDGETS --- */

        btnPipoca.setOnClickListener {
            template(70, 300)
        }

        btnLasanha.setOnClickListener {
            template(100, 600)
        }

        btnBrigadeiro.setOnClickListener {
            template(50, 240)
        }

        btnVegetais.setOnClickListener {
            template(50, 240)
        }

        btnArroz.setOnClickListener {
            template(90, 540)
        }

        btnCarne.setOnClickListener {
            template(100, 720)
        }

        btnStart.setOnClickListener {
            database.getReference("start").setValue(1)
            Log.d("STATE", "Start set to 1.")
        }

        btnStop.setOnClickListener {
            handleStop()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                updateSeekBarPotencia(value)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    private fun updateSeekBarPotencia(value: Int) {
        val txtViewPotencia = findViewById<TextView>(R.id.txtview_potencia_value)
        txtViewPotencia.setText("${value}%")
    }

    private fun template(newPower: Int, time: Int) {
        database.getReference("power").setValue(newPower)
        // ? update da seekbar ?
        database.getReference("time").setValue(time)
        database.getReference("start").setValue(1)
    }

    private fun handleStop() {
        val stopRef = database.getReference("stop")
        stopRef.get().addOnSuccessListener {
            val stop = it.value.toString().toInt()
            when (stop) {
                0 -> stopRef.setValue(1)
                1 -> stopRef.setValue(2)
                else -> print("Don't change.")
            }
        }.addOnFailureListener {
            Log.e("Firebase", "Error getting data.", it)
        }
    }
}