package com.example.myvideogamelist.models

import com.google.gson.annotations.SerializedName

data class clsEstado (
    @SerializedName("id")val id: Int,
    @SerializedName("nombreEstado")val nombreEstado: String
){
    override fun toString(): String {
        return nombreEstado //este toString para que luego en el dropDownList se vea bien
    }
}