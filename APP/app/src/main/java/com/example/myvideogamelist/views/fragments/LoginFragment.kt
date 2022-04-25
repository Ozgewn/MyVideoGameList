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
import com.example.myvideogamelist.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        var datosLoginValidos = false

        binding.loginPassword.editText!!.setOnFocusChangeListener { _, hasFocus ->
            datosLoginValidos = false
            binding.loginPassword.error = null
            if(!hasFocus && binding.loginPassword.editText!!.text.isNullOrEmpty()){
                binding.loginPassword.error = Mensajes.errores.PASSWORD_VACIO
            }else if(!hasFocus && binding.loginPassword.editText!!.text!!.length < 6){
                binding.loginPassword.error = Mensajes.errores.PASSWORD_MASDE6CHARS
            }
        }
        binding.loginPassword.editText!!.doOnTextChanged { _, _, _, _ ->
            if(binding.loginPassword.editText!!.text!!.length >= 6){
                datosLoginValidos = true
            }
        }
        binding.loginEmail.editText!!.doOnTextChanged { text, _, _, count ->
            if(binding.loginEmail.editText!!.text!!.length >= 3 && binding.loginEmail.editText!!.text!!.contains("@")){
                datosLoginValidos = true
            }
        }
        binding.loginEmail.editText!!.setOnFocusChangeListener { _, hasFocus ->
            datosLoginValidos = false
            binding.loginEmail.error = null
            if(!hasFocus && binding.loginEmail.editText!!.text.isNullOrEmpty()){
                binding.loginEmail.error = Mensajes.errores.EMAIL_VACIO
            }else if(!hasFocus && !binding.loginEmail.editText!!.text!!.contains("@")){
                binding.loginEmail.error = Mensajes.errores.EMAIL_NOVALIDO
            }
        }

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.editText?.text.toString()
            val password = binding.loginPassword.editText?.text.toString()

            if(datosLoginValidos){
                login(email, password)
            }
        }
        binding.loginGoRegisterButton.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
            binding.loginEmail.error = null
            binding.loginPassword.error = null
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            navController.navigate(R.id.action_loginFragment_to_mainContentFragment)
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    navController.navigate(R.id.action_loginFragment_to_mainContentFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        Mensajes.errores.LOGIN_FALLIDO,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}