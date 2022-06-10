package com.example.myvideogamelist.views.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentRepetirContrasenyaBinding
import com.example.myvideogamelist.utils.InterfazUsuarioUtils
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RepetirContrasenyaFragment : Fragment() {

    private var _binding: FragmentRepetirContrasenyaBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var datosValidos = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRepetirContrasenyaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tIContrasenyaRepetida.editText!!.doOnTextChanged { _, _, _, _ ->
            if(binding.tIContrasenyaRepetida.editText!!.text!!.length >= 6){
                datosValidos = true
                binding.tIContrasenyaRepetida.error = null
            }else{
                datosValidos = false
                binding.tIContrasenyaRepetida.error = Mensajes.errores.PASSWORD_MASDE5CHARS
            }
        }
        //verificamos si la contraseña introducida es correcta...
        binding.btnConfirmar.setOnClickListener {
            InterfazUsuarioUtils.hideKeyboard(binding.root, this)
            if(datosValidos){
                auth.signInWithEmailAndPassword(auth.currentUser!!.email!!, binding.tIContrasenyaRepetida.editText!!.text.toString()).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        findNavController().navigate(R.id.modificarCredencialesFragment)
                    }else{
                        binding.tIContrasenyaRepetida.error = Mensajes.errores.SIGNUP_PASSWORD_NO_COINCIDEN
                        Snackbar.make(requireView(), Mensajes.errores.SIGNUP_PASSWORD_NO_COINCIDEN, Snackbar.LENGTH_SHORT).show()
                        datosValidos = false //este false aqui, para que si el usuario le da muchas veces al boton, que solo se realice la primera, y que esta variable no vuelva a
                        //ser true a menos que modifique el campo de texto
                    }
                }
            }else{
                Snackbar.make(requireView(), Mensajes.informacion.PEDIR_CORRECION_DE_ERRORES, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}