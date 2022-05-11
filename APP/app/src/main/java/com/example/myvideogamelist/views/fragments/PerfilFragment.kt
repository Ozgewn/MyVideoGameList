package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentPerfilBinding
import com.example.myvideogamelist.models.clsUsuario
import com.example.myvideogamelist.viewmodels.UsuarioViewModel
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private var infoDeUsuario: clsUsuario? = null
    private val infoCompletaDeUsuario = MutableLiveData<clsUsuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargarUsuario()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        infoCompletaDeUsuario.observe(this, Observer{ usuario ->
            with(binding){
                tVNombreUsuario.text = usuario.nombreUsuario
                tVVideojuegosDropeadosValue.text = usuario.videojuegosDropeados.toString()
                tVVideojuegosPlaneadosValue.text = usuario.videojuegosPlaneados.toString()
                tVVideojuegosJugandoValue.text = usuario.videojuegosJugando.toString()
                tVVideojuegosJugadosValue.text = usuario.videojuegosJugados.toString()
                tVVideojuegosEnPausaValue.text = usuario.videojuegosEnPausa.toString()
                btnVerLista.setOnClickListener {
                    if(usuario.esListaPrivada){
                        Snackbar.make(requireView(), Mensajes.informacion.LA_LISTA_ES_PRIVADA, Snackbar.LENGTH_SHORT).show()
                    }else{
                        navController.navigate(R.id.miListaFragment)
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.pBIndeterminada.visibility = View.GONE
    }

    private fun cargarUsuario(){
        CoroutineScope(Dispatchers.IO).launch {
            /*
            Diferenciaremos si el usuario quiere ver su propio del perfil de otro usuario mediante el value
            del MutableLiveData del viewModel, si es null es que el usuario quiere ver su propio perfil, si no es
            null, es que quiere ver el perfil de otra persona
             */
            if(usuarioViewModel.usuarioSeleccionado.value == null){
                infoDeUsuario = usuarioViewModel.getUsuario(SharedData.idUsuario)
            }else{
                infoDeUsuario = usuarioViewModel.getUsuario(usuarioViewModel.usuarioSeleccionado.value!!.id)
            }
            infoCompletaDeUsuario.postValue(infoDeUsuario!!)
            activity?.runOnUiThread {
                binding.pBIndeterminada.visibility = View.GONE
            }
        }
    }
}