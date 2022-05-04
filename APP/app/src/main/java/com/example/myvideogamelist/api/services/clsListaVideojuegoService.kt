package com.example.myvideogamelist.api.services

import com.example.myvideogamelist.api.conexion.Conexion
import com.example.myvideogamelist.api.core.RetrofitHelper
import com.example.myvideogamelist.api.providers.clsListaVideojuegoProvider
import com.example.myvideogamelist.models.clsListaVideojuego
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class clsListaVideojuegoService {

    private val retrofit = RetrofitHelper.getRetrofit()

    /*
    Esto se hace asi ahora ya que en la version retrofit 2.6.0+ retrofit hace el enqueue de manera el solo
     */
    suspend fun insertarVideojuego(oVideojuegoEnLista: clsListaVideojuego): Int = retrofit.create(clsListaVideojuegoProvider::class.java).insertarVideojuegoEnLista(oVideojuegoEnLista)

    suspend fun editarVideojuego(oVideojuegoEnLista: clsListaVideojuego): Int = retrofit.create(clsListaVideojuegoProvider::class.java).editarVideojuegoEnLista(oVideojuegoEnLista)

    suspend fun eliminarVideojuego(idUsuario: String, idVideojuego: Int): Int =
        retrofit.create(clsListaVideojuegoProvider::class.java).eliminarVideojuegoEnLista(
            Conexion.DELETE_LISTAVIDEOJUEGOS_USUARIO+idUsuario+"/"
            +Conexion.DELETE_LISTAVIDEOJUEGOS_VIDEOJUEGO+idVideojuego
        )

}