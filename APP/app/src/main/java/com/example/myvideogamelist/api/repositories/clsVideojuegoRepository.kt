package com.example.myvideogamelist.api.repositories

import com.example.myvideogamelist.api.services.clsVideojuegoService
import com.example.myvideogamelist.models.clsVideojuego

class clsVideojuegoRepository {

    private val api = clsVideojuegoService()

    suspend fun getVideojuego(idVideojuego: Int): clsVideojuego = api.getVideojuego(idVideojuego)

}