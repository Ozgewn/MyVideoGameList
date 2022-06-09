package com.example.myvideogamelist.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.api.repositories.clsUsuarioRepository
import com.example.myvideogamelist.databinding.FragmentModificarCredencialesBinding
import com.example.myvideogamelist.utils.InterfazUsuarioUtils
import com.example.myvideogamelist.views.sharedData.SharedData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModificarCredencialesFragment : Fragment() {

    private var _binding: FragmentModificarCredencialesBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModificarCredencialesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        var navControllerMainContent = requireParentFragment().requireParentFragment().findNavController()
        with(binding){
            tVCambiarEmail.setOnClickListener {
                navController.navigate(R.id.cambiarEmailFragment)
                InterfazUsuarioUtils.hideKeyboard(binding.root, this@ModificarCredencialesFragment)
            }
            tVCambiarNombreUsuario.setOnClickListener {
                navController.navigate(R.id.cambiarNombreUsuarioFragment)
                InterfazUsuarioUtils.hideKeyboard(binding.root, this@ModificarCredencialesFragment)
            }
            tVCambiarPassword.setOnClickListener {
                navController.navigate(R.id.cambiarPasswordFragment)
                InterfazUsuarioUtils.hideKeyboard(binding.root, this@ModificarCredencialesFragment)
            }
            tVBorrarCuenta.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("¿Desea borrar su cuenta?")
                    .setMessage("¡ANTENCIÓN! Esta acción es irreversible, ¿está realmente seguro de que desea borrar su cuenta? No la podrá recuperar.")
                    .setNeutralButton("cancelar") { dialog, which ->
                        // nada
                    }
                    .setPositiveButton("Borrar") { dialog, which ->
                        auth.currentUser!!.delete()
                        CoroutineScope(Dispatchers.IO).launch {
                            clsUsuarioRepository().borrarUsuario(SharedData.idUsuario)
                        }
                        navControllerMainContent.navigate(R.id.action_mainContentFragment_to_loginFragment)
                    }
                    .show()
            }
        }
    }

}