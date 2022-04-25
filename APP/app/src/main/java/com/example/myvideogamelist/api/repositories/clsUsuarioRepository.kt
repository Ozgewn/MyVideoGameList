package com.example.myvideogamelist.api.repositories

import com.example.myvideogamelist.api.services.clsUsuarioService
import com.example.myvideogamelist.models.clsUsuario

class clsUsuarioRepository {

    private val api = clsUsuarioService()

    fun insertarUsuario(oUsuario: clsUsuario): clsUsuario?{
        val response = api.insertarPersona(oUsuario)
        return response
    }

}