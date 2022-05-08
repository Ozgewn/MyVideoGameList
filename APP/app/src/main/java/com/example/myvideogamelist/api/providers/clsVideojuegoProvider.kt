package com.example.myvideogamelist.api.providers

import com.example.myvideogamelist.models.clsVideojuego
import retrofit2.http.GET
import retrofit2.http.Url

interface clsVideojuegoProvider {
    @GET
    suspend fun getVideojuego(@Url url: String): clsVideojuego
}