package com.example.myvideogamelist.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myvideogamelist.api.repositories.clsUsuarioRepository
import com.example.myvideogamelist.models.clsUsuario
import com.example.myvideogamelist.views.sharedData.SharedData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioViewModel: ViewModel() {

    val usuarioSeleccionado = MutableLiveData<clsUsuario>()
    val listaUsuariosCompleto = mutableListOf<clsUsuario>()
    private val repository = clsUsuarioRepository()

    /**
     * Este metodo obtendra todos los usuarios que contengan el nombre de usuario introducido por parametros
     */
    suspend fun cargarUsuarios(){
        withContext(Dispatchers.IO){
            val result = repository.getListadoCompletoUsuarios()
            if(!result.isNullOrEmpty()){
                listaUsuariosCompleto.removeAll(listaUsuariosCompleto)
                listaUsuariosCompleto.addAll(result)
                listaUsuariosCompleto.remove(listaUsuariosCompleto.find { it.id == SharedData.idUsuario }) //para que no aparezca el propio usuario en la lista comunidad
            }
        }

    }

    suspend fun getUsuario(idUsuario: String): clsUsuario = clsUsuarioRepository().getUsuario(idUsuario)

}