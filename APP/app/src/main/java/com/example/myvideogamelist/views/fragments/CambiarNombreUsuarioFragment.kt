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
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
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

        binding.tINuevoNombreUsuario.editText!!.doOnTextChanged { text, start, before, count ->
            binding.tINuevoNombreUsuario.error = null
            if(binding.tINuevoNombreUsuario.editText!!.text.length > 2){
                datosValidos = true
            }else{
                datosValidos = false
                binding.tINuevoNombreUsuario.error = Mensajes.errores.USERNAME_MASDE2CHARS
            }
        }

        binding.btnConfirmar.setOnClickListener {
            hideKeyboard()
            if(datosValidos){
                var nombreUsuario = binding.tINuevoNombreUsuario.editText!!.text.toString()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Confirmar")
                    .setMessage("¿Estás seguro de que deseas cambiar el nombre de usuario a $nombreUsuario?")
                    .setNeutralButton("Cancelar") { dialog, which ->
                        //No hacemos nada
                    }
                    .setPositiveButton("Confirmar") { dialog, which ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try{
                                val respuesta = clsUsuarioRepository().editarUsuario(clsUsuario(id = SharedData.idUsuario, nombreUsuario = nombreUsuario))
                                if(respuesta != 1){
                                    modificacionCompletadaCorrectamente = false
                                }
                            }catch (e: Exception){
                                activity?.runOnUiThread {
                                    binding.tINuevoNombreUsuario.error = Mensajes.errores.USERNAME_YA_EN_USO
                                }
                                modificacionCompletadaCorrectamente = false
                            }
                            activity?.runOnUiThread {
                                if(modificacionCompletadaCorrectamente){
                                    SharedData.nombreUsuario.postValue(nombreUsuario)
                                    Snackbar.make(requireView(), Mensajes.informacion.MODIFICACION_CREDENCIALES_EXITOSA_NO_RELOG, Snackbar.LENGTH_SHORT).show()
                                }else{
                                    Snackbar.make(requireView(), Mensajes.informacion.PEDIR_CORRECION_DE_ERRORES, Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                    .show()
            }else{
                Snackbar.make(requireView(), Mensajes.informacion.PEDIR_CORRECION_DE_ERRORES, Snackbar.LENGTH_SHORT).show()
                binding.tINuevoNombreUsuario.error = Mensajes.errores.USERNAME_MASDE2CHARS
            }

        }
    }

    /**
     * Oculta el teclado
     */
    private fun hideKeyboard(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

}