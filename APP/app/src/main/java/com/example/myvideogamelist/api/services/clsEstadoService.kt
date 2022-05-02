package com.example.myvideogamelist.api.services

import com.example.myvideogamelist.api.core.RetrofitHelper
import com.example.myvideogamelist.api.providers.clsEstadoProvider
import com.example.myvideogamelist.models.clsEstado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.create

class clsEstadoService {

    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getListaEstados(): List<clsEstado>{
        return withContext(Dispatchers.IO){
            val response = retrofit.create(clsEstadoProvider::class.java).getListaEstados()
            response.body() ?: emptyList()
        }
    }

}