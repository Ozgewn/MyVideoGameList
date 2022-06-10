package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentAnyadirOEditarBinding
import com.example.myvideogamelist.models.clsEstado
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.models.clsListaVideojuego
import com.example.myvideogamelist.utils.SnackbarHelper
import com.example.myvideogamelist.viewmodels.VideojuegoViewModel
import com.example.myvideogamelist.views.adapters.ListaEstadosAdapter
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import com.example.myvideogamelist.views.textos.Textos
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.wefika.horizontalpicker.HorizontalPicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AnyadirOEditarFragment : Fragment()  {

    private var _binding: FragmentAnyadirOEditarBinding? = null
    private val binding get() = _binding!!
    private val videojuegoViewModel: VideojuegoViewModel by activityViewModels()
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
                try{
                    videojuegoViewModel.cargarListaEstados()
                }catch (e: UnknownHostException){
                    activity?.runOnUiThread {
                        SnackbarHelper.errorNoInternet(this@AnyadirOEditarFragment)
                    }
                }
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
            actualizarListaEstados()
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
                if(!oVideojuegoSeleccionado.fechaDeComienzo.isNullOrEmpty()){
                    formatearFecha(oVideojuegoSeleccionado.fechaDeComienzo!!, binding.eTFechaComienzo)
                    oVideojuegoAInsertarOEditar.fechaDeComienzo = oVideojuegoSeleccionado.fechaDeComienzo
                }
                if(!oVideojuegoSeleccionado.fechaDeFinalizacion.isNullOrEmpty()){
                    formatearFecha(oVideojuegoSeleccionado.fechaDeFinalizacion!!, binding.eTFechaFinalizacion)
                    oVideojuegoAInsertarOEditar.fechaDeFinalizacion = oVideojuegoSeleccionado.fechaDeFinalizacion
                }
                mostrarUOcultarFechasSegunIdEstado()
            }
        }
    }

    private fun actualizarListaEstados(){
        listaEstados.clear()
        listaEstados.addAll(videojuegoViewModel.listaEstados)
        binding.pBIndeterminada.visibility = View.GONE
        adapter.notifyDataSetChanged()
    }

    private fun formatearFecha(fecha: String, eTFecha: EditText) {
        eTFecha.setText(
            LocalDate.parse(fecha)
                .format(DateTimeFormatter.ofPattern(Textos.FORMATO_FECHA_CON_GUIONES))
        )
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
        formatearHorizontalPicker(binding.hPickerNota)
        binding.hPickerNota.setOnItemSelectedListener {
            oVideojuegoAInsertarOEditar.nota = it
        }
        formatearHorizontalPicker(binding.hPickerDificultad)
        binding.hPickerDificultad.setOnItemSelectedListener {
            oVideojuegoAInsertarOEditar.dificultad = it
        }
        binding.btnFechaComienzoHoy.setOnClickListener {
            val fechaActual = LocalDate.now().toString()
            formatearFecha(fechaActual, binding.eTFechaComienzo)
            oVideojuegoAInsertarOEditar.fechaDeComienzo = fechaActual
        }
        binding.btnFechaFinalizacionHoy.setOnClickListener {
            val fechaActual = LocalDate.now().toString()
            formatearFecha(fechaActual, binding.eTFechaFinalizacion)
            oVideojuegoAInsertarOEditar.fechaDeFinalizacion = fechaActual
        }
        binding.eTFechaComienzo.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(Textos.SELECCIONE_FECHA_COMIENZO)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            mostrarFechaSeleccionada(datePicker, binding.eTFechaComienzo)
            datePicker.addOnNegativeButtonClickListener {
                oVideojuegoAInsertarOEditar.fechaDeComienzo = null
                binding.eTFechaComienzo.setText("")
            }

        }
        binding.eTFechaFinalizacion.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(Textos.SELECCIONE_FECHA_FINALIZACION)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            mostrarFechaSeleccionada(datePicker, binding.eTFechaFinalizacion)
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
            asignarNotaYDificultadPrevias()
            //el valor del estado esta arriba, en el onCreate -> activity?.runOnUiThread (hay un comentario explicando el por que esta ahi)
        }

        binding.btnAnyadirOEditar.setOnClickListener {
            if(binding.atVEstados.text.isNullOrEmpty()){
                binding.tFMenuDesplegable.error = Mensajes.errores.ELIGE_UN_ESTADO
            }else{
                binding.tFMenuDesplegable.error = null
                try{
                    if(isEditable){
                        editarJuego()
                    }else{
                        insertarJuego()
                    }
                    /*
                    Esto de aqui es para cambiar el valor del videojuegoSeleccionado del viewmodel, ya que si el usuario va a detalles de un videojuego, y luego le a añadir o editar,
                    cuando acabe de añadir o editar, y le de hacia atras, los cambios no se veran reflejados por mucho que recargue, con esto que hacemos aqui abajo, actualizamos la informacion
                    del videojuego seleccionado en el viewmodel, que es lo que se usa en el fragment de detalles para visualizar la informacion
                     */
                    actualizarInfoJuegoEnViewModel()
                }catch (e: Exception){
                    Snackbar.make(requireView(), Mensajes.errores.CONEXION_INTERNET_FALLIDA, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnBorrar.setOnClickListener {
            if(isBorrable) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(String.format(Textos.TITULO_CONFIRMAR_BORRAR_VIDEOJUEGO, oVideojuegoSeleccionado.nombreVideojuego))
                    .setMessage(String.format(Textos.MENSAJE_CONFIRMAR_BORRAR_VIDEOJUEGO, oVideojuegoSeleccionado.nombreVideojuego))
                    .setNeutralButton(Textos.BOTON_CANCELAR) { dialog, which ->
                        // nada
                    }
                    .setPositiveButton(Textos.BOTON_BORRAR) { dialog, which ->
                        try{
                            borrarJuego()
                        }catch (e: Exception){
                            Snackbar.make(requireView(), Mensajes.errores.CONEXION_INTERNET_FALLIDA, Snackbar.LENGTH_SHORT).show()
                        }
                        borrarInfoJuegoEnViewModel()
                    }
                    .show()
            }else{
                Snackbar.make(requireView(), Mensajes.errores.BORRAR_VIDEOJUEGO_QUE_NO_ESTA_EN_LISTA, Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    private fun borrarInfoJuegoEnViewModel() {
        with(videojuegoViewModel.videojuegoSeleccionado.value!!){
            notaPersonal = 0
            dificultadPersonal = 0
            idUsuario = null
            estado = 0
            fechaDeComienzo = null
            fechaDeFinalizacion = null
        }
    }

    private fun borrarJuego() {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val filasEliminadas = videojuegoViewModel.eliminarVideojuegoEnLista(SharedData.idUsuario, oVideojuegoAInsertarOEditar.idVideojuego)
                if(filasEliminadas == 4){
                    isBorrable = false
                    isEditable = false
                    activity?.runOnUiThread {
                        binding.atVEstados.text = null
                    }
                    Snackbar.make(requireView(), String.format(Mensajes.informacion.VIDEOJUEGO_BORRADO, oVideojuegoSeleccionado.nombreVideojuego), Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(requireView(), String.format(Mensajes.errores.VIDEOJUEGO_NO_BORRADO, oVideojuegoSeleccionado.nombreVideojuego), Snackbar.LENGTH_SHORT).show()
                }
            }catch (e: UnknownHostException){
                activity?.runOnUiThread {
                    SnackbarHelper.errorNoInternet(this@AnyadirOEditarFragment)
                }
            }
        }
    }

    private fun actualizarInfoJuegoEnViewModel() {
        with(videojuegoViewModel.videojuegoSeleccionado.value!!){
            notaPersonal = oVideojuegoAInsertarOEditar.nota
            dificultadPersonal = oVideojuegoAInsertarOEditar.dificultad
            idUsuario = oVideojuegoAInsertarOEditar.idUsuario
            estado = oVideojuegoAInsertarOEditar.estado
            fechaDeComienzo = oVideojuegoAInsertarOEditar.fechaDeComienzo
            fechaDeFinalizacion = oVideojuegoAInsertarOEditar.fechaDeFinalizacion
        }
    }

    private fun insertarJuego() {
        var filasInsertadas = 0
        CoroutineScope(Dispatchers.IO).launch{
            try{
                filasInsertadas = videojuegoViewModel.insertarVideojuegoEnLista(oVideojuegoAInsertarOEditar)
                /*
                Tengo que hacer esto aqui, porque si no, siempre llegara la variable de filasInsertadas a 0 (porque se hace antes el Snackbar que la asignacion del valor de la variable con la API)
                 */
                if(filasInsertadas == 4){
                    Snackbar.make(requireView(), String.format(Mensajes.informacion.VIDEOJUEGO_ANYADIDO, oVideojuegoSeleccionado.nombreVideojuego), Snackbar.LENGTH_SHORT).show()
                    //este editar = true para que si el usuario le da 2 veces al boton, la 1º vez inserte y la 2º inserte, evitando asi, un error
                    isEditable = true
                    isBorrable = true
                }else{
                    Snackbar.make(requireView(), String.format(Mensajes.errores.VIDEOJUEGO_NO_ANYADIDO, oVideojuegoSeleccionado.nombreVideojuego), Snackbar.LENGTH_SHORT).show()
                }
            }catch (e: UnknownHostException){
                activity?.runOnUiThread {
                    SnackbarHelper.errorNoInternet(this@AnyadirOEditarFragment)
                }
            }
        }
    }

    private fun editarJuego() {
        var filasEditadas = 0
        CoroutineScope(Dispatchers.IO).launch{
            try{
                filasEditadas = videojuegoViewModel.editarVideojuegoEnLista(oVideojuegoAInsertarOEditar)
                /*
                Tengo que hacer esto aqui, porque si no, siempre llegara la variable de filasEditadas a 0 (porque se hace antes el Snackbar que la asignacion del valor de la variable con la API)
                 */
                if(filasEditadas == 4){
                    Snackbar.make(requireView(), String.format(Mensajes.informacion.VIDEOJUEGO_EDITADO, oVideojuegoSeleccionado.nombreVideojuego), Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(requireView(), String.format(Mensajes.errores.VIDEOJUEGO_NO_EDITADO, oVideojuegoSeleccionado.nombreVideojuego), Snackbar.LENGTH_SHORT).show()
                }
            }catch (e: UnknownHostException){
                activity?.runOnUiThread {
                    SnackbarHelper.errorNoInternet(this@AnyadirOEditarFragment)
                }
            }
        }
    }

    private fun asignarNotaYDificultadPrevias() {
        oVideojuegoAInsertarOEditar.nota = oVideojuegoSeleccionado.notaPersonal
        oVideojuegoAInsertarOEditar.dificultad = oVideojuegoSeleccionado.dificultadPersonal
        binding.hPickerNota.selectedItem = oVideojuegoAInsertarOEditar.nota
        binding.hPickerDificultad.selectedItem = oVideojuegoAInsertarOEditar.dificultad
    }

    private fun mostrarFechaSeleccionada(datePicker: MaterialDatePicker<Long>, eTFecha: EditText) {
        datePicker.show(childFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener {
            val fecha = Instant.ofEpochMilli(datePicker.selection!!).atZone(ZoneId.systemDefault()).toLocalDate()
            val fechaFormateada = fecha.format(DateTimeFormatter.ofPattern(Textos.FORMATO_FECHA_CON_GUIONES))
            eTFecha.setText(fechaFormateada.toString())
            oVideojuegoAInsertarOEditar.fechaDeComienzo = fecha.toString()
        }
    }

    private fun formatearHorizontalPicker(hPicker: HorizontalPicker) {
        hPicker.values = Textos.NOTAS_POSIBLES_ANYADIR_O_EDITAR
        hPicker.sideItems = 2
        hPicker.scrollBarSize = 10
    }

    fun mostrarUOcultarFechasSegunIdEstado(){
        if(oVideojuegoAInsertarOEditar.estado > 0){
            binding.eTFechaComienzo.visibility = View.VISIBLE
            binding.btnFechaComienzoHoy.visibility = View.VISIBLE
        }
        if((listaEstados.find { it.nombreEstado == Textos.NOMBRE_ESTADO_JUGADO }!!.id == oVideojuegoAInsertarOEditar.estado) || listaEstados.find { it.nombreEstado == Textos.NOMBRE_ESTADO_DROPEADO }!!.id == oVideojuegoAInsertarOEditar.estado){
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