package com.example.myvideogamelist.viewmodels

import androidx.lifecycle.ViewModel
import com.example.myvideogamelist.api.repositories.clsListaConInfoDeVideojuegoRepository
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class clsListaConInfoDeVideojuegoViewModel: ViewModel() {

    val listaConInfoDeVideojuegosModel = mutableListOf<clsListaConInfoDeVideojuego>()
    val repository = clsListaConInfoDeVideojuegoRepository()

    /**
     * Este metodo obtendra la lista de videojuegos con su info con una corrutina
     */
    suspend fun onCreate(idUsuario: String){
        withContext(Dispatchers.IO){
            val result = repository.getListaVideojuegosPorUsuario(idUsuario)
            if(!result.isNullOrEmpty()){
                listaConInfoDeVideojuegosModel.removeAll(listaConInfoDeVideojuegosModel)
                listaConInfoDeVideojuegosModel.addAll(result)
            }
        }
    }

}