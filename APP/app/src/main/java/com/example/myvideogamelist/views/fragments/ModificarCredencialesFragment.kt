package com.example.myvideogamelist.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentModificarCredencialesBinding

class ModificarCredencialesFragment : Fragment() {

    private var _binding: FragmentModificarCredencialesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModificarCredencialesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        with(binding){
            tVCambiarEmail.setOnClickListener {
                navController.navigate(R.id.cambiarEmailFragment)
                hideKeyboard()
            }
            tVCambiarNombreUsuario.setOnClickListener {
                navController.navigate(R.id.cambiarNombreUsuarioFragment)
                hideKeyboard()
            }
            tVCambiarPassword.setOnClickListener {
                navController.navigate(R.id.cambiarPasswordFragment)
                hideKeyboard()
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