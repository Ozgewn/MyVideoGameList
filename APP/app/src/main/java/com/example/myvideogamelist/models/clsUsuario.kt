package com.example.myvideogamelist.models

import com.google.gson.annotations.SerializedName

data class clsUsuario(
    @SerializedName("id")val id: String,
    @SerializedName("nombreUsuario")val nombreUsuario: String,
    @SerializedName("videojuegosJugando")val videojuegosJugando: Int = 0,
    @SerializedName("videojuegosJugados")val videojuegosJugados: Int = 0,
    @SerializedName("videojuegosEnPausa")val videojuegosEnPausa: Int = 0,
    @SerializedName("videojuegosDropeados")val videojuegosDropeados: Int = 0,
    @SerializedName("videojuegosPlaneados")val videojuegosPlaneados: Int = 0,
    @SerializedName("esListaPrivada")var esListaPrivada: Boolean = false,
)
