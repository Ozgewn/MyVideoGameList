package com.example.myvideogamelist.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(private val context: Context) {

    private val mySharedPref = context.getSharedPreferences("DARK_THEME", Context.MODE_PRIVATE)
    //constantes estaticas
    object constantes{
        const val KEY_MODO_NOCHE = "isDarkModeOn"
    }

    fun setModoNoche(modoNoche: Boolean){
        val editor: SharedPreferences.Editor = mySharedPref.edit()
        editor.putBoolean(constantes.KEY_MODO_NOCHE,modoNoche)
        editor.apply()
    }
    fun getModoNoche(): Boolean = mySharedPref.getBoolean(constantes.KEY_MODO_NOCHE, false)

}