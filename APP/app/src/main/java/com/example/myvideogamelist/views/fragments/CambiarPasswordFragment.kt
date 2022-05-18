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
import com.example.myvideogamelist.databinding.FragmentCambiarPasswordBinding
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class CambiarPasswordFragment : Fragment() {
    private var _binding: FragmentCambiarPasswordBinding? = null
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
        _binding = FragmentCambiarPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var datosValidos = false
        var navController = requireParentFragment().requireParentFragment().findNavController()

        binding.tINuevaPassword.editText!!.setOnFocusChangeListener { _, hasFocus ->
            datosValidos = false
            binding.tINuevaPassword.error = null
            if(!hasFocus && binding.tINuevaPassword.editText!!.text.isNullOrEmpty()){
                binding.tINuevaPassword.error = Mensajes.errores.PASSWORD_VACIO
            }else if(!hasFocus && binding.tINuevaPassword.editText!!.text!!.length < 6){
                binding.tINuevaPassword.error = Mensajes.errores.PASSWORD_MASDE5CHARS
            }
        }

        binding.tINuevaPassword.editText!!.doOnTextChanged { _, _, _, _ ->
            if(binding.tINuevaPassword.editText!!.text!!.length >= 6){
                datosValidos = true
            }
        }

        binding.tINuevaPasswordRepetida.editText!!.doOnTextChanged { _, _, _, _ ->
            if(binding.tINuevaPasswordRepetida.editText!!.text!!.toString() == binding.tINuevaPassword.editText!!.text!!.toString()){
                datosValidos = true
                binding.tINuevaPasswordRepetida.error = null
            }else{
                binding.tINuevaPasswordRepetida.error = Mensajes.errores.SIGNUP_PASSWORD_NO_COINCIDEN
            }
        }

        binding.btnConfirmar.setOnClickListener {
            hideKeyboard()
            var password = binding.tINuevaPassword.editText!!.text.toString()
            if(binding.tINuevaPasswordRepetida.editText!!.text.toString() == password){
                if(datosValidos){
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Confirmar")
                        .setMessage("¿Estás seguro de que deseas cambiar tu contraseña?")
                        .setNeutralButton("Cancelar") { dialog, which ->
                            //No hacemos nada
                        }
                        .setPositiveButton("Confirmar"){ dialog, which ->
                            auth.currentUser!!.updatePassword(password).addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    Snackbar.make(requireView(), Mensajes.informacion.MODIFICACION_CREDENCIALES_EXITOSA, Snackbar.LENGTH_SHORT).show()
                                    auth.signOut()
                                    navController.navigate(R.id.action_mainContentFragment_to_loginFragment)
                                }
                            }
                        }

                }else{
                    Snackbar.make(requireView(), Mensajes.informacion.PEDIR_CORRECION_DE_ERRORES, Snackbar.LENGTH_SHORT).show()
                }
            }else{
                binding.tINuevaPasswordRepetida.error = Mensajes.errores.SIGNUP_PASSWORD_NO_COINCIDEN
                Snackbar.make(requireView(), Mensajes.informacion.PEDIR_CORRECION_DE_ERRORES, Snackbar.LENGTH_SHORT).show()
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