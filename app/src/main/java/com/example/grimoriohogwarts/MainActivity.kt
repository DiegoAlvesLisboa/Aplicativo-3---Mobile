package com.example.grimoriohogwarts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnBuscarPersonagem = findViewById<Button>(R.id.btnBuscarPersonagem)
        val btnBuscarCriatura = findViewById<Button>(R.id.btnBuscarCriatura)

        btnBuscarPersonagem.setOnClickListener {
            startActivity(Intent(this, PersonagemActivity::class.java))
        }

        btnBuscarCriatura.setOnClickListener {
            startActivity(Intent(this, CriaturaActivity::class.java))
        }
    }
}
