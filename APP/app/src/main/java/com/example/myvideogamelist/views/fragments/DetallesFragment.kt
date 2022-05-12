package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myvideogamelist.R
import com.example.myvideogamelist.api.repositories.clsVideojuegoRepository
import com.example.myvideogamelist.databinding.FragmentDetallesBinding
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.models.clsVideojuego
import com.example.myvideogamelist.viewmodels.VideojuegoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetallesFragment : Fragment() {

    private var _binding: FragmentDetallesBinding? = null
    private val binding get() = _binding!!
    private val videojuegoViewModel: VideojuegoViewModel by activityViewModels()
    private val infoCompletaVideojuego = MutableLiveData<clsVideojuego>()
    private var infoDeVideojuegoEnLista : clsListaConInfoDeVideojuego? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargarInfoVideojuego()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetallesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        infoCompletaVideojuego.observe(this, Observer {
            with(binding){
                pBIndeterminada.visibility = View.GONE
                tVNombreVideojuegoValue.text = it.nombre
                Glide.with(requireContext()).load(infoCompletaVideojuego.value!!.urlImagen).into(binding.iVImagenVideojuego)

                if(infoDeVideojuegoEnLista!!.idUsuario.isNullOrEmpty()){ //si el usuario NO tiene el juego en su lista...
                    btnAnyadirOEditar.setIconResource(R.drawable.ic_add) //ponemos un icono de un +
                }else{ //en cambio, si el usuario SI tiene el juego en su lista...
                    btnAnyadirOEditar.setIconResource(R.drawable.ic_edit) //ponemos un icono de editar
                }
                /*
                Controlamos para que en vez de poner como nota 0, ponemos "-" porque si la nota es 0, es que nadie le ha puesto nota a ese juego, de igual manera con la dificultad
                 */
                if(infoCompletaVideojuego.value!!.notaMedia > 0){
                    tVNotaMediaVideojuegoValue.text = it.notaMedia.toString()
                }else{
                    tVNotaMediaVideojuegoValue.text = "-"
                }
                if(infoCompletaVideojuego.value!!.dificultadMedia > 0){
                    tVDificultadMediaVideojuegoValue.text = it.dificultadMedia.toString()
                }else{
                    tVDificultadMediaVideojuegoValue.text = "-"
                }
                if(infoDeVideojuegoEnLista!!.notaPersonal > 0){
                    tVNotaPersonalVideojuegoValue.text = infoDeVideojuegoEnLista!!.notaPersonal.toString()
                }else{
                    tVNotaPersonalVideojuegoValue.text = "-"
                }
                if(infoDeVideojuegoEnLista!!.dificultadPersonal > 0){
                    tVDificultadPersonalVideojuegoValue.text = infoDeVideojuegoEnLista!!.dificultadPersonal.toString()
                }else{
                    tVDificultadPersonalVideojuegoValue.text = "-"
                }
                btnAnyadirOEditar.setOnClickListener {
                    findNavController().navigate(R.id.anyadirOEditarFragment)
                }
            }
        })
        binding.swipeRefresh.setOnRefreshListener {
            cargarInfoVideojuego()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun cargarInfoVideojuego() {
        infoDeVideojuegoEnLista = videojuegoViewModel.videojuegoSeleccionado.value!!
        CoroutineScope(Dispatchers.IO).launch {
            val respuestaInfoVideojuego =  clsVideojuegoRepository().getVideojuego(infoDeVideojuegoEnLista!!.idVideojuego) //obtenemos info del videojuego a partir de la id del videojuego seleccionado
            infoCompletaVideojuego.postValue(respuestaInfoVideojuego)
        }
    }

}