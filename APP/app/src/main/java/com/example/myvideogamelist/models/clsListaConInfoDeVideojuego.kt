package com.example.myvideogamelist.models

import com.google.gson.annotations.SerializedName

data class clsListaConInfoDeVideojuego(
    @SerializedName("idUsuario")val idUsuario: String,
    @SerializedName("idVideojuego")val idVideojuego: Int,
)
