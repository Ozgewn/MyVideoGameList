package com.example.myvideogamelist.utils

import androidx.fragment.app.Fragment
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.google.android.material.snackbar.Snackbar

object SnackbarHelper {
    /**
     * Cabecera: fun errorNoInternet(fragment: Fragment)
     * Descripcion: Este metodo se encarga de mostrar un Snackbar informando de que no hay Internet
     * Precondiciones: El fragment debe existir
     * Postcondiciones: Se mostrara un Snackbar informando de un error de conexion
     * Entrada:
     *      fragment: Fragment -> El fragment en el que se desea mostrar dicho Snackbar
     * Salida: N/A
     */
    fun errorNoInternet(fragment: Fragment){
        Snackbar.make(fragment.requireView(), Mensajes.errores.CONEXION_INTERNET_FALLIDA, Snackbar.LENGTH_SHORT).show()
    }
}