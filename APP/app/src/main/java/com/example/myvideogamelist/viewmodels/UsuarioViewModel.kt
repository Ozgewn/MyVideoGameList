package com.example.myvideogamelist.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myvideogamelist.api.repositories.clsUsuarioRepository
import com.example.myvideogamelist.models.clsUsuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioViewModel: ViewModel() {

    val usuarioSeleccionado = MutableLiveData<clsUsuario>()
    val listaUsuariosCompleto = mutableListOf<clsUsuario>()
    private val repository = clsUsuarioRepository()

    /**
     * Este metodo obtendra todos los usuarios que contengan el nombre de usuario introducido por parametros
     */
    suspend fun cargarUsuariosPorNombre(nombreUsuario: String){
        withContext(Dispatchers.IO){
            val result = repository.getUsuariosPorNombre(nombreUsuario)
            if(!result.isNullOrEmpty()){
                listaUsuariosCompleto.removeAll(listaUsuariosCompleto)
                listaUsuariosCompleto.addAll(result)
            }
        }

    }

    suspend fun getUsuario(idUsuario: String): clsUsuario = clsUsuarioRepository().getUsuario(idUsuario)

}