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
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.R
import com.example.myvideogamelist.api.repositories.clsUsuarioRepository
import com.example.myvideogamelist.databinding.FragmentLoginBinding
import com.example.myvideogamelist.utils.InterfazUsuarioUtils
import com.example.myvideogamelist.utils.SnackbarHelper
import com.example.myvideogamelist.views.sharedData.SharedData
import com.example.myvideogamelist.views.validaciones.Validaciones
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException

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
        var eTPasswordCorrecta = false
        var eTCorreoCorrecto = false


        binding.loginPassword.editText!!.doOnTextChanged { _, _, _, _ ->
            eTPasswordCorrecta = Validaciones.validacionPassword(binding.loginPassword)
        }
        binding.loginEmail.editText!!.doOnTextChanged { _, _, _, _ ->
            eTCorreoCorrecto = Validaciones.validacionEmail(binding.loginEmail)
        }
        binding.loginButton.setOnClickListener {
            val datosLoginValidos = eTPasswordCorrecta && eTCorreoCorrecto
            val email = binding.loginEmail.editText?.text.toString()
            val password = binding.loginPassword.editText?.text.toString()

            if(datosLoginValidos){
                login(email, password)
            }else{
                binding.loginPassword.clearFocus()
                binding.loginEmail.clearFocus()
                InterfazUsuarioUtils.hideKeyboard(binding.root, this)
                Snackbar.make(requireView(), Mensajes.errores.LOGIN_SIGNUP_FALTAN_DATOS, Snackbar.LENGTH_LONG).show()
            }
        }
        binding.loginGoRegisterButton.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
            binding.loginEmail.error = null
            binding.loginPassword.error = null
        }
        binding.loginAnonymous.setOnClickListener {
            auth.signInAnonymously().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    SharedData.idUsuario = "anonimo"
                    navController.navigate(R.id.action_loginFragment_to_mainContentFragment)
                    actualizarNombreEnSharedData()
                }else{
                    Snackbar.make(requireView(), Mensajes.errores.ERROR_GENERICO, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            if(auth.currentUser!!.isAnonymous){
                SharedData.idUsuario = "anonimo"
            }else{
                SharedData.idUsuario = auth.currentUser!!.uid //Lo pongo en la SharedData para no estar pasando Firebase en todos los fragments
            }
            actualizarNombreEnSharedData()
            navController.navigate(R.id.action_loginFragment_to_mainContentFragment)
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    SharedData.idUsuario = task.result.user!!.uid
                    actualizarNombreEnSharedData()
                    InterfazUsuarioUtils.hideKeyboard(binding.root, this)
                    navController.navigate(R.id.action_loginFragment_to_mainContentFragment)
                } else {
                    Snackbar.make(
                        requireView(),
                        Mensajes.errores.LOGIN_FALLIDO,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    /**
     * Actualiza el nombre de usuario en la clase estatica SharedData haciendo uso del idUsuario
     */
    private fun actualizarNombreEnSharedData(){
        if(auth.currentUser!!.isAnonymous){
            SharedData.nombreUsuario.postValue("Anónimo")
        }else{
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    SharedData.nombreUsuario.postValue(clsUsuarioRepository().getUsuario(SharedData.idUsuario).nombreUsuario)
                }catch (e: UnknownHostException){
                    SnackbarHelper.errorNoInternet(this@LoginFragment)
                }
            }
        }

    }
}