package com.example.myvideogamelist.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myvideogamelist.api.repositories.clsEstadoRepository
import com.example.myvideogamelist.api.repositories.clsListaConInfoDeVideojuegoRepository
import com.example.myvideogamelist.models.clsEstado
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideojuegoViewModel: ViewModel() {

    val listaConInfoDeVideojuegosModel = mutableListOf<clsListaConInfoDeVideojuego>()
    val listaEstados = mutableListOf<clsEstado>()
    private val repository = clsListaConInfoDeVideojuegoRepository()
    private val repositoryEstados = clsEstadoRepository()
    val videojuegoSeleccionado = MutableLiveData<clsListaConInfoDeVideojuego>()

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

    /**
     * Este metodo obtendra la lista de estados con una corrutina
     */
    suspend fun onCreateEstados(){
        withContext(Dispatchers.IO){
            val result = repositoryEstados.getListaEstados()
            if(!result.isNullOrEmpty()){
                listaEstados.removeAll(listaEstados)
                listaEstados.addAll(result)
            }
        }
    }

}