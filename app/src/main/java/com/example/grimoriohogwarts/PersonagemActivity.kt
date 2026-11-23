package com.example.grimoriohogwarts

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.grimoriohogwarts.network.HogwartsApi
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonagemActivity : AppCompatActivity() {

    private lateinit var etNomePersonagem: EditText
    private lateinit var btnBuscarPersonagem: Button
    private lateinit var progressBarPersonagem: ProgressBar
    private lateinit var tvResultadoPersonagem: TextView
    private lateinit var hogwartsApi: HogwartsApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personagem)

        val toolbarPersonagem = findViewById<MaterialToolbar>(R.id.toolbarPersonagem)
        toolbarPersonagem.setNavigationOnClickListener {
            finish()
        }

        etNomePersonagem = findViewById(R.id.etNomePersonagem)
        btnBuscarPersonagem = findViewById(R.id.btnBuscarPersonagem)
        progressBarPersonagem = findViewById(R.id.progressBarPersonagem)
        tvResultadoPersonagem = findViewById(R.id.tvResultadoPersonagem)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hogwarts-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        hogwartsApi = retrofit.create(HogwartsApi::class.java)

        btnBuscarPersonagem.setOnClickListener {
            buscarPersonagem()
        }
    }

    private fun buscarPersonagem() {
        val nome = etNomePersonagem.text.toString().trim()

        if (nome.isEmpty()) {
            etNomePersonagem.error = "Digite o nome do personagem"
            return
        }

        if (nome.length < 3) {
            etNomePersonagem.error = "Digite pelo menos 3 caracteres"
            return
        }

        lifecycleScope.launch {
            progressBarPersonagem.visibility = View.VISIBLE
            tvResultadoPersonagem.text = ""

            try {
                val response = withContext(Dispatchers.IO) {
                    hogwartsApi.searchCharacters(nome)
                }

                val personagem = response.data?.firstOrNull()
                if (personagem == null) {
                    tvResultadoPersonagem.text = "Nenhum personagem encontrado"
                } else {
                    val nomeCompleto = personagem.name?.takeIf { it.isNotBlank() } ?: "Não informado"
                    val especie = personagem.species?.takeIf { it.isNotBlank() } ?: "Não informado"
                    val casa = personagem.house?.takeIf { it.isNotBlank() } ?: "Não informado"

                    tvResultadoPersonagem.text =
                        "Nome: $nomeCompleto\nEspécie: $especie\nCasa: $casa"
                }
            } catch (e: Exception) {
                tvResultadoPersonagem.text =
                    "Erro ao buscar personagem. Verifique sua conexão e tente novamente."
            } finally {
                progressBarPersonagem.visibility = View.GONE
            }
        }
    }
}
