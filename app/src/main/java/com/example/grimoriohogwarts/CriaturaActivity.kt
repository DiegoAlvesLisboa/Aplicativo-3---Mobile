package com.example.grimoriohogwarts

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.grimoriohogwarts.network.HogwartsApi
import com.google.android.material.appbar.MaterialToolbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CriaturaActivity : AppCompatActivity() {

    private lateinit var etNomeCriatura: EditText
    private lateinit var btnBuscarCriatura: Button
    private lateinit var progressBarCriatura: ProgressBar
    private lateinit var tvResultadoCriatura: TextView
    private lateinit var ivCriatura: ImageView
    private lateinit var hogwartsApi: HogwartsApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criatura)

        val toolbarCriatura = findViewById<MaterialToolbar>(R.id.toolbarCriatura)
        toolbarCriatura.setNavigationOnClickListener {
            finish()
        }

        etNomeCriatura = findViewById(R.id.etNomeCriatura)
        btnBuscarCriatura = findViewById(R.id.btnBuscarCriatura)
        progressBarCriatura = findViewById(R.id.progressBarCriatura)
        tvResultadoCriatura = findViewById(R.id.tvResultadoCriatura)
        ivCriatura = findViewById(R.id.ivCriatura)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hogwarts-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        hogwartsApi = retrofit.create(HogwartsApi::class.java)

        btnBuscarCriatura.setOnClickListener {
            buscarCriatura()
        }
    }

    private fun buscarCriatura() {
        val nome = etNomeCriatura.text.toString().trim()

        if (nome.isEmpty()) {
            etNomeCriatura.error = "Digite o nome da criatura"
            return
        }

        if (nome.length < 3) {
            etNomeCriatura.error = "Digite pelo menos 3 caracteres"
            return
        }

        lifecycleScope.launch {
            progressBarCriatura.visibility = View.VISIBLE
            tvResultadoCriatura.text = ""
            ivCriatura.setImageDrawable(null)

            try {
                val response = withContext(Dispatchers.IO) {
                    hogwartsApi.searchCreatures(nome)
                }

                val lista = response.data.orEmpty()
                val criatura = lista.firstOrNull { it.name.equals(nome, ignoreCase = true) }
                    ?: lista.firstOrNull()

                if (criatura == null) {
                    progressBarCriatura.visibility = View.GONE
                    tvResultadoCriatura.text = "Nenhuma criatura encontrada"
                } else {
                    val nomeCriatura = criatura.name?.takeIf { it.isNotBlank() } ?: nome
                    val imageUrl = criatura.image

                    if (imageUrl.isNullOrBlank()) {
                        progressBarCriatura.visibility = View.GONE
                        tvResultadoCriatura.text = "$nomeCriatura\nImagem indisponível"
                        ivCriatura.setImageResource(R.drawable.ic_criatura_placeholder)
                    } else {
                        tvResultadoCriatura.text = nomeCriatura
                        Picasso.get()
                            .load(imageUrl)
                            .into(ivCriatura, object : Callback {
                                override fun onSuccess() {
                                    progressBarCriatura.visibility = View.GONE
                                }

                                override fun onError(e: Exception?) {
                                    progressBarCriatura.visibility = View.GONE
                                    tvResultadoCriatura.text = "$nomeCriatura\nImagem indisponível"
                                    ivCriatura.setImageResource(R.drawable.ic_criatura_placeholder)
                                }
                            })
                    }
                }
            } catch (e: Exception) {
                progressBarCriatura.visibility = View.GONE
                tvResultadoCriatura.text =
                    "Erro ao buscar criatura. Verifique sua conexão e tente novamente."
            }
        }
    }
}
