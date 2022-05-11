package com.example.myvideogamelist.api.providers

import com.example.myvideogamelist.api.conexion.Conexion
import com.example.myvideogamelist.models.clsUsuario
import retrofit2.Call
import retrofit2.http.*

interface clsUsuarioProvider {

    @Headers("Content-Type: application/json")
    @POST(Conexion.POST_USUARIO)
    suspend fun insertarUsuario(@Body clsUsuario: clsUsuario): Int

    @GET
    suspend fun getUsuario(@Url url: String): clsUsuario

    @GET
    suspend fun getUsuariosPorNombre(@Url url: String): List<clsUsuario>

}