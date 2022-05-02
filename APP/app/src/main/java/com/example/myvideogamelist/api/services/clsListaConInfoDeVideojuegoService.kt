package com.example.myvideogamelist.api.services

import com.example.myvideogamelist.api.conexion.Conexion
import com.example.myvideogamelist.api.core.RetrofitHelper
import com.example.myvideogamelist.api.providers.clsListaConInfoDeVideojuegoProvider
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class clsListaConInfoDeVideojuegoService {

    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getListaVideojuegosPorUsuario(idUsuario: String):List<clsListaConInfoDeVideojuego>{
        return withContext(Dispatchers.IO){
            val response = retrofit.create(clsListaConInfoDeVideojuegoProvider::class.java).getListaVideojuegosPorUsuario(Conexion.GET_POST_PUT_LISTA_VIDEOJUEGOS_POR_USUARIO+idUsuario)
            response.body() ?: emptyList()
        } //esta corrutina devolvera una lista de videojuegos completa, si ocurre un fallo, y la respuesta a la llamada de la API es null, devolvera una lista vacia (aunque, gracias a la API, nunca nos
    // llegara nada a nulo, pero hago esta comprobacion por si acaso )
    }

}