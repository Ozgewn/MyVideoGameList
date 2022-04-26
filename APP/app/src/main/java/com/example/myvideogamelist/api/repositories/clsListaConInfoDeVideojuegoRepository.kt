package com.example.myvideogamelist.api.repositories

import com.example.myvideogamelist.api.services.clsListaConInfoDeVideojuegoService
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego

class clsListaConInfoDeVideojuegoRepository {

    private val api = clsListaConInfoDeVideojuegoService()

    suspend fun getListaVideojuegosPorUsuario(idUsuario: String):List<clsListaConInfoDeVideojuego>{
        return api.getListaVideojuegosPorUsuario(idUsuario)
    }

}