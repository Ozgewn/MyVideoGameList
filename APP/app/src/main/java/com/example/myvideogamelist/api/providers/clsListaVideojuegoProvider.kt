package com.example.myvideogamelist.api.providers

import com.example.myvideogamelist.api.conexion.Conexion
import com.example.myvideogamelist.models.clsListaVideojuego
import retrofit2.Call
import retrofit2.http.*

interface clsListaVideojuegoProvider {

    @Headers("Content-Type: application/json")
    @POST(Conexion.GET_POST_PUT_LISTA_VIDEOJUEGOS_POR_USUARIO)
    suspend fun insertarVideojuegoEnLista(@Body clsListaVideojuego: clsListaVideojuego): Int

    @Headers("Content-Type: application/json")
    @PUT(Conexion.GET_POST_PUT_LISTA_VIDEOJUEGOS_POR_USUARIO)
    suspend fun editarVideojuegoEnLista(@Body clsListaVideojuego: clsListaVideojuego): Int

    @DELETE
    suspend fun eliminarVideojuegoEnLista(@Url url: String): Int

}