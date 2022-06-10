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
     * Cabecera: suspend fun cargarUsuarios()
     * Descripcion: Este metodo carga una lista completa de usuarios en una corrutina
     * Precondiciones: Se debe tener conexion a Internet
     * Postcondiciones: Se devolvera la lista completa de usuarios
     * Entrada: N/A
     * Salida: N/A
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

    /**
     * Cabecera: suspend fun getUsuario(idUsuario: String): clsUsuario
     * Descripcion: Este metodo obtiene la informacion completa de un usuario en concreto, no controlamos que se pida informacion de un usuario
     * porque eso nunca podra ocurrir en nuestra APP
     * Precondiciones: El usuario en cuestion debe existir, aunque esto ya lo controle la APP con su IU, se debe tener conexion a Internet
     * Postcondiciones: Se devolvera el usuario del que se desea obtener la informacion
     * Entrada:
     *      idUsuario: String -> el id del usuario del que se desea obtener la informacion
     * Salida:
     *      clsUsuario -> el usuario con toda la informacion
     */
    suspend fun getUsuario(idUsuario: String): clsUsuario = repository.getUsuario(idUsuario)

}