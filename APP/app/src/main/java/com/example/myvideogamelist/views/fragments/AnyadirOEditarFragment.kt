package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.myvideogamelist.R
import com.example.myvideogamelist.api.repositories.clsListaVideojuegoRepository
import com.example.myvideogamelist.databinding.FragmentAnyadirOEditarBinding
import com.example.myvideogamelist.models.clsEstado
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.models.clsListaVideojuego
import com.example.myvideogamelist.viewmodels.VideojuegoViewModel
import com.example.myvideogamelist.views.adapters.ListaEstadosAdapter
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class AnyadirOEditarFragment : Fragment() {

    private var _binding: FragmentAnyadirOEditarBinding? = null
    private val binding get() = _binding!!
    private val videojuegoViewModel: VideojuegoViewModel by activityViewModels()
    //private lateinit var navController: NavController
    private var listaEstados = mutableListOf<clsEstado>()
    private lateinit var adapter: ListaEstadosAdapter
    private lateinit var oVideojuegoAInsertarOEditar: clsListaVideojuego
    private lateinit var oVideojuegoSeleccionado: clsListaConInfoDeVideojuego
    private var editar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            CoroutineScope(Dispatchers.IO).launch {
                videojuegoViewModel.onCreateEstados()
                activity?.runOnUiThread {
                    listaEstados.addAll(videojuegoViewModel.listaEstados)
                    binding.pBIndeterminada.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                    /*
                    Para entender este if(editar) primero hay que entender como funciona el runOnUiThread:
                    Como bien nos dice la documentación: "If the current thread is not the UI thread, the action is posted to the event queue of the UI thread."
                    Esto significa que si ya hay algo ejecutandose en el hilo de la UI (en nuestro caso, si hay algo ejecutandose, que es el metodo onViewCreated),
                    nuestro activity?.runOnUiThread esperará a que acabe el onViewCreated, y acto seguido, ejecutara el bloque de codigo en el que se encuentra este comentario, es
                    por esta razon por la cual podemos usar el binding, si usasemos el binding en el onCreate normal (sin corrutinas ni nada), nos daria una excepcion ya que todavia
                    no ha "cargado" la UI.
                    Una vez que sabemos como funciona, sabemos que editar estara a true si el idUsuario no es nulo ni vacio, como bien vemos al final del metodo onViewCreated.
                    Tuve que poner esto aqui porque como bien he explicado antes, se ejecuta antes el onViewCreated que este bloque del codigo
                     */
                    if(editar){
                        binding.atVEstados.setText(listaEstados[oVideojuegoSeleccionado.estado-1].toString(), false)
                        oVideojuegoAInsertarOEditar.estado = listaEstados[oVideojuegoSeleccionado.estado-1].id
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                Mensajes.errores.CONEXION_INTERNET_FALLIDA,
                Toast.LENGTH_SHORT
            ).show()
        }
        oVideojuegoSeleccionado = videojuegoViewModel.videojuegoSeleccionado.value!!
        oVideojuegoAInsertarOEditar = clsListaVideojuego(idUsuario = SharedData.idUsuario, idVideojuego = oVideojuegoSeleccionado.idVideojuego)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnyadirOEditarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pBIndeterminada.visibility = View.VISIBLE
        initRecyclerView()
        binding.atVEstados.setOnItemClickListener { _, _, position, _ ->
            oVideojuegoAInsertarOEditar.estado = position+1
        }
        binding.hPickerNota.values = listOf("-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10").toTypedArray()
        binding.hPickerNota.sideItems = 4
        binding.hPickerNota.scrollBarSize = 4
        binding.hPickerNota.setOnItemSelectedListener {
            oVideojuegoAInsertarOEditar.nota = it
        }
        binding.hPickerDificultad.values = listOf("-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10").toTypedArray()
        binding.hPickerDificultad.sideItems = 4
        binding.hPickerDificultad.scrollBarSize = 4
        binding.hPickerDificultad.setOnItemSelectedListener {
            oVideojuegoAInsertarOEditar.dificultad = it
        }
        /*
        Ahora vamos a hacer que si el usuario ya tiene ese videojuego en la lista, y por consecuente, quiere editarlo, se le carguen los valores que tiene en la lista, es decir,
        que si el usuario, le tiene puesto un 7 de nota al juego, aparezca el 7 seleccionado en el carrusel horizontal de notas.
        Sabremos si el usuario tiene el juego en la lista o no, si el objeto de clsListaConInfoDeVideojuego tiene su idUsuario con algun valor
        */
        if(!oVideojuegoSeleccionado.idUsuario.isNullOrEmpty()){
            editar = true
            oVideojuegoAInsertarOEditar.nota = oVideojuegoSeleccionado.notaPersonal
            oVideojuegoAInsertarOEditar.dificultad = oVideojuegoSeleccionado.dificultadPersonal
            binding.hPickerNota.selectedItem = oVideojuegoAInsertarOEditar.nota
            binding.hPickerDificultad.selectedItem = oVideojuegoAInsertarOEditar.dificultad
            //el valor del estado esta arriba, en el onCreate -> activity?.runOnUiThread (hay un comentario explicando el por que esta ahi)
        }

        binding.btnTest.setOnClickListener {
            try{
                if(editar){
                    Toast.makeText(requireContext(),clsListaVideojuegoRepository().editarVideojuegoEnLista(oVideojuegoAInsertarOEditar).toString(), Toast.LENGTH_SHORT).show()
//                    clsListaVideojuegoRepository().editarVideojuegoEnLista(oVideojuegoAInsertarOEditar)
                }else{
                    Toast.makeText(requireContext(),clsListaVideojuegoRepository().insertarVideojuegoEnLista(oVideojuegoAInsertarOEditar).toString(), Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(requireContext(), Mensajes.errores.CONEXION_INTERNET_FALLIDA, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initRecyclerView() {
        adapter = ListaEstadosAdapter(requireContext(), R.layout.list_item_estado, listaEstados)
        binding.atVEstados.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

}