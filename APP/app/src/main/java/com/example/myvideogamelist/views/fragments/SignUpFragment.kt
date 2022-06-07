package com.example.myvideogamelist.views.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.R
import com.example.myvideogamelist.api.repositories.clsUsuarioRepository
import com.example.myvideogamelist.databinding.FragmentSignUpBinding
import com.example.myvideogamelist.models.clsUsuario
import com.example.myvideogamelist.utils.InterfazUsuarioUtils
import com.example.myvideogamelist.views.validaciones.Validaciones
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private val thisFragment = this //esto es para poder obtener el fragment en la corrutina, porque si hago this en la corrutina, me cogera el CorroutineScope

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

        /*
        Todos estos listeners y doOnTextChanged son simples validaciones que se hacen, y las hago de esta manera
        para que el usuario tenga un mejor feedback y siempre sepa qué esta fallando, y qué esta fallando concretamente
         */
        inicializarListenersCamposTexto()
    }

    private fun inicializarListenersCamposTexto(){
        var eTPasswordCorrecta = false
        var eTUsernameCorrecto = false
        var eTCorreoCorrecto = false
        var eTRepetirPasswordCorrecto = false
        var datosSignUpValidos = false

        binding.signUpPassword.editText!!.doOnTextChanged { _, _, _, _ ->
            eTPasswordCorrecta = Validaciones.validacionPassword(binding.signUpPassword)
        }

        binding.signUpEmail.editText!!.doOnTextChanged { _, _, _, _ ->
            eTCorreoCorrecto = Validaciones.validacionEmail(binding.signUpEmail)
        }

        binding.signUpRepeatPassword.editText!!.doOnTextChanged { _, _, _, _ ->
            eTRepetirPasswordCorrecto = Validaciones.validacionRepetirPassword(binding.signUpRepeatPassword, binding.signUpPassword.editText!!.text.toString())
        }

        binding.signUpUsername.editText!!.doOnTextChanged { _, _, _, _ ->
            eTUsernameCorrecto = Validaciones.validacionUsername(binding.signUpUsername)
        }

        binding.signUpGoLoginButton.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_loginFragment) //pasamos al login
        }

        binding.signUpButton.setOnClickListener {
            datosSignUpValidos = eTPasswordCorrecta && eTUsernameCorrecto && eTCorreoCorrecto && eTRepetirPasswordCorrecto
            val email = binding.signUpEmail.editText!!.text.toString()
            val password = binding.signUpPassword.editText!!.text.toString()

            if(datosSignUpValidos){
                register(email, password)
            }else{
                Snackbar.make(requireView(), Mensajes.informacion.ERRORES_SIN_CORREGIR, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun register(email: String, password: String) {
        val nombreUsuario = binding.signUpUsername.editText!!.text!!.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val nombreUsuarioExistente = clsUsuarioRepository().comprobarExistenciaUsuario(nombreUsuario)
            if(nombreUsuarioExistente){ //si el nombre existe, mostramos un error
                activity?.runOnUiThread {
                    binding.signUpUsername.error = Mensajes.errores.USERNAME_YA_EN_USO
                }
            }else{ //si no existe, seguimos con la creacion del usuario
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity!!) { task ->
                        if(task.isSuccessful){
                            insertarUsuarioEnBBDD(task, nombreUsuario)
                            InterfazUsuarioUtils.hideKeyboard(binding.root, thisFragment)
                        }else{
                            controlarExcepcion(task)
                        }
                    }
            }
        }

    }

    private fun insertarUsuarioEnBBDD(task: Task<AuthResult>, nombreUsuario: String){
        val oUsuario = clsUsuario(id = task.result.user!!.uid, nombreUsuario = nombreUsuario) //creamos el usuario para su posterior insercion en la BBDD
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = clsUsuarioRepository().insertarUsuario(oUsuario) //insertamos al usuario en la BBDD
            if(respuesta != 1){
                Snackbar.make(requireView(), Mensajes.errores.ERROR_GENERICO, Snackbar.LENGTH_SHORT).show()
            }
            activity?.runOnUiThread {
                //hacemos el navigate en el runOnUiThread, para que solo naveguemos cuando acabemos de insertar al usuario en la BBDD
                navController.navigate(R.id.action_signUpFragment_to_loginFragment) //pasamos al login
            }
        }
    }

    private fun controlarExcepcion(task: Task<AuthResult>){
        /*
        A continuacion vamos a manejar la excepcion de cuando se crea un usuario que ya tiene cuenta, si ponemos un try-catch capturando esa excepcion, no saltara al catch,
        asi que lo tenemos que hacer con un if
         */
        if(task.exception!!::class.java == FirebaseAuthUserCollisionException::class.java){
            binding.signUpEmail.error = Mensajes.errores.SIGNUP_EMAIL_REPETIDO
        }else{
            Snackbar.make(requireView(), Mensajes.errores.SIGNUP_FALLIDO, Snackbar.LENGTH_LONG).show()
        }
    }
}