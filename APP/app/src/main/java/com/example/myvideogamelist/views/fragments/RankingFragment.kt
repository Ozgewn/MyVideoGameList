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
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentRankingBinding
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.viewmodels.VideojuegoViewModel
import com.example.myvideogamelist.views.adapters.ListaConInfoDeVideojuegoAdapter
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListaConInfoDeVideojuegoAdapter
    private var listaVideojuegosConInfoFiltrada = mutableListOf<clsListaConInfoDeVideojuego>()
    private val videojuegoViewModel: VideojuegoViewModel by activityViewModels()
    private lateinit var navController: NavController
    private var listaVideojuegosConInfoCompleta: List<clsListaConInfoDeVideojuego> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        Este if lo hacemos para que si se destruye y se vuelve a crear la vista (lo que pasaria al seleccionar el modo oscuro), no salte una excepcion, y ademas,
        no vuelva a hacer una llamada a la API
         */
        if(videojuegoViewModel.listaConInfoDeVideojuegosModel.isNullOrEmpty()){
            cargarRanking()
        }else{
            listaVideojuegosConInfoCompleta = videojuegoViewModel.listaConInfoDeVideojuegosModel
            listaVideojuegosConInfoFiltrada.clear()
            listaVideojuegosConInfoFiltrada.addAll(listaVideojuegosConInfoCompleta)
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
        navController = findNavController()
        binding.pBIndeterminada.visibility = View.VISIBLE
        initRecyclerView()
        binding.swipeRefresh.setOnRefreshListener {
            cargarRanking()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        if(!listaVideojuegosConInfoFiltrada.isNullOrEmpty()){
            binding.pBIndeterminada.visibility = View.GONE
            /*
            esto lo hago porque si nosotros entramos en la pantalla de editar, y luego le damos a atras y entramos en el fragment de ranking (este), la barra de progreso
            se quedara visible dando vueltas sin que desaparezca, esto ocurre porque no entraremos de nuevo al onCreate, el cual se encargaria de hacer que la barra de
            progreso desaparezca. La barra de progreso se quedaria en la pantalla dando vueltas ya que en nuestro layout lo tenemos puesto como que sea visible (que es el
            valor por defecto)
             */
        }
    }

    private fun initRecyclerView() {
        adapter = ListaConInfoDeVideojuegoAdapter(listaVideojuegosConInfoFiltrada, {onVideojuegoSeleccionado(it)}, {onVideojuegoAnyadidoOEditado(it)})
        val manager = GridLayoutManager(requireContext(), 2)
        binding.rVListaConInfoDeVideojuego.layoutManager = manager //LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rVListaConInfoDeVideojuego.adapter = adapter
    }

    private fun onVideojuegoSeleccionado(oVideojuego: clsListaConInfoDeVideojuego) {
        videojuegoViewModel.videojuegoSeleccionado.postValue(oVideojuego)
        navController.navigate(R.id.detallesFragment)
    }

    private fun onVideojuegoAnyadidoOEditado(oVideojuego: clsListaConInfoDeVideojuego){
        videojuegoViewModel.videojuegoSeleccionado.postValue(oVideojuego)
        navController.navigate(R.id.anyadirOEditarFragment)
    }

    private fun cargarRanking() {
        try{
            CoroutineScope(Dispatchers.IO).launch { //iniciamos la corrutina
                videojuegoViewModel.cargarListaConInfoDeVideojuego(SharedData.idUsuario) //el onCreate le asigna el valor a listaConInfoDeVideojuegosModel de lo que devuelva la API
                listaVideojuegosConInfoCompleta = videojuegoViewModel.listaConInfoDeVideojuegosModel //le asignamos valor a la lista completa
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

}