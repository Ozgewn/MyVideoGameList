package com.example.myvideogamelist.utils

import androidx.fragment.app.Fragment
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.google.android.material.snackbar.Snackbar

object SnackbarHelper {
    fun errorNoInternet(fragment: Fragment){
        Snackbar.make(fragment.requireView(), Mensajes.errores.CONEXION_INTERNET_FALLIDA, Snackbar.LENGTH_SHORT).show()
    }
}