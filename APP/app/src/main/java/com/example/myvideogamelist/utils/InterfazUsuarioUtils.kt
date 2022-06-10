package com.example.myvideogamelist.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

object InterfazUsuarioUtils {

    /**
     * Cabecera: fun hideKeyboard(root: View, fragment: Fragment)
     * Descripcion: Oculta el teclado
     * Precondiciones: Se debe tener el teclado abierto
     * Postcondiciones: Se ocultara el teclado
     * Entrada:
     *      root: View -> La vista desde la cual se desea ocultar el teclado, en este caso la usamos para cerrar el teclado
     *      fragment: Fragment -> El fragment desde el cual se desea ocultar el teclado
     * Salida: N/A
     */
    fun hideKeyboard(root: View, fragment: Fragment){
        val imm = fragment.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(root.windowToken, 0)
    }

}