package com.example.myvideogamelist.api.repositories

import com.example.myvideogamelist.api.services.clsUsuarioService
import com.example.myvideogamelist.models.clsUsuario

class clsUsuarioRepository {

    private val api = clsUsuarioService()

    suspend fun insertarUsuario(oUsuario: clsUsuario): Int = api.insertarPersona(oUsuario)

    suspend fun getUsuario(idUsuario: String): clsUsuario = api.getUsuario(idUsuario)

}