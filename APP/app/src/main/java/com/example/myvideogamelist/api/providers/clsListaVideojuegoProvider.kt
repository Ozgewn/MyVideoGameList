package com.example.myvideogamelist.api.providers

import com.example.myvideogamelist.api.conexion.Conexion
import com.example.myvideogamelist.models.clsListaVideojuego
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface clsListaVideojuegoProvider {

    @Headers("Content-Type: application/json")
    @POST(Conexion.GET_POST_PUT_LISTA_VIDEOJUEGOS_POR_USUARIO)
    fun insertarVideojuegoEnLista(@Body clsListaVideojuego: clsListaVideojuego): Call<Int>

    @Headers("Content-Type: application/json")
    @PUT(Conexion.GET_POST_PUT_LISTA_VIDEOJUEGOS_POR_USUARIO)
    fun editarVideojuegoEnLista(@Body clsListaVideojuego: clsListaVideojuego): Call<Int>

}