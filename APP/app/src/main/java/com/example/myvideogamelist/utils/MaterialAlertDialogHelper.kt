package com.example.myvideogamelist.utils

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

object MaterialAlertDialogHelper {
    /**
     * Cabecera: fun errorPorSerAnonimo(fragment: Fragment, auth: FirebaseAuth)
     * Descripcion: Este metodo se encarga de mostrar un AlertDialog avisando al usuario de que es anonimo, y que necesita iniciar sesion para acceder
     * a la funcionalidad en cuestion
     * Precondiciones: Se debe haber iniciado sesion como anonimo
     * Postcondiciones: Se mostrara un AlertDialog avisando del error, e informando la solucion
     * Entrada:
     *      fragment: Fragment -> El fragment desde el que se envia el AlertDialog, en este caso lo uso para encontrar el navController y navegar gracias a este
     *      auth: FirebaseAuth -> El objeto FirebaseAuth, en este caso lo uso para averiguar si el usuario es anonimo o no
     * Salida:
     */
    fun errorPorSerAnonimo(fragment: Fragment, auth: FirebaseAuth){
        var navController = fragment.findNavController()
        MaterialAlertDialogBuilder(fragment.requireContext())
            .setTitle("Error, eres anónimo")
            .setMessage("Ya que has iniciado sesión como anónimo, no puedes acceder a esta funcionalidad")
            .setNeutralButton("Iniciar Sesión") { dialog, which ->
                auth.signOut()
                if(navController.currentDestination!!.id != R.id.mainContentFragment){
                    navController = fragment.requireParentFragment().requireParentFragment().findNavController() //obtenemos el navController de Maincontent si el materialAlert se lanza desde otro fragment, por ejemplo, el de ranking
                    navController.navigate(R.id.action_mainContentFragment_to_loginFragment)
                }else{
                    navController.navigate(R.id.action_mainContentFragment_to_loginFragment)
                }
            }
            .setPositiveButton("Cancelar") { dialog, which ->

            }
            .show()
    }
}