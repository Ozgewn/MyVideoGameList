package com.example.myvideogamelist.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentCambiarEmailBinding
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CambiarEmailFragment : Fragment() {
    private var _binding: FragmentCambiarEmailBinding? = null
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
        _binding = FragmentCambiarEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Obtenemos el navController del MainContent (el navHost), para que nos permita navegar a la pantalla de login
        var navController = requireParentFragment().requireParentFragment().findNavController()
        var datosValidos = false

        binding.tINuevoEmail.editText!!.setOnFocusChangeListener { _, hasFocus ->
            datosValidos = false
            binding.tINuevoEmail.error = null
            if(!hasFocus && binding.tINuevoEmail.editText!!.text.isNullOrEmpty()){
                binding.tINuevoEmail.error = Mensajes.errores.EMAIL_VACIO
            }else if(!hasFocus && !binding.tINuevoEmail.editText!!.text!!.contains("@")){
                binding.tINuevoEmail.error = Mensajes.errores.EMAIL_NOVALIDO
            }
        }

        binding.tINuevoEmail.editText!!.doOnTextChanged { _, _, _, _ ->
            if(binding.tINuevoEmail.editText!!.text!!.length >= 3 && binding.tINuevoEmail.editText!!.text!!.contains("@")){
                datosValidos = true
            }
        }

        binding.btnConfirmar.setOnClickListener {
            hideKeyboard()
            if(datosValidos){
                var email = binding.tINuevoEmail.editText!!.text.toString()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Confirmar")
                    .setMessage("¿Estás seguro de que deseas cambiar el email a $email?")
                    .setNeutralButton("Cancelar") { dialog, which ->
                        //No hacemos nada
                    }
                    .setPositiveButton("Confirmar") { dialog, which ->
                        auth.currentUser!!.updateEmail(email).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                Snackbar.make(requireView(), Mensajes.informacion.MODIFICACION_CREDENCIALES_EXITOSA, Snackbar.LENGTH_SHORT).show()
                                auth.signOut()
                                navController.navigate(R.id.action_mainContentFragment_to_loginFragment)
                            }
                        }
                    }
                    .show()
            }else{
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