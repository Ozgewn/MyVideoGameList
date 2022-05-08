package com.example.myvideogamelist.api.services

import com.example.myvideogamelist.api.conexion.Conexion
import com.example.myvideogamelist.api.core.RetrofitHelper
import com.example.myvideogamelist.api.providers.clsListaConInfoDeVideojuegoProvider
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.create

class clsListaConInfoDeVideojuegoService {

    private val retrofit = RetrofitHelper.getRetrofit()

    /*
    Esto se hace asi ahora ya que en la version retrofit 2.6.0+ retrofit hace la conversion de manera automatica, antiguamente seria devolviendo un Response<List<clsListaConInfoDeVideojuego>>,
    pero con esta nueva manera, no hace falta, ponemos que devuelva una List<clsListaConInfoDeVideojuego> y ya hace la conversion de manera automatica
     */

    suspend fun getListaVideojuegosPorUsuario(idUsuario: String): List<clsListaConInfoDeVideojuego> =
        retrofit.create(clsListaConInfoDeVideojuegoProvider::class.java)
            .getListaVideojuegosPorUsuario(Conexion.GET_POST_PUT_LISTA_VIDEOJUEGOS_POR_USUARIO + idUsuario)
}