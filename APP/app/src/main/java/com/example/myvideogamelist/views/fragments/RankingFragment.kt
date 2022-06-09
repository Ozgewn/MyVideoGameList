package com.example.myvideogamelist.views.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentRankingBinding
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.utils.InterfazUsuarioUtils
import com.example.myvideogamelist.utils.MaterialAlertDialogHelper
import com.example.myvideogamelist.utils.SnackbarHelper
import com.example.myvideogamelist.viewmodels.VideojuegoViewModel
import com.example.myvideogamelist.views.adapters.ListaConInfoDeVideojuegoAdapter
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception
import java.net.UnknownHostException

class RankingFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListaConInfoDeVideojuegoAdapter
    private var listaVideojuegosConInfoFiltrada = mutableListOf<clsListaConInfoDeVideojuego>()
    private val videojuegoViewModel: VideojuegoViewModel by activityViewModels()
    private lateinit var navController: NavController
    private var listaVideojuegosConInfoCompleta: List<clsListaConInfoDeVideojuego> = emptyList()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        /*
        Este if lo hacemos para que si se destruye y se vuelve a crear la vista (lo que pasaria al seleccionar el modo oscuro), no salte una excepcion, y ademas,
        no vuelva a hacer una llamada a la API, ademas hago el != SharedData.idUsuario, porque si el usuario cierra sesion, e inicia sesion con otra cuenta, tendra en el viewmodel
        la lista de la cuenta que ha cerrado sesion, por lo cual, habra que cargar la del nuevo usuario
         */
        if(videojuegoViewModel.listaConInfoDeVideojuegosModel.isNullOrEmpty() || videojuegoViewModel.listaConInfoDeVideojuegosModel[0].idUsuario != SharedData.idUsuario){
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
        binding.sVRanking.setOnQueryTextListener(this)
        //Ordenar
        binding.btnOrdenar.setOnClickListener {
            elegirOpcionOrdenadoYOrdenar()
        }
    }

    private fun elegirOpcionOrdenadoYOrdenar() {
        val opcionesDeOrdenado = arrayOf("Estado", "Nombre A-Z", "Nombre Z-A", "Nota media", "Dificultad media")
        var opcionOrdenado = 0

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Elija modo de ordenado")
            .setNeutralButton("cancelar") { dialog, which ->
                // nada
            }
            .setPositiveButton("Ok") { dialog, which ->
                ordenar(opcionOrdenado)
            }
            .setSingleChoiceItems(opcionesDeOrdenado, opcionOrdenado) { dialog, which ->
                opcionOrdenado = which
            }
            .show()
    }

    private fun ordenar(opcionOrdenado: Int){
        listaVideojuegosConInfoFiltrada.clear()
        when(opcionOrdenado){
            0 -> { //Estado
                listaVideojuegosConInfoFiltrada.addAll(listaVideojuegosConInfoCompleta)
            }
            1 -> { //Nombre A-Z
                listaVideojuegosConInfoFiltrada.addAll(listaVideojuegosConInfoCompleta.sortedBy {
                    it.nombreVideojuego
                })
            }
            2 -> { //Nombre Z-A
                listaVideojuegosConInfoFiltrada.addAll(listaVideojuegosConInfoCompleta.sortedByDescending {
                    it.nombreVideojuego
                })
            }
            3 -> { //Nota media
                listaVideojuegosConInfoFiltrada.addAll(listaVideojuegosConInfoCompleta.sortedByDescending {
                    it.notaMediaVideojuego
                })
            }
            4 -> { //Dificultad media
                listaVideojuegosConInfoFiltrada.addAll(listaVideojuegosConInfoCompleta.sortedByDescending {
                    it.dificultadMediaVideojuego
                })
            }
        }
        adapter.notifyDataSetChanged()
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
        if(auth.currentUser!!.isAnonymous){
            MaterialAlertDialogHelper.errorPorSerAnonimo(this, auth)
        }else{
            videojuegoViewModel.videojuegoSeleccionado.postValue(oVideojuego)
            navController.navigate(R.id.anyadirOEditarFragment)
        }
    }

    private fun cargarRanking() {
        CoroutineScope(Dispatchers.IO).launch { //iniciamos la corrutina
            try{
                videojuegoViewModel.cargarListaConInfoDeVideojuego(SharedData.idUsuario) //el onCreate le asigna el valor a listaConInfoDeVideojuegosModel de lo que devuelva la API
            }catch (e: UnknownHostException){
                activity?.runOnUiThread {
                    SnackbarHelper.errorNoInternet(this@RankingFragment)
                }
            }
            listaVideojuegosConInfoCompleta = videojuegoViewModel.listaConInfoDeVideojuegosModel //le asignamos valor a la lista completa
            activity?.runOnUiThread{
                listaVideojuegosConInfoFiltrada.removeAll(listaVideojuegosConInfoFiltrada)
                listaVideojuegosConInfoFiltrada.addAll(listaVideojuegosConInfoCompleta) //añadimos lo de la lista completa a la lista que vamos a ofrecer
                binding.pBIndeterminada.visibility = View.GONE //quitamos el progress bar(el circulo que indica que esta cargando)
                adapter.notifyDataSetChanged() //avisamos el adapter que hemos modificado la lista
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        InterfazUsuarioUtils.hideKeyboard(binding.root, this)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        listaVideojuegosConInfoFiltrada.clear()
        if(!newText.isNullOrEmpty()){
            listaVideojuegosConInfoFiltrada.addAll(listaVideojuegosConInfoCompleta.filter {
                it.nombreVideojuego.lowercase().contains(newText.lowercase())
            })
        }else{
            listaVideojuegosConInfoFiltrada.addAll(listaVideojuegosConInfoCompleta)
        }
        adapter.notifyDataSetChanged()
        return true
    }

}