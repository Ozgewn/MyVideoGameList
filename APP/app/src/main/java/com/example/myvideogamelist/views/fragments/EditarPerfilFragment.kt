package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.api.repositories.clsUsuarioRepository
import com.example.myvideogamelist.databinding.FragmentEditarPerfilBinding
import com.example.myvideogamelist.models.clsUsuario
import com.example.myvideogamelist.viewmodels.UsuarioViewModel
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditarPerfilFragment : Fragment() {

    private var _binding: FragmentEditarPerfilBinding? = null
    private val binding get() = _binding!!
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private lateinit var usuarioConNuevosCambios: clsUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuarioConNuevosCambios = clsUsuario(id = usuarioViewModel.usuarioSeleccionado.value!!.id,
            nombreUsuario = usuarioViewModel.usuarioSeleccionado.value!!.nombreUsuario,
            esListaPrivada = usuarioViewModel.usuarioSeleccionado.value!!.esListaPrivada)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentEditarPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swListaPrivada.isChecked = usuarioViewModel.usuarioSeleccionado.value!!.esListaPrivada
        binding.swListaPrivada.setOnCheckedChangeListener { buttonView, isChecked ->
            usuarioConNuevosCambios.esListaPrivada = isChecked
        }
        binding.btnGuardarCambios.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                clsUsuarioRepository().editarUsuario(usuarioConNuevosCambios)
                activity?.runOnUiThread {
                    Snackbar.make(requireView(), Mensajes.informacion.CAMBIOS_CORRECTOS_GENERICO, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnModificarCredenciales.setOnClickListener {
            findNavController().navigate(R.id.repetirContrasenyaFragment)
        }
    }

}