package com.example.myvideogamelist.api.repositories

import com.example.myvideogamelist.api.services.clsListaVideojuegoService
import com.example.myvideogamelist.models.clsListaVideojuego

class clsListaVideojuegoRepository {

    private val api = clsListaVideojuegoService()

    suspend fun insertarVideojuegoEnLista(oVideojuegoEnLista: clsListaVideojuego): Int{
        return api.insertarVideojuego(oVideojuegoEnLista)
    }

    suspend fun editarVideojuegoEnLista(oVideojuegoEnLista: clsListaVideojuego): Int{
        return api.editarVideojuego(oVideojuegoEnLista)
    }

    suspend fun eliminarVideojuegoEnLista(idUsuario: String, idVideojuego: Int): Int{
        return api.eliminarVideojuego(idUsuario, idVideojuego)
    }

}