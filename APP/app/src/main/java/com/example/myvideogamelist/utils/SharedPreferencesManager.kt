package com.example.myvideogamelist.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(private val context: Context) {

    private val mySharedPref = context.getSharedPreferences("DARK_THEME", Context.MODE_PRIVATE)
    //constantes estaticas
    object constantes{
        const val KEY_MODO_NOCHE = "isDarkModeOn"
    }

    /**
     * Cabecera: fun setModoNoche(modoNoche: Boolean)
     * Descripcion: Este metodo se encarga de guardar en un SharedPreferences si el usuario tiene activado el modo noche o no
     * Precondiciones: N/A
     * Postcondiciones: Se guardara y se activara/desactivara el modo noche, segun el usuario lo desee
     * Entrada:
     *      modoNoche: Boolean -> True si se quiere activar, false si se quiere desactivar
     * Salida: N/A
     */
    fun setModoNoche(modoNoche: Boolean){
        val editor: SharedPreferences.Editor = mySharedPref.edit()
        editor.putBoolean(constantes.KEY_MODO_NOCHE,modoNoche)
        editor.apply()
    }

    /**
     * Cabecera: fun getModoNoche()
     * Descripcion: Este metodo se encarga de obtener si el modo noche esta activado o no de las SharedPreferences
     * Precondiciones: N/A
     * Postcondiciones: Se devolvera si el modo noche esta activado o no
     * Entrada: N/A
     * Salida:
     *      Boolean -> True si esta activado, false si no
     */
    fun getModoNoche(): Boolean = mySharedPref.getBoolean(constantes.KEY_MODO_NOCHE, false)
}