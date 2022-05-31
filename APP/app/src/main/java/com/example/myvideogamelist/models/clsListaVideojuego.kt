package com.example.myvideogamelist.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class clsListaVideojuego (
    @SerializedName("idUsuario")var idUsuario:String,
    @SerializedName("idVideojuego")var idVideojuego:Int,
    @SerializedName("fechaDeComienzo")var fechaDeComienzo:String? = null,
    @SerializedName("fechaDeFinalizacion")var fechaDeFinalizacion:String? = null,
    @SerializedName("nota")var nota:Int = 0,
    @SerializedName("dificultad")var dificultad:Int = 0,
    @SerializedName("estado")var estado:Int = 0,
)