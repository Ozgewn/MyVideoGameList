package com.example.myvideogamelist.api.repositories

import com.example.myvideogamelist.api.services.clsUsuarioService
import com.example.myvideogamelist.models.clsUsuario

class clsUsuarioRepository {

    private val api = clsUsuarioService()

    suspend fun insertarUsuario(oUsuario: clsUsuario): Int = api.insertarUsuario(oUsuario)

    suspend fun editarUsuario(oUsuario: clsUsuario):Int = api.editarUsuario(oUsuario)

    suspend fun comprobarExistenciaUsuario(idUsuario: String):Boolean = api.comprobarExistenciaUsuario(idUsuario)

    suspend fun borrarUsuario(idUsuario: String): Int = api.borrarUsuario(idUsuario)

    suspend fun getUsuario(idUsuario: String): clsUsuario = api.getUsuario(idUsuario)

    suspend fun getListadoCompletoUsuarios(): List<clsUsuario> = api.getListadoCompletoUsuarios()

}