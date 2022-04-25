package com.example.myvideogamelist.api.core

import com.example.myvideogamelist.api.conexion.Conexion
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Conexion.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}