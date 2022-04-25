package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.R
import com.example.myvideogamelist.api.repositories.clsUsuarioRepository
import com.example.myvideogamelist.databinding.FragmentSignUpBinding
import com.example.myvideogamelist.models.clsUsuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        var datosSignUpValidos = false

        /*
        Todos estos listeners y doOnTextChanged son simples validaciones que se hacen, y las hago de esta manera
        para que el usuario tenga un mejor feedback y siempre sepa qué esta fallando, y qué esta fallando concretamente
         */

        binding.signUpPassword.editText!!.setOnFocusChangeListener { _, hasFocus ->
            datosSignUpValidos = false
            binding.signUpPassword.error = null
            if(!hasFocus && binding.signUpPassword.editText!!.text.isNullOrEmpty()){
                binding.signUpPassword.error = Mensajes.errores.PASSWORD_VACIO
            }else if(!hasFocus && binding.signUpPassword.editText!!.text!!.length < 6){
                binding.signUpPassword.error = Mensajes.errores.PASSWORD_MASDE6CHARS
            }
        }
        binding.signUpUsername.editText!!.setOnFocusChangeListener { _, hasFocus ->
            datosSignUpValidos = false
            binding.signUpUsername.error = null
            if(!hasFocus && binding.signUpUsername.editText!!.text.isNullOrEmpty()){
                binding.signUpPassword.error = Mensajes.errores.SIGNUP_USERNAME_VACIO
            }else{
                datosSignUpValidos = true
            }
        }
        binding.signUpPassword.editText!!.doOnTextChanged { _, _, _, _ ->
            if(binding.signUpPassword.editText!!.text!!.length >= 6){
                datosSignUpValidos = true
            }
        }
        binding.signUpEmail.editText!!.doOnTextChanged { _, _, _, _ ->
            if(binding.signUpEmail.editText!!.text!!.length >= 3 && binding.signUpEmail.editText!!.text!!.contains("@")){
                datosSignUpValidos = true
            }
        }
        binding.signUpRepeatPassword.editText!!.doOnTextChanged { _, _, _, _ ->
            if(binding.signUpRepeatPassword.editText!!.text!!.toString() == binding.signUpPassword.editText!!.text!!.toString()){
                datosSignUpValidos = true
                binding.signUpRepeatPassword.error = null
            }else{
                binding.signUpRepeatPassword.error = Mensajes.errores.SIGNUP_PASSWORD_NO_COINCIDEN
            }
        }
        binding.signUpEmail.editText!!.setOnFocusChangeListener { _, hasFocus ->
            datosSignUpValidos = false
            binding.signUpEmail.error = null
            if(!hasFocus && binding.signUpEmail.editText!!.text.isNullOrEmpty()){
                binding.signUpEmail.error = Mensajes.errores.EMAIL_VACIO
            }else if(!hasFocus && !binding.signUpEmail.editText!!.text!!.contains("@")){
                binding.signUpEmail.error = Mensajes.errores.EMAIL_NOVALIDO
            }
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.signUpEmail.editText!!.text.toString()
            val password = binding.signUpPassword.editText!!.text.toString()

            if(datosSignUpValidos){
                register(email, password)
            }
        }
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity!!) { task ->
                if(task.isSuccessful){
                    var oUsuario = clsUsuario(id = task.result.user!!.uid, nombreUsuario = binding.signUpUsername.editText!!.text!!.toString()) //creamos el usuario para su posterior insercion en la BBDD
                    clsUsuarioRepository().insertarUsuario(oUsuario) //insertamos al usuario en la BBDD
                    navController.navigate(R.id.action_signUpFragment_to_loginFragment) //pasamos al login
                }else{
                    Toast.makeText(requireContext(), Mensajes.errores.SIGNUP_FALLIDO, Toast.LENGTH_LONG).show()
                }
            }
    }
}