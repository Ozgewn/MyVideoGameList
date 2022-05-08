package com.example.myvideogamelist.api.services

import com.example.myvideogamelist.api.conexion.Conexion
import com.example.myvideogamelist.api.core.RetrofitHelper
import com.example.myvideogamelist.api.providers.clsUsuarioProvider
import com.example.myvideogamelist.models.clsUsuario
import com.google.firebase.auth.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class clsUsuarioService {

    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun insertarPersona(oUsuario: clsUsuario):Int = retrofit.create(clsUsuarioProvider::class.java).insertarUsuario(oUsuario)

    suspend fun getUsuario(idUsuario: String): clsUsuario =
        retrofit.create(clsUsuarioProvider::class.java)
            .getUsuario(Conexion.GET_USUARIO_POR_ID+idUsuario)

}