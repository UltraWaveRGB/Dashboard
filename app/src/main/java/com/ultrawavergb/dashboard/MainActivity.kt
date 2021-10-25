package com.ultrawavergb.dashboard

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val database = Firebase.database
    private var doorIsOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            seekBar.progress = intValue
            updateSeekBarPotencia(intValue)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data.", it)
        }

        addDoorIsOpenListener()
        addTimerListener()

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
            if (doorIsOpen) {
                Toast.makeText(this, "Porta Aberta", Toast.LENGTH_LONG).show()
            } else {
                val editTextTempoMinutes = findViewById<EditText>(R.id.txtview_tempo_value_minutes)
                val editTextTempoSeconds = findViewById<EditText>(R.id.txtview_tempo_value_seconds)
                val minutes = editTextTempoMinutes.text.toString().toInt()
                val seconds = editTextTempoSeconds.text.toString().toInt()
                if (seconds > 60) {
                    Toast.makeText(this, "Valor de tempo invalido", Toast.LENGTH_LONG).show()
                } else {
                    database.getReference("start_button_was_pressed").setValue(1)
                    database.getReference("timer").setValue(minutes * 60 + seconds)
                    editTextTempoMinutes.isEnabled = false
                    editTextTempoSeconds.isEnabled = false
                }

            }
        }

        btnStop.setOnClickListener {
            database.getReference("stop_button_was_pressed").setValue(1)
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
        txtViewPotencia.text = "${value}%"
        database.getReference("power").setValue(value)
    }

    private fun template(newPower: Int, timer: Int) {
        updateSeekBarPotencia(newPower)
        findViewById<SeekBar>(R.id.skbar_potencia).progress = newPower
        database.getReference("timer").setValue(timer)
        database.getReference("start_button_was_pressed").setValue(1)
    }


    private fun addDoorIsOpenListener() {
        val doorListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value.toString()
                if (value == "1") {
                    doorIsOpen = true
                } else if (value == "0") {
                    doorIsOpen = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "loadPost: Cancelled", error.toException())
            }
        }
        database.getReference("door_is_open").addValueEventListener(doorListener)
    }

    private fun addTimerListener() {
        val editTextTempoMinutes = findViewById<TextView>(R.id.txtview_tempo_value_minutes)
        val editTextTempoSeconds = findViewById<TextView>(R.id.txtview_tempo_value_seconds)
        val timerListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value.toString().toInt()
                if (value >= 60) {
                    val minutes = value / 60
                    val seconds = value % 60
                    editTextTempoMinutes.text = if (minutes >= 10) "$minutes" else "0$minutes"
                    editTextTempoSeconds.text = if (seconds >= 10) "$seconds" else "0$seconds"
                } else {
                    editTextTempoMinutes.text = "00"
                    editTextTempoSeconds.text = if (value >= 10) "$value" else "0$value"
                }

                if (value == 0) {
                    editTextTempoMinutes.isEnabled = true
                    editTextTempoSeconds.isEnabled = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "loadPost: Cancelled", error.toException())
            }
        }
        database.getReference("timer").addValueEventListener(timerListener)
    }
}