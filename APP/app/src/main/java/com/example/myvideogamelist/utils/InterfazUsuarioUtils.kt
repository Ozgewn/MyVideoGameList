package com.example.myvideogamelist.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

object InterfazUsuarioUtils {

    /**
     * Oculta el teclado
     */
    fun hideKeyboard(root: View, fragment: Fragment){
        val imm = fragment.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(root.windowToken, 0)
    }

}