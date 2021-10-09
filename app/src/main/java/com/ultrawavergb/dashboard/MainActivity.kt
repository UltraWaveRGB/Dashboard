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
        val txtPotencia =
            findViewById<TextView>(R.id.txtview_potencia)  // ? separar em dois text view ?

        /* --- FUNCOES DOS WIDGETS --- */

        btnPipoca.setOnClickListener {
            // TODO
        }

        btnLasanha.setOnClickListener {
            // TODO
        }

        btnBrigadeiro.setOnClickListener {
            // TODO
        }

        btnVegetais.setOnClickListener {
            // TODO
        }

        btnArroz.setOnClickListener {
            // TODO
        }

        btnCarne.setOnClickListener {
            // TODO
        }

        btnStart.setOnClickListener {
            val startRef = database.getReference("start")
            startRef.setValue(1)
            Log.d("STATE", "Start set to 1.")
        }

        btnStop.setOnClickListener {
            // TODO
        }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                txtPotencia.setText("Potencia: $value")
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

        })


    }


//    // TODO: timer
//    private fun updateTime() {
//        val refLdr = database.getReference("time")
//        val textViewTimeValue = findViewById<TextView>(R.id.text_view_time_value)
//        refLdr.addValueEventListener(object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val value = snapshot.getValue<Int>()
//                Log.d("STATE", "Time Value is: " + value)
//                textViewTimeValue.text = value.toString()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("ERRO", "Failed to read value.", error.toException())
//            }
//        })
//    }

}