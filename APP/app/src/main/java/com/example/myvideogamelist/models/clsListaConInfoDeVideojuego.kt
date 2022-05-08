package com.example.myvideogamelist.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class clsListaConInfoDeVideojuego(
    @SerializedName("idUsuario") var idUsuario: String?,
    @SerializedName("idVideojuego")val idVideojuego: Int,
    @SerializedName("nombreVideojuego")val nombreVideojuego: String,
    @SerializedName("nota") var notaPersonal: Int,
    @SerializedName("notaMediaVideojuego")val notaMediaVideojuego: Double,
    @SerializedName("dificultad") var dificultadPersonal: Int,
    @SerializedName("dificultadMediaVideojuego")val dificultadMediaVideojuego: Double,
    @SerializedName("fechaDeComienzo")val fechaDeComienzo: LocalDate,
    @SerializedName("fechaDeFinalizacion")val fechaDeFinalizacion: LocalDate,
    @SerializedName("generos")val generos: String,
    @SerializedName("estado") var estado: Int,
    @SerializedName("urlImagenVideojuego")val urlImagenVideojuego: String
)
