package com.example.myvideogamelist.api.repositories

import com.example.myvideogamelist.api.services.clsEstadoService
import com.example.myvideogamelist.models.clsEstado

class clsEstadoRepository {

    private val api = clsEstadoService()

    suspend fun getListaEstados(): List<clsEstado>{
        return api.getListaEstados()
    }
}