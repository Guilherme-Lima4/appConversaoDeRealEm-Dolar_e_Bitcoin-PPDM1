package com.example.app_05_2022_05_25

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.net.URL
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.RuntimeException

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.ticker
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val API_BTC = "https://www.mercadobitcoin.net/api/BTC/ticker/"

    var cotacaoBitcoin: Double = 0.0

    val API_USD = "https://economia.awesomeapi.com.br/json/last/USD-BRL"

    var cotacaoDolar: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCalcular = findViewById<Button>(R.id.btnCalcular)

        var txtValor = findViewById<EditText>(R.id.txtValor)

        buscarCotacaoBitcoin()
        buscarCotacaoDolar()
        btnCalcular.setOnClickListener {
            calcular()
        }
    }
    fun buscarCotacaoBitcoin(){

        GlobalScope.async (Dispatchers.Default) {

            val respostaBTC = URL(API_BTC).readText()
            cotacaoBitcoin = JSONObject(respostaBTC).getJSONObject("ticker").getDouble("last")
            val f = NumberFormat.getCurrencyInstance(Locale("pt",  "br"))
            val cotacaoBTCFormatada = f.format(cotacaoBitcoin)
            val txtBTC = findViewById<TextView>(R.id.txtBTC)
            txtBTC.setText("$cotacaoBTCFormatada")
            withContext(Main){

            }
        }
    }

    fun buscarCotacaoDolar(){

        GlobalScope.async (Dispatchers.Default) {

            val respostaUSD = URL(API_USD).readText()
            cotacaoDolar = JSONObject(respostaUSD).getJSONObject("USDBRL").getDouble("bid")
            val f = NumberFormat.getCurrencyInstance(Locale("pt",  "br"))
            val cotacaoUSDFormatada = f.format(cotacaoDolar)
            val txtUSD = findViewById<TextView>(R.id.txtUSD)
            txtUSD.setText("$cotacaoUSDFormatada")
            withContext(Main){
            }
        }
    }


    fun calcular(){

        val txtQtdBitcoins = findViewById<TextView>(R.id.txtQtdBitcoins)
        var txtValor = findViewById<EditText>(R.id.txtValor)
        val txtQtdDolares = findViewById<TextView>(R.id.txtQtdDolares)

        if(txtValor.text.isEmpty()){
            txtValor.error = "Preencha um valor!"
            return
        }
        val valorDigitado = txtValor.text.toString()
            .replace(",", ".").toDouble()
        val resultado1 = if(cotacaoBitcoin > 0)valorDigitado / cotacaoBitcoin
            else 0.0
        txtQtdBitcoins.text = "%.8f".format(resultado1)

        val resultado2 = if(cotacaoDolar > 0)valorDigitado / cotacaoDolar
        else 0.0
        txtQtdDolares.text = "%.8f".format(resultado2)
    }
}