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
import com.example.myvideogamelist.databinding.FragmentMiListaBinding
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.viewmodels.UsuarioViewModel
import com.example.myvideogamelist.viewmodels.VideojuegoViewModel
import com.example.myvideogamelist.views.adapters.MiListaAdapter
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MiListaFragment : Fragment() {

    private var _binding: FragmentMiListaBinding? = null
    private val binding get() = _binding!!
    private val videojuegoViewModel: VideojuegoViewModel by activityViewModels()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private lateinit var adapter: MiListaAdapter
    private val listaVideojuegosEnListaFiltrado = mutableListOf<clsListaConInfoDeVideojuego>()
    private var listaVideojuegosEnListaCompleta: List<clsListaConInfoDeVideojuego> = emptyList()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        Metodo para cargar la lista llamado en el onViewCreated, en el cual explico el por que lo llamo ahi.
        Y no lo llamo en el onCreate porque si no, al entrar al fragment llamariamos 2 veces al metodo de cargar
        la lista, una vez en el onCreate y otra en el onViewCreated
         */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMiListaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.pBIndeterminada.visibility = View.VISIBLE
        /*
        Para entender por que cargo de nuevo la lista, vamos a poner una situacion de ejemplo:
        Estoy en mi lista, voy a editar un juego, y lo borro de mi lista, y le doy a la flecha hacia atras para
        volver a mi lista, si no vuelvo a cargar la lista, el juego que he borrado "seguira" en la lista, lo cual
        provocaria una excepcion, por lo cual, volvemos a cargar la lista
         */
        cargarVideojuegosEnLista()
        initRecyclerView()
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab!!.text.toString()=="Todos"){
                    listaVideojuegosEnListaFiltrado.clear()
                    listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnListaCompleta)
                    adapter.notifyDataSetChanged()
                }else{
                    listaVideojuegosEnListaFiltrado.clear()
                    listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnListaCompleta.filter {
                        videojuegoViewModel.listaEstados[it.estado-1].nombreEstado.lowercase() == tab.text.toString().lowercase()
                    })
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //nada
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //nada
            }

        })
    }

    override fun onResume() {
        super.onResume()
        if(!listaVideojuegosEnListaFiltrado.isNullOrEmpty()){
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
        adapter = MiListaAdapter(listaVideojuegosEnListaFiltrado, {onVideojuegoSeleccionado(it)}, {getNombreEstado(it)}, {getEstadoDelUsuarioObservador(it)}, {onVideojuegoAnyadidoOEditado(it)})
        binding.rVSoloVideojuegosEnLista.layoutManager = LinearLayoutManager(requireContext())
        binding.rVSoloVideojuegosEnLista.adapter = adapter
    }

    private fun onVideojuegoSeleccionado(oVideojuegoConInfo: clsListaConInfoDeVideojuego){
        /*
        Si el usuario desde el que estamos dando al boton anyadir no es el nuestro, si no desde la lista de otro usuario, debemos hacer un postValue
        del videojuego en cuestion en nuestra lista, y no un postValue del viedojuego en la lista del usuario (que es lo que ocurriria sin este if)
         */
        if(usuarioViewModel.usuarioSeleccionado.value != null){
            videojuegoViewModel.videojuegoSeleccionado.postValue(videojuegoViewModel.listaConInfoDeVideojuegosModel.find {
                it.idVideojuego == oVideojuegoConInfo.idVideojuego
            })
        }else{//en caso de que si sea nuestro propio usuario desde donde hacemos click a anyadir o editar
            videojuegoViewModel.videojuegoSeleccionado.postValue(oVideojuegoConInfo)
        }
        navController.navigate(R.id.detallesFragment)
    }

    private fun getNombreEstado(oVideojuegoConInfo: clsListaConInfoDeVideojuego): String {
        var nombreEstado = ""
        if(!oVideojuegoConInfo.idUsuario.isNullOrEmpty()){
            nombreEstado = videojuegoViewModel.listaEstados[oVideojuegoConInfo.estado-1].nombreEstado
        }
        return nombreEstado
    }

    /**
     * Este metodo devuelve el estado del usuario que esta observando la lista.
     * Pongamos un ejemplo:
     * Supongamos que el usuario A esta viendo la lista del usuario B. El usuario B tiene en su lista el videojuego
     * "The Witcher 3", pero el usuario A no tiene ese juego en su lista.
     * Entonces, si el usuario A esta viendo la lista del usuario B, le deberia salir un "+" en el videojuego "The
     * Witcher 3".
     * Basicamente seria algo como esto:
     * idDelEstadoDelUsuarioObservador: 0
     * idDelUsuarioDueñoDeLaLista: 3 (o 4, o el que sea).
     * Pero esa es la idea de este metodo, traer el id del estado del usuario que esta observando la lista de otro usuario
     */
    private fun getEstadoDelUsuarioObservador(oVideojuegoConInfo: clsListaConInfoDeVideojuego): Int{
        val idEstadoObservador = videojuegoViewModel.listaConInfoDeVideojuegosModel.find {
            it.idVideojuego == oVideojuegoConInfo.idVideojuego
        }!!.estado
        return idEstadoObservador
    }

    private fun onVideojuegoAnyadidoOEditado(oVideojuego: clsListaConInfoDeVideojuego){
        /*
        Si el usuario desde el que estamos dando al boton anyadir no es el nuestro, si no desde la lista de otro usuario, debemos hacer un postValue
        del videojuego en cuestion en nuestra lista, y no un postValue del viedojuego en la lista del usuario (que es lo que ocurriria sin este if)
         */
        if(usuarioViewModel.usuarioSeleccionado.value != null){
            videojuegoViewModel.videojuegoSeleccionado.postValue(videojuegoViewModel.listaConInfoDeVideojuegosModel.find {
                it.idVideojuego == oVideojuego.idVideojuego
            })
        }else{//en caso de que si sea nuestro propio usuario desde donde hacemos click a anyadir o editar
            videojuegoViewModel.videojuegoSeleccionado.postValue(oVideojuego)
        }
        navController.navigate(R.id.anyadirOEditarFragment)
    }

    private fun cargarVideojuegosEnLista(){
        try{
            CoroutineScope(Dispatchers.IO).launch { //iniciamos la corrutina
                /*
                Diferenciaremos si el usuario quiere ver su propia lista de la lista de otro usuario mediante
                el value del MutableLiveData del usuarioViewModel, si es null, significa que el usuario quiere ver
                su propia lista, si no es null, es que el usuario quiere ver la lista de otro usuario
                 */
                if(usuarioViewModel.usuarioSeleccionado.value == null){
                    videojuegoViewModel.cargarSoloVideojuegosEnListaDelUsuario(SharedData.idUsuario) //el onCreate le asigna el valor a listaConInfoDeVideojuegosModel de lo que devuelva la API
                }else{
                    videojuegoViewModel.cargarSoloVideojuegosEnListaDelUsuario(usuarioViewModel.usuarioSeleccionado.value!!.id)
                }
                listaVideojuegosEnListaCompleta = videojuegoViewModel.listaDeVideojuegosSoloEnLista //le asignamos valor a la lista completa
                if(videojuegoViewModel.listaEstados.isNullOrEmpty()){
                    //aunque lo comprobemos ya en el viewmodel, hacemos doble comprobacion por si acaso, y asi no hacemos la llamada para "nada"
                    videojuegoViewModel.cargarListaEstados()
                }
                activity?.runOnUiThread{
                    listaVideojuegosEnListaFiltrado.removeAll(listaVideojuegosEnListaFiltrado)
                    listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnListaCompleta)//añadimos lo de la lista completa a la lista que vamos a ofrecer
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