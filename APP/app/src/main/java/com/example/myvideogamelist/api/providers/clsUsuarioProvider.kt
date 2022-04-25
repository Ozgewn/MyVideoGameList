package com.example.myvideogamelist.api.providers

import com.example.myvideogamelist.api.conexion.Conexion
import com.example.myvideogamelist.models.clsUsuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface clsUsuarioProvider {

    @Headers("Content-Type: application/json")
    @POST(Conexion.postUsuario)
    fun insertarUsuario(@Body clsUsuario: clsUsuario): Call<clsUsuario>
}