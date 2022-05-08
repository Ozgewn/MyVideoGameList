package com.example.myvideogamelist.api.providers

import com.example.myvideogamelist.api.conexion.Conexion
import com.example.myvideogamelist.models.clsEstado
import retrofit2.Response
import retrofit2.http.GET

interface clsEstadoProvider {
    @GET(Conexion.GET_LISTA_ESTADOS)
    suspend fun getListaEstados(): List<clsEstado>
}