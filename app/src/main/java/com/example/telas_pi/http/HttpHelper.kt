package com.example.telas_pi.http

import com.example.telas_pi.model.Usuario
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient;
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class HttpHelper {
    fun post(json: String, tipoDeUsuario: String?): String {
        //definir url do servidor
        val baseUrl = "http://192.168.15.7:8080/usuario/cadastro/"
        val URL = "$baseUrl$tipoDeUsuario"

        // definir o cabeçalho
        val headerHttp = "application/json; charset=utf-8".toMediaTypeOrNull()

        // cria um cliente que vai disparar a requisição
        val client = OkHttpClient()

        //criar o body da requisição
        val body = RequestBody.create(headerHttp, json)

        //contruir a requisição http para o servidor
        var request = Request.Builder().url(URL).post(body).build()

        // utilizar o client para fazer a requisição e receber a resposata
        val response = client.newCall(request).execute()

        return response.body.toString()
    }

//    fun update(json: String, user: Usuario): String {
//
//        val baseUrl = "http://192.168.15.7:8080/usuario/${user.id}"
//        val headerHttp = "application/json; charset=utf-8".toMediaTypeOrNull()
//        val client = OkHttpClient()
//        val body = RequestBody.create(headerHttp, json)
//        val request = Request.Builder().url(baseUrl).put(body).build()
//        val response = client.newCall(request).execute()
//
//        return response.body.toString()
//
//    }


    suspend fun postLogin(json: String): Long? {
        val baseUrl = "http://192.168.15.7:8080/usuario/login"
        val headerHttp = "application/json; charset=utf-8".toMediaTypeOrNull()
        val client = OkHttpClient()
        val body = RequestBody.create(headerHttp, json)
        val request = Request.Builder().url(baseUrl).post(body).build()

        return try {
            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
            val responseBody = response.body?.string()

            android.util.Log.d("postLogin", "Response Code: ${response.code}")
            android.util.Log.d("postLogin", "Response Body: $responseBody")

            if (response.isSuccessful && responseBody != null) {
                val userId = responseBody.toLongOrNull() ?: -1L
                android.util.Log.d("postLogin", "Extracted userId: $userId")
                userId.takeIf { it != -1L }
            } else {
                null
            }
        } catch (e: IOException) {
            android.util.Log.e("postLogin", "Error during login request", e)
            null
        }
    }


    fun get(id: Long): Usuario? {
        val baseUrl = "http://192.168.15.7:8080/usuario/$id"
        val client = OkHttpClient()
        val request = Request.Builder().url(baseUrl).get().build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body


            if (responseBody != null) {
                val json = responseBody.string()

                val gson = Gson()
                return gson.fromJson(json, Usuario::class.java)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null

    }


    fun delete(id: Usuario) {


    }
}