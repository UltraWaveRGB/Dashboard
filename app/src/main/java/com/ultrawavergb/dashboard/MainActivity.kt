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
    private val STATE_ON = 1
    private val STATE_OFF = 0
    private val STATE_PAUSED = 2

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
            handleState()
        }

        btnStop.setOnClickListener {
            handleState()
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
        database.getReference("power").setValue(value)
    }

    private fun template(newPower: Int, time: Int) {
        updateSeekBarPotencia(newPower)
        findViewById<SeekBar>(R.id.skbar_potencia).setProgress(newPower)
        // ? update da seekbar ?
        database.getReference("time").setValue(time)
        database.getReference("state").setValue(1)
    }

    private fun handleState() {
        val stateRef = database.getReference("state")
        stateRef.get().addOnSuccessListener {
            val stop = it.value.toString().toInt()
            when (stop) {
                STATE_OFF -> stateRef.setValue(STATE_ON)
                STATE_ON -> stateRef.setValue(STATE_PAUSED)
                STATE_PAUSED -> stateRef.setValue(STATE_OFF)
            }
        }.addOnFailureListener {
            Log.e("Firebase", "Error getting data.", it)
        }
    }

    private fun setTime() {
        TODO("Precisa implementar")
    }
}