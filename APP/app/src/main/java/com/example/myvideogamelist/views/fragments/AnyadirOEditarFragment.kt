package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentAnyadirOEditarBinding
import com.example.myvideogamelist.models.clsEstado
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.models.clsListaVideojuego
import com.example.myvideogamelist.viewmodels.VideojuegoViewModel
import com.example.myvideogamelist.views.adapters.ListaEstadosAdapter
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AnyadirOEditarFragment : Fragment()  {

    private var _binding: FragmentAnyadirOEditarBinding? = null
    private val binding get() = _binding!!
    private val videojuegoViewModel: VideojuegoViewModel by activityViewModels()
    //private lateinit var navController: NavController
    private var listaEstados = mutableListOf<clsEstado>()
    private lateinit var adapter: ListaEstadosAdapter
    private lateinit var oVideojuegoAInsertarOEditar: clsListaVideojuego
    private lateinit var oVideojuegoSeleccionado: clsListaConInfoDeVideojuego
    private var isEditable = false
    private var isBorrable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        Este if-else es para optimizar la llamada a la API, ya que solo haremos 1 llamada a la API la 1º vez que intentemos añadir/editar algun videojuego a la lista
         */
        if (videojuegoViewModel.listaEstados.isNullOrEmpty()) {
            cargarListaEstadosYAsignarValores()
        }
        oVideojuegoSeleccionado = videojuegoViewModel.videojuegoSeleccionado.value!!
        oVideojuegoAInsertarOEditar = clsListaVideojuego(
            idUsuario = SharedData.idUsuario,
            idVideojuego = oVideojuegoSeleccionado.idVideojuego
        )
    }

    private fun cargarListaEstadosYAsignarValores() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                videojuegoViewModel.cargarListaEstados()
                asignarValores()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                Mensajes.errores.CONEXION_INTERNET_FALLIDA,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun asignarValores(){
        activity?.runOnUiThread {
            listaEstados.clear()
            listaEstados.addAll(videojuegoViewModel.listaEstados)
            binding.pBIndeterminada.visibility = View.GONE
            adapter.notifyDataSetChanged()
            /*
            Para entender este if(isEditable) primero hay que entender como funciona el runOnUiThread:
            Como bien nos dice la documentación: "If the current thread is not the UI thread, the action is posted to the event queue of the UI thread."
            Esto significa que si ya hay algo ejecutandose en el hilo de la UI (en nuestro caso, si hay algo ejecutandose, que es el metodo onViewCreated),
            nuestro activity?.runOnUiThread esperará a que acabe el onViewCreated, y acto seguido, ejecutara el bloque de codigo en el que se encuentra este comentario, es
            por esta razon por la cual podemos usar el binding, si usasemos el binding en el onCreate normal (sin corrutinas ni nada), nos daria una excepcion ya que todavia
            no ha "cargado" la UI.
            Una vez que sabemos como funciona, sabemos que editar estara a true si el idUsuario no es nulo ni vacio, como bien vemos al final del metodo onViewCreated.
            Tuve que poner esto aqui porque como bien he explicado antes, se ejecuta antes el onViewCreated que este bloque del codigo
             */
            if(isEditable){
                binding.atVEstados.setText(listaEstados[oVideojuegoSeleccionado.estado-1].toString(), false)
                oVideojuegoAInsertarOEditar.estado = listaEstados[oVideojuegoSeleccionado.estado-1].id
                binding.eTFechaComienzo.setText(oVideojuegoSeleccionado.fechaDeComienzo)
                oVideojuegoAInsertarOEditar.fechaDeComienzo = oVideojuegoSeleccionado.fechaDeComienzo
                binding.eTFechaFinalizacion.setText(oVideojuegoSeleccionado.fechaDeFinalizacion)
                oVideojuegoAInsertarOEditar.fechaDeFinalizacion = oVideojuegoSeleccionado.fechaDeFinalizacion
                mostrarUOcultarFechasSegunIdEstado()
            }
        }
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
            mostrarUOcultarFechasSegunIdEstado()
        }
        /*
        TODO: mejorar esto
         */
        binding.hPickerNota.values = listOf("-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10").toTypedArray()
        binding.hPickerNota.sideItems = 2
        binding.hPickerNota.scrollBarSize = 10
        binding.hPickerNota.setOnItemSelectedListener {
            oVideojuegoAInsertarOEditar.nota = it
        }
        binding.hPickerDificultad.values = listOf("-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10").toTypedArray()
        binding.hPickerDificultad.sideItems = 2
        binding.hPickerDificultad.scrollBarSize = 10
        binding.hPickerDificultad.setOnItemSelectedListener {
            oVideojuegoAInsertarOEditar.dificultad = it
        }
        binding.btnFechaComienzoHoy.setOnClickListener {
            val fechaActual = LocalDate.now().toString()
            binding.eTFechaComienzo.setText(fechaActual)
            oVideojuegoAInsertarOEditar.fechaDeComienzo = fechaActual
        }
        binding.eTFechaComienzo.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Seleccione fecha de comienzo")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(childFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                val fechaComienzo = Instant.ofEpochMilli(datePicker.selection!!).atZone(ZoneId.systemDefault()).toLocalDate()
                val fechaFormateada = fechaComienzo.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                binding.eTFechaComienzo.setText(fechaFormateada.toString())
                oVideojuegoAInsertarOEditar.fechaDeComienzo = fechaComienzo.toString()
            }
            datePicker.addOnNegativeButtonClickListener {
                oVideojuegoAInsertarOEditar.fechaDeComienzo = null
                binding.eTFechaComienzo.setText("")
            }
        }
        binding.eTFechaFinalizacion.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Seleccione fecha de finalizacion")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(childFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                val fechaComienzo = Instant.ofEpochMilli(datePicker.selection!!).atZone(ZoneId.systemDefault()).toLocalDate()
                val fechaFormateada = fechaComienzo.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                binding.eTFechaFinalizacion.setText(fechaFormateada.toString())
                oVideojuegoAInsertarOEditar.fechaDeFinalizacion = fechaComienzo.toString()
            }
            datePicker.addOnNegativeButtonClickListener {
                oVideojuegoAInsertarOEditar.fechaDeFinalizacion = null
                binding.eTFechaFinalizacion.setText("")
            }
        }
        /*
        Ahora vamos a hacer que si el usuario ya tiene ese videojuego en la lista, y por consecuente, quiere editarlo, se le carguen los valores que tiene en la lista, es decir,
        que si el usuario, le tiene puesto un 7 de nota al juego, aparezca el 7 seleccionado en el carrusel horizontal de notas.
        Sabremos si el usuario tiene el juego en la lista o no, si el objeto de clsListaConInfoDeVideojuego tiene su idUsuario con algun valor
        */
        if(!oVideojuegoSeleccionado.idUsuario.isNullOrEmpty()){
            isEditable = true
            isBorrable = true
            oVideojuegoAInsertarOEditar.nota = oVideojuegoSeleccionado.notaPersonal
            oVideojuegoAInsertarOEditar.dificultad = oVideojuegoSeleccionado.dificultadPersonal
            binding.hPickerNota.selectedItem = oVideojuegoAInsertarOEditar.nota
            binding.hPickerDificultad.selectedItem = oVideojuegoAInsertarOEditar.dificultad
            //el valor del estado esta arriba, en el onCreate -> activity?.runOnUiThread (hay un comentario explicando el por que esta ahi)
        }

        binding.btnAnyadirOEditar.setOnClickListener {
            if(binding.atVEstados.text.isNullOrEmpty()){
                binding.tFMenuDesplegable.error = "Elija un estado, por favor"
            }else{
                binding.tFMenuDesplegable.error = null
                try{
                    if(isEditable){
                        var filasEditadas = 0
                        CoroutineScope(Dispatchers.IO).launch{
                            Log.d("_INFO", oVideojuegoAInsertarOEditar.toString())
                            filasEditadas = videojuegoViewModel.editarVideojuegoEnLista(oVideojuegoAInsertarOEditar)
                            /*
                            Tengo que hacer esto aqui, porque si no, siempre llegara la variable de filasEditadas a 0 (porque se hace antes el Snackbar que la asignacion del valor de la variable con la API)
                             */
                            if(filasEditadas == 4){
                                Snackbar.make(requireView(), "Se ha modificado "+oVideojuegoSeleccionado.nombreVideojuego+" satisfactoriamente", Snackbar.LENGTH_SHORT).show()
                            }else{
                                Snackbar.make(requireView(), "Ha ocurrido un problema mientras se intentaba editar "+oVideojuegoSeleccionado.nombreVideojuego+", intentelo de nuevo mas tarde", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        var filasInsertadas = 0
                        CoroutineScope(Dispatchers.IO).launch{
                            filasInsertadas = videojuegoViewModel.insertarVideojuegoEnLista(oVideojuegoAInsertarOEditar)
                            /*
                            Tengo que hacer esto aqui, porque si no, siempre llegara la variable de filasInsertadas a 0 (porque se hace antes el Snackbar que la asignacion del valor de la variable con la API)
                             */
                            if(filasInsertadas == 4){
                                Snackbar.make(requireView(), "Se ha añadido "+oVideojuegoSeleccionado.nombreVideojuego+" a la lista satisfactoriamente", Snackbar.LENGTH_SHORT).show()
                                //este editar = true para que si el usuario le da 2 veces al boton, la 1º vez inserte y la 2º inserte, evitando asi, un error
                                isEditable = true
                                isBorrable = true
                            }else{
                                Snackbar.make(requireView(), "Ha ocurrido un problema mientras se intentaba añadir "+oVideojuegoSeleccionado.nombreVideojuego+" a la lista, intentelo de nuevo mas tarde", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                    /*
                    Esto de aqui es para cambiar el valor del videojuegoSeleccionado del viewmodel, ya que si el usuario va a detalles de un videojuego, y luego le a añadir o editar,
                    cuando acabe de añadir o editar, y le de hacia atras, los cambios no se veran reflejados por mucho que recargue, con esto que hacemos aqui abajo, actualizamos la informacion
                    del videojuego seleccionado en el viewmodel, que es lo que se usa en el fragment de detalles para visualizar la informacion
                     */
                    with(videojuegoViewModel.videojuegoSeleccionado.value!!){
                        notaPersonal = oVideojuegoAInsertarOEditar.nota
                        dificultadPersonal = oVideojuegoAInsertarOEditar.dificultad
                        idUsuario = oVideojuegoAInsertarOEditar.idUsuario
                        estado = oVideojuegoAInsertarOEditar.estado
                    }
                }catch (e: Exception){
                    Snackbar.make(requireView(), Mensajes.errores.CONEXION_INTERNET_FALLIDA, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnBorrar.setOnClickListener {
            if(isBorrable) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("¿Desea borrar ${oVideojuegoSeleccionado.nombreVideojuego} de su lista?")
                    .setMessage("¿Está seguro de que desea borrar ${oVideojuegoSeleccionado.nombreVideojuego} de su lista de videojuegos? Lo podrá volver a añadir" +
                            "más tarde")
                    .setNeutralButton("Cancelar") { dialog, which ->
                        // nada
                    }
                    .setPositiveButton("Borrar") { dialog, which ->
                        try{
                            CoroutineScope(Dispatchers.IO).launch {
                                val filasEliminadas = videojuegoViewModel.eliminarVideojuegoEnLista(SharedData.idUsuario, oVideojuegoAInsertarOEditar.idVideojuego)

                                if(filasEliminadas == 4){
                                    isBorrable = false
                                    isEditable = false
                                    activity?.runOnUiThread {
                                        binding.atVEstados.text = null
                                    }
                                    Snackbar.make(requireView(), "Se ha eliminado "+oVideojuegoSeleccionado.nombreVideojuego+" de la lista", Snackbar.LENGTH_SHORT).show()
                                }else{
                                    Snackbar.make(requireView(), "Ha ocurrido un problema mientras se intentaba eliminar "+oVideojuegoSeleccionado.nombreVideojuego+" de la lista, intentelo de nuevo mas tarde", Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }catch (e: Exception){
                            Snackbar.make(requireView(), Mensajes.errores.CONEXION_INTERNET_FALLIDA, Snackbar.LENGTH_SHORT).show()
                        }
                        with(videojuegoViewModel.videojuegoSeleccionado.value!!){
                            notaPersonal = 0
                            dificultadPersonal = 0
                            idUsuario = null
                            estado = 0
                        }
                    }
                    .show()
            }else{
                Snackbar.make(requireView(), Mensajes.errores.BORRAR_VIDEOJUEGO_QUE_NO_ESTA_EN_LISTA, Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    fun mostrarUOcultarFechasSegunIdEstado(){
        if(oVideojuegoAInsertarOEditar.estado > 0){
            binding.eTFechaComienzo.visibility = View.VISIBLE
            binding.btnFechaComienzoHoy.visibility = View.VISIBLE
        }
        if((listaEstados.find { it.nombreEstado == "Jugado" }!!.id == oVideojuegoAInsertarOEditar.estado) || listaEstados.find { it.nombreEstado == "Dropeado" }!!.id == oVideojuegoAInsertarOEditar.estado){
            binding.eTFechaFinalizacion.visibility = View.VISIBLE
            binding.btnFechaFinalizacionHoy.visibility = View.VISIBLE
        }else{
            binding.eTFechaFinalizacion.visibility = View.GONE
            binding.btnFechaFinalizacionHoy.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if(!listaEstados.isNullOrEmpty()){
            binding.pBIndeterminada.visibility = View.GONE
        }
        if(!videojuegoViewModel.listaEstados.isNullOrEmpty()){
            asignarValores()
        }
    }

    private fun initRecyclerView() {
        adapter = ListaEstadosAdapter(requireContext(), R.layout.list_item_estado, listaEstados)
        binding.atVEstados.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

}