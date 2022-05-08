package com.example.myvideogamelist.api.services

import com.example.myvideogamelist.api.conexion.Conexion
import com.example.myvideogamelist.api.core.RetrofitHelper
import com.example.myvideogamelist.api.providers.clsVideojuegoProvider
import com.example.myvideogamelist.models.clsVideojuego

class clsVideojuegoService {

    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getVideojuego(idVideojuego: Int): clsVideojuego =
        retrofit.create(clsVideojuegoProvider::class.java)
            .getVideojuego(Conexion.GET_VIDEOJUEGO+idVideojuego)

}