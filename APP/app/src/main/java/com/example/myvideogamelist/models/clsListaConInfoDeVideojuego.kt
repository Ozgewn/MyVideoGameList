package com.example.myvideogamelist.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class clsListaConInfoDeVideojuego(
    @SerializedName("idUsuario")val idUsuario: String,
    @SerializedName("idVideojuego")val idVideojuego: Int,
    @SerializedName("nombreVideojuego")val nombreVideojuego: String,
    @SerializedName("nota")val notaPersonal: Int,
    @SerializedName("notaMediaVideojuego")val notaMediaVideojuego: Double,
    @SerializedName("dificultad")val dificultadPersonal: Int,
    @SerializedName("dificultadMediaVideojuego")val dificultadMediaVideojuego: Double,
    @SerializedName("fechaDeComienzo")val fechaDeComienzo: LocalDate,
    @SerializedName("fechaDeFinalizacion")val fechaDeFinalizacion: LocalDate,
    @SerializedName("generos")val generos: String,
    @SerializedName("estado")val estado: Int,
    @SerializedName("urlImagenVideojuego")val urlImagenVideojuego: String
)
