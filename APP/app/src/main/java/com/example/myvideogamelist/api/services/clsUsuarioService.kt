package com.example.myvideogamelist.api.services

import com.example.myvideogamelist.api.core.RetrofitHelper
import com.example.myvideogamelist.api.providers.clsUsuarioProvider
import com.example.myvideogamelist.models.clsUsuario
import com.google.firebase.auth.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsUsuarioService {

    private val retrofit = RetrofitHelper.getRetrofit()

    fun insertarPersona(oUsuario: clsUsuario):clsUsuario?{
        var oUsuarioInsertado : clsUsuario? = null
        val response = retrofit.create(clsUsuarioProvider::class.java).insertarUsuario(oUsuario).enqueue(
            object : Callback<clsUsuario>{
                override fun onFailure(call: Call<clsUsuario>, t: Throwable) {
                }
                override fun onResponse(
                    call: Call<clsUsuario>,
                    response: Response<clsUsuario>
                ) {
                    oUsuarioInsertado = response.body()
                }
            }
        )
        return oUsuarioInsertado
    }

}