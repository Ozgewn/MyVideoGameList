package com.example.myvideogamelist.api.providers

import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface clsListaConInfoDeVideojuegoProvider {
    @GET
    suspend fun getListaVideojuegosPorUsuario(@Url idUsuario: String): Response<List<clsListaConInfoDeVideojuego>>
}