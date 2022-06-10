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
import com.example.myvideogamelist.utils.SnackbarHelper
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
import java.net.UnknownHostException

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

        /*
        Todos estos listeners y doOnTextChanged son simples validaciones que se hacen, y las hago de esta manera
        para que el usuario tenga un mejor feedback y siempre sepa qué esta fallando, y qué esta fallando concretamente
         */
        inicializarListenersCamposTexto()
    }

    /**
     * Cabecera: private fun inicializarListenersCamposTexto()
     * Descripcion: Este metodo se encarga de inicializar todos los listeners que tienen los textos, los cuales son los encargados de
     * verificar si un campo es correcto, o no, y si todos son correctos, permitira hacer click en el boton de registro
     * Precondiciones: Ninguna
     * Postcondiciones: Se permitira o no el login, si los campos son correctos, o incorrectos
     * Entrada: N/A
     * Salida: N/A
     */
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

    /**
     * Cabecera: private fun register(email: String, password: String)
     * Descripcion: Este metodo registra al usuario en Firebase, y si el registro en Firebase es exitoso, tambien lo inserta en la BBDD a traves de la API
     * Precondiciones: Conexion a Internet
     * Postcondiciones: Se registrara al usuario en Firebase y en la BBDD
     * Entrada:
     *      email: String -> el email del usuario a registrar
     *      password: String -> la contraseña del usuario a registrar
     * Salida: N/A
     */
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
                            try{
                                insertarUsuarioEnBBDD(task, nombreUsuario)
                            }catch (e: UnknownHostException){
                                auth.currentUser!!.delete() //borramos la cuenta, porque no se ha podido insertar en la BBDD
                                activity?.runOnUiThread {
                                    SnackbarHelper.errorNoInternet(this@SignUpFragment)
                                }
                            }
                        }else{
                            controlarExcepcion(task)
                        }
                    }
            }
        }
        InterfazUsuarioUtils.hideKeyboard(binding.root, this@SignUpFragment)
    }

    /**
     * Cabecera: private fun insertarUsuarioEnBBDD(task: Task<AuthResult>, nombreUsuario: String)
     * Descripcion: Este metodo insertara al usuario en la BBDD, con el id recientemente creado por Firebase, y el nombre de usuario deseado
     * Precondiciones: Conexion a Internet, que el registro de Firebase haya sido correcto
     * Postcondiciones: Se insertara al usuario en la BBDD
     * Entrada:
     *      task: Task<AuthResult> -> este task contendra el id del usuario recientemente creado
     *      nombreUsuario: String -> El nombre de usuario del usuario a crear
     * Salida: N/A
     */
    private fun insertarUsuarioEnBBDD(task: Task<AuthResult>, nombreUsuario: String){
        val oUsuario = clsUsuario(id = task.result.user!!.uid, nombreUsuario = nombreUsuario) //creamos el usuario para su posterior insercion en la BBDD
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val respuesta = clsUsuarioRepository().insertarUsuario(oUsuario) //insertamos al usuario en la BBDD
                if(respuesta != 1){
                    Snackbar.make(requireView(), Mensajes.errores.ERROR_GENERICO, Snackbar.LENGTH_SHORT).show()
                }
                activity?.runOnUiThread {
                    //hacemos el navigate en el runOnUiThread, para que solo naveguemos cuando acabemos de insertar al usuario en la BBDD
                    navController.navigate(R.id.action_signUpFragment_to_loginFragment) //pasamos al login
                }
            }catch (e: UnknownHostException){
                activity?.runOnUiThread {
                    SnackbarHelper.errorNoInternet(this@SignUpFragment)
                }
            }
        }
    }

    /**
     * Cabecera: private fun controlarExcepcion(task: Task<AuthResult>)
     * Descripcion: Este metodo controla la excepciones que puede darse al registrarse
     * Precondiciones: Conexion a Internet
     * Postcondiciones: Se controlara la excepcion y se informara al usuario
     * Entrada:
     *      task: Task<AuthResult> -> este task contendra la excepcion que ha sido lanzada
     * Salida: N/A
     */
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