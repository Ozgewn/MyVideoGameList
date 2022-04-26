package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentRankingBinding
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.viewmodels.clsListaConInfoDeVideojuegoViewModel
import com.example.myvideogamelist.views.adapters.ListaConInfoDeVideojuegoAdapter
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListaConInfoDeVideojuegoAdapter
    private var listaVideojuegosConInfoFiltrada = mutableListOf<clsListaConInfoDeVideojuego>()
    private val listaVideojuegosConInfoViewModel: clsListaConInfoDeVideojuegoViewModel by activityViewModels()
    private lateinit var navController: NavController
    private var listaVideojuegosConInfoCompleta: List<clsListaConInfoDeVideojuego> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            CoroutineScope(Dispatchers.IO).launch { //iniciamos la corrutina
                listaVideojuegosConInfoViewModel.onCreate(SharedData.idUsuario) //el onCreate le asigna el valor a listaConInfoDeVideojuegosModel de lo que devuelva la API
                listaVideojuegosConInfoCompleta = listaVideojuegosConInfoViewModel.listaConInfoDeVideojuegosModel //le asignamos valor a la lista completa
                activity?.runOnUiThread{
                    listaVideojuegosConInfoFiltrada.removeAll(listaVideojuegosConInfoFiltrada)
                    listaVideojuegosConInfoFiltrada.addAll(listaVideojuegosConInfoCompleta) //añadimos lo de la lista completa a la lista que vamos a ofrecer
                    binding.pBIndeterminada.visibility = View.GONE //quitamos el progress bar(el circulo que indica que esta cargando)
                    adapter.notifyDataSetChanged() //avisamos el adapter que hemos modificado la lista
                }
            }
        }catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                Mensajes.errores.CONEXION_INTERNET_FALLIDA,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pBIndeterminada.visibility = View.VISIBLE
        initRecyclerView()
    }

    fun initRecyclerView() {
        adapter = ListaConInfoDeVideojuegoAdapter(listaVideojuegosConInfoFiltrada) {
            onVideojuegoSeleccionado(it)
        }
        var manager: GridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rVListaConInfoDeVideojuego.layoutManager = manager //LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rVListaConInfoDeVideojuego.adapter = adapter
    }

    fun onVideojuegoSeleccionado(oVideojuego: clsListaConInfoDeVideojuego) {
        //TODO: NAVEGAR A FRAGMENT DE DETALLES
        Toast.makeText(requireContext(), oVideojuego.nombreVideojuego, Toast.LENGTH_SHORT).show()
    }

}