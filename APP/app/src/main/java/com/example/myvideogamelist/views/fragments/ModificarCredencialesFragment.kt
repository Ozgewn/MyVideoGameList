package com.example.myvideogamelist.views.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.example.myvideogamelist.utils.SnackbarHelper
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import com.example.myvideogamelist.views.textos.Textos
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException

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
                    .setTitle(Textos.TITULO_CONFIRMAR_BORRAR_CUENTA)
                    .setMessage(Textos.MENSAJE_CONFIRMAR_BORRAR_CUENTA)
                    .setNeutralButton(Textos.BOTON_CANCELAR) { dialog, which ->
                        // nada
                    }
                    .setPositiveButton(Textos.BOTON_BORRAR) { dialog, which ->
                        borrarCuenta()

                    }
                    .show()
            }
        }
    }

    private fun borrarCuenta() {
        val navControllerMainContent = requireParentFragment().requireParentFragment().findNavController()
        var respuesta = 0
        auth.currentUser!!.delete().addOnCompleteListener { task ->
            if(task.isSuccessful){
                CoroutineScope(Dispatchers.IO).launch {
                    try{
                        respuesta = clsUsuarioRepository().borrarUsuario(SharedData.idUsuario)
                        activity?.runOnUiThread {
                            navControllerMainContent.navigate(R.id.action_mainContentFragment_to_loginFragment)
                        }
                    }catch (e: UnknownHostException){
                        SnackbarHelper.errorNoInternet(this@ModificarCredencialesFragment)
                    }
                    catch (e: Exception){
                        Snackbar.make(requireView(), Mensajes.errores.ERROR_GENERICO, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }else{
                Snackbar.make(requireView(), Mensajes.errores.ERROR_GENERICO, Snackbar.LENGTH_SHORT).show()
            }
            if(respuesta == 0){
                Snackbar.make(requireView(), Mensajes.errores.ERROR_GENERICO, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}