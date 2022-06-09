package com.example.myvideogamelist.viewmodels

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myvideogamelist.api.repositories.clsEstadoRepository
import com.example.myvideogamelist.api.repositories.clsListaConInfoDeVideojuegoRepository
import com.example.myvideogamelist.api.repositories.clsListaVideojuegoRepository
import com.example.myvideogamelist.models.clsEstado
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.models.clsListaVideojuego
import com.example.myvideogamelist.utils.SnackbarHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import java.net.UnknownHostException

class VideojuegoViewModel: ViewModel() {

    val listaConInfoDeVideojuegosModel = mutableListOf<clsListaConInfoDeVideojuego>()
    val listaDeVideojuegosSoloEnLista = mutableListOf<clsListaConInfoDeVideojuego>()
    val listaEstados = mutableListOf<clsEstado>()
    private val repository = clsListaConInfoDeVideojuegoRepository()
    private val repositoryEstados = clsEstadoRepository()
    private val repositoryLista = clsListaVideojuegoRepository()
    val videojuegoSeleccionado = MutableLiveData<clsListaConInfoDeVideojuego>()

    /**
     * Este metodo obtendra la lista de videojuegos con su info con una corrutina
     */
    suspend fun cargarListaConInfoDeVideojuego(idUsuario: String){
        withContext(Dispatchers.IO){
            val result = repository.getListaVideojuegosPorUsuario(idUsuario)
            if(!result.isNullOrEmpty()){
                listaConInfoDeVideojuegosModel.removeAll(listaConInfoDeVideojuegosModel)
                result.forEach {
                    it.fechaDeComienzo = it.fechaDeComienzo?.substring(0, 10)
                    it.fechaDeFinalizacion = it.fechaDeFinalizacion?.substring(0, 10)
                }
                listaConInfoDeVideojuegosModel.addAll(result)
            }
        }
    }

    /**
     * Este metodo obtendra una lista de los videojuegos que ESTEN en la lista del usuario en cuestion
      */
    suspend fun cargarSoloVideojuegosEnListaDelUsuario(idUSuario: String){
        withContext(Dispatchers.IO){
            val result = repository.getSoloVideojuegosEnListaDeUsuario(idUSuario)
            if(!result.isNullOrEmpty()){
                listaDeVideojuegosSoloEnLista.removeAll(listaDeVideojuegosSoloEnLista)
                result.forEach {
                    it.fechaDeComienzo = it.fechaDeComienzo?.substring(0, 10)
                    it.fechaDeFinalizacion = it.fechaDeFinalizacion?.substring(0, 10)
                }
                listaDeVideojuegosSoloEnLista.addAll(result)
            }
        }
    }

    /**
     * Este metodo obtendra la lista de estados con una corrutina
     */
    suspend fun cargarListaEstados(){
        if(listaEstados.isNullOrEmpty()){
            withContext(Dispatchers.IO){
                val result = repositoryEstados.getListaEstados()
                if(!result.isNullOrEmpty()){
                    listaEstados.removeAll(listaEstados)
                    listaEstados.addAll(result)
                }
            }
        }
    }

    suspend fun insertarVideojuegoEnLista(oVideojuegoEnLista: clsListaVideojuego): Int = repositoryLista.insertarVideojuegoEnLista(oVideojuegoEnLista)

    suspend fun editarVideojuegoEnLista(oVideojuegoEnLista: clsListaVideojuego): Int = repositoryLista.editarVideojuegoEnLista(oVideojuegoEnLista)

    suspend fun eliminarVideojuegoEnLista(idUsuario: String, idVideojuego: Int): Int = repositoryLista.eliminarVideojuegoEnLista(idUsuario, idVideojuego)
}
