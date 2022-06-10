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
import com.example.myvideogamelist.views.textos.Textos
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
                    SharedData.idUsuario = Textos.USUARIO_ANONIMO_ID_NOMBRE_USUARIO
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
                SharedData.idUsuario = Textos.USUARIO_ANONIMO_ID_NOMBRE_USUARIO
            }else{
                SharedData.idUsuario = auth.currentUser!!.uid //Lo pongo en la SharedData para no estar pasando Firebase en todos los fragments
            }
            actualizarNombreEnSharedData()
            navController.navigate(R.id.action_loginFragment_to_mainContentFragment)
        }
    }

    /**
     * Cabecera: private fun login(email: String, password: String)
     * Descripcion: Este metodo se encarga de iniciar sesion
     * Precondiciones: Conexion a Internet, el usuario debe existir previamente
     * Postcondiciones: Se iniciara sesion si todos los campos son correctos y el usuario existe
     * Entrada:
     *      email: String -> el email del usuario que desea iniciar sesion
     *      password: String -> la contraseña del usuario que desea iniciar sesion
     * Salida: N/A
     */
    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    SharedData.idUsuario = task.result.user!!.uid
                    actualizarNombreEnSharedData()
                    navController.navigate(R.id.action_loginFragment_to_mainContentFragment)
                } else {
                    Snackbar.make(
                        requireView(),
                        Mensajes.errores.LOGIN_FALLIDO,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        InterfazUsuarioUtils.hideKeyboard(binding.root, this)
    }

    /**
     * Cabecera: private fun actualizarNombreEnSharedData()
     * Descripcion: Este metodo se encarga de actualizar el nombre de usuario en la clase estatica SharedData haciendo uso del idUsuario
     * Precondiciones: Se debe haber iniciado sesion
     * Postcondiciones: Se avisara al observable de que el nombre de usuario en la clase SharedData ha cambiado
     * Entrada: N/A
     * Salida: N/A
     */
    private fun actualizarNombreEnSharedData(){
        if(auth.currentUser!!.isAnonymous){
            SharedData.nombreUsuario.postValue(Textos.USUARIO_ANONIMO_ID_NOMBRE_USUARIO)
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