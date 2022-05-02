package com.example.myvideogamelist.api.services

import com.example.myvideogamelist.api.core.RetrofitHelper
import com.example.myvideogamelist.api.providers.clsListaVideojuegoProvider
import com.example.myvideogamelist.models.clsListaVideojuego
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsListaVideojuegoService {

    private val retrofit = RetrofitHelper.getRetrofit()

    fun insertarVideojuego(oVideojuegoEnLista: clsListaVideojuego): Int?{
        var filasInsertadas : Int? = null
        retrofit.create(clsListaVideojuegoProvider::class.java).insertarVideojuegoEnLista(oVideojuegoEnLista).enqueue(
            object : Callback<Int> {
                override fun onFailure(call: Call<Int>, t: Throwable) {
                }
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    filasInsertadas = response.body()
                }
            }
        )
        return filasInsertadas
    }

    fun editarVideojuego(oVideojuegoEnLista: clsListaVideojuego): Int?{
        var filasInsertadas : Int? = null
        retrofit.create(clsListaVideojuegoProvider::class.java).editarVideojuegoEnLista(oVideojuegoEnLista).enqueue(
            object : Callback<Int> {
                override fun onFailure(call: Call<Int>, t: Throwable) {
                }
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    filasInsertadas = response.body()
                }
            }
        )
        return filasInsertadas
    }

}