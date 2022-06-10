package com.example.myvideogamelist.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.api.repositories.clsUsuarioRepository
import com.example.myvideogamelist.databinding.FragmentCambiarNombreUsuarioBinding
import com.example.myvideogamelist.models.clsUsuario
import com.example.myvideogamelist.utils.InterfazUsuarioUtils
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import com.example.myvideogamelist.views.textos.Textos
import com.example.myvideogamelist.views.validaciones.Validaciones
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CambiarNombreUsuarioFragment : Fragment() {
    private var _binding: FragmentCambiarNombreUsuarioBinding? = null
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
        _binding = FragmentCambiarNombreUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var modificacionCompletadaCorrectamente = true
        var datosValidos = false

        binding.tINuevoNombreUsuario.editText!!.doOnTextChanged { _, _, _, _ ->
            datosValidos = Validaciones.validacionUsername(binding.tINuevoNombreUsuario)
        }

        binding.btnConfirmar.setOnClickListener {
            InterfazUsuarioUtils.hideKeyboard(binding.root, this)
            if(datosValidos){
                val nombreUsuario = binding.tINuevoNombreUsuario.editText!!.text.toString()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(Textos.TITULO_CONFIRMAR_CAMBIO_NOMBRE_USUARIO)
                    .setMessage(String.format(Textos.MENSAJE_CONFIRMAR_CAMBIO_NOMBRE_USUARIO, nombreUsuario))
                    .setNeutralButton(Textos.BOTON_CANCELAR) { dialog, which ->
                        //No hacemos nada
                    }
                    .setPositiveButton(Textos.BOTON_CONFIRMAR) { dialog, which ->
                        modificacionCompletadaCorrectamente = editarNombreUsuario(nombreUsuario)
                    }
                    .show()
            }else{
                Snackbar.make(requireView(), Mensajes.informacion.PEDIR_CORRECION_DE_ERRORES, Snackbar.LENGTH_SHORT).show()
                binding.tINuevoNombreUsuario.error = Mensajes.errores.USERNAME_MASDE2CHARS
            }

        }
    }

    private fun editarNombreUsuario(nombreUsuario: String): Boolean {
        var modificacionRealizada = true
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val respuesta = clsUsuarioRepository().editarUsuario(clsUsuario(id = SharedData.idUsuario, nombreUsuario = nombreUsuario))
                if(respuesta != 1){
                    modificacionRealizada = false
                }
            }catch (e: Exception){
                activity?.runOnUiThread {
                    binding.tINuevoNombreUsuario.error = Mensajes.errores.USERNAME_YA_EN_USO
                }
                modificacionRealizada = false
            }
            activity?.runOnUiThread {
                if(modificacionRealizada){
                    SharedData.nombreUsuario.postValue(nombreUsuario)
                    Snackbar.make(requireView(), Mensajes.informacion.MODIFICACION_CREDENCIALES_EXITOSA_NO_RELOG, Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(requireView(), Mensajes.informacion.PEDIR_CORRECION_DE_ERRORES, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        return modificacionRealizada
    }

}