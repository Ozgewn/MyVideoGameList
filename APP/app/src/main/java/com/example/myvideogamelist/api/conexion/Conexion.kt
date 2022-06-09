package com.example.myvideogamelist.api.conexion

object Conexion{
    const val BASE_URL = "https://myvideogamelist.azurewebsites.net/api/"
    const val GET_USUARIO_POR_ID = "Usuario/id/"
    const val POST_PUT_USUARIO = "Usuario"
    const val COMPROBAR_EXISTENCIA_USUARIO = "Usuario/comprobarExistencia/"
    const val DELETE_USUARIO = "Usuario/"
    const val GET_LISTADO_COMPLETO_USUARIOS = "Usuario"
    const val GET_POST_PUT_LISTA_VIDEOJUEGOS_POR_USUARIO = "listavideojuegos/"
    const val GET_LISTA_ESTADOS="estados"
    const val DELETE_LISTAVIDEOJUEGOS_USUARIO="listavideojuegos/Usuario/"
    const val DELETE_LISTAVIDEOJUEGOS_VIDEOJUEGO="Videojuego/"
    const val GET_VIDEOJUEGO="Videojuego/"
    const val GET_SOLO_VIDEOJUEGOS_EN_LISTA= GET_POST_PUT_LISTA_VIDEOJUEGOS_POR_USUARIO+"soloEnLista/"
}