package com.example.myvideogamelist.api.repositories

import com.example.myvideogamelist.api.services.clsListaVideojuegoService
import com.example.myvideogamelist.models.clsListaVideojuego

class clsListaVideojuegoRepository {

    private val api = clsListaVideojuegoService()

    fun insertarVideojuegoEnLista(oVideojuegoEnLista: clsListaVideojuego): Int?{
        return api.insertarVideojuego(oVideojuegoEnLista)
    }

    fun editarVideojuegoEnLista(oVideojuegoEnLista: clsListaVideojuego): Int?{
        return api.editarVideojuego(oVideojuegoEnLista)
    }

}