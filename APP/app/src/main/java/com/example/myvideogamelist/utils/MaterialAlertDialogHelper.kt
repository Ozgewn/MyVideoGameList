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