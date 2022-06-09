package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentCambiarPasswordBinding
import com.example.myvideogamelist.utils.InterfazUsuarioUtils
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.validaciones.Validaciones
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
        val navController = requireParentFragment().requireParentFragment().findNavController()
        var eTNuevaPasswordCorrecta = false
        var eTNuevaPasswordRepetidaCorrecta = false

        binding.tINuevaPassword.editText!!.doOnTextChanged { _, _, _, _ ->
            eTNuevaPasswordCorrecta = Validaciones.validacionPassword(binding.tINuevaPassword)
        }

        binding.tINuevaPasswordRepetida.editText!!.doOnTextChanged { _, _, _, _ ->
            eTNuevaPasswordRepetidaCorrecta = Validaciones.validacionRepetirPassword(binding.tINuevaPasswordRepetida, binding.tINuevaPassword.editText!!.text.toString())
        }

        binding.btnConfirmar.setOnClickListener {
            InterfazUsuarioUtils.hideKeyboard(binding.root, this)
            val datosValidos = eTNuevaPasswordCorrecta && eTNuevaPasswordRepetidaCorrecta
            val password = binding.tINuevaPassword.editText!!.text.toString()
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

}