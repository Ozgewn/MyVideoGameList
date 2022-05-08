package com.example.myvideogamelist.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class clsVideojuego (
    @SerializedName("id")val id: Int,
    @SerializedName("nombre")val nombre: String,
    @SerializedName("notaMedia")val notaMedia: Double,
    @SerializedName("dificultadMedia")val dificultadMedia: Double,
    @SerializedName("generos")val generos: String,
    @SerializedName("desarrollador")val desarrollador: String,
    @SerializedName("distribuidores")val distribuidores: String,
    @SerializedName("fechaDeLanzamiento")val fechaDeLanzamiento: String,
    @SerializedName("plataformas")val plataformas: String,
    @SerializedName("urlImagen")val urlImagen: String
)