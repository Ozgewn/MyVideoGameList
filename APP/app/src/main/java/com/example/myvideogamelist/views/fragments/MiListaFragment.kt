package com.example.myvideogamelist.views.fragments

import android.content.Context
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentMiListaBinding
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.utils.InterfazUsuarioUtils
import com.example.myvideogamelist.utils.MaterialAlertDialogHelper
import com.example.myvideogamelist.utils.SnackbarHelper
import com.example.myvideogamelist.viewmodels.UsuarioViewModel
import com.example.myvideogamelist.viewmodels.VideojuegoViewModel
import com.example.myvideogamelist.views.adapters.MiListaAdapter
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.example.myvideogamelist.views.sharedData.SharedData
import com.example.myvideogamelist.views.textos.Textos
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException

class MiListaFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentMiListaBinding? = null
    private val binding get() = _binding!!
    private val videojuegoViewModel: VideojuegoViewModel by activityViewModels()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private lateinit var adapter: MiListaAdapter
    private val listaVideojuegosEnListaFiltrado = mutableListOf<clsListaConInfoDeVideojuego>()
    private val listaVideojuegosEnEstado = mutableListOf<clsListaConInfoDeVideojuego>()
    private var listaVideojuegosEnListaCompleta: List<clsListaConInfoDeVideojuego> = emptyList()
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
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
                mostrarVideojuegosSegunEstado(tab!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //nada
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //nada
            }

        })
        binding.sVMiLista.setOnQueryTextListener(this)
        //Ordenar
        binding.btnOrdenar.setOnClickListener {

            val opcionesDeOrdenado = Textos.OPCIONES_ORDENADO_MI_LISTA
            var opcionOrdenado = 0

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(Textos.ELIJA_OPCION_ORDENADO)
                .setNeutralButton(Textos.BOTON_CANCELAR) { dialog, which ->
                    // nada
                }
                .setPositiveButton(Textos.BOTON_OK) { dialog, which ->
                    ordenar(opcionOrdenado)
                }
                .setSingleChoiceItems(opcionesDeOrdenado, opcionOrdenado) { dialog, which ->
                    opcionOrdenado = which
                }
                .show()
        }
    }

    /**
     * Cabecera: private fun mostrarVideojuegosSegunEstado(tab: TabLayout.Tab)
     * Descripcion: Este metodo se encarga de filtrar la lista que se mostrara dependiendo de a que opcion el usuario le de en el TabLayout que se
     * observa en la parte superior de la pantalla, filtrando por estados
     * Precondiciones: Conexion a Internet, la lista no puede estar vacia
     * Postcondiciones: Se filtrara la lista
     * Entrada: N/A
     * Salida: N/A
     */
    private fun mostrarVideojuegosSegunEstado(tab: TabLayout.Tab) {
        listaVideojuegosEnEstado.clear()
        if(tab.text.toString()==Textos.TAB_TEXT_MI_LISTA_TODOS){
            listaVideojuegosEnEstado.addAll(listaVideojuegosEnListaCompleta)

        }else{
            listaVideojuegosEnEstado.addAll(listaVideojuegosEnListaCompleta.filter {
                videojuegoViewModel.listaEstados[it.estado-1].nombreEstado.lowercase() == tab.text.toString().lowercase()
            })
        }
        listaVideojuegosEnListaFiltrado.clear()
        listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnEstado)
        adapter.notifyDataSetChanged()
    }

    /**
     * Cabecera: private fun ordenar(opcionOrdenado: Int)
     * Descripcion: Este metodo se encarga de ordenar la lista segun la opcion seleccioanda
     * Precondiciones: La lista no puede estar vacia, por lo cual, se debe tener conexion a Internet
     * Postcondiciones: Se ordenara la lista segun la opcion seleccionada
     * Entrada:
     *      opcionOrdenado: Int -> la opcion por la cual se desea ordenar
     * Salida: N/A
     */
    private fun ordenar(opcionOrdenado: Int){
        listaVideojuegosEnListaFiltrado.clear()
        when(opcionOrdenado){
            0 -> { //Estado
                listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnEstado)
            }
            1 -> { //Nombre A-Z
                listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnEstado.sortedBy {
                    it.nombreVideojuego
                })
            }
            2 -> { //Nombre Z-A
                listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnEstado.sortedByDescending {
                    it.nombreVideojuego
                })
            }
            3 -> { //Nota personal
                listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnEstado.sortedByDescending {
                    it.notaPersonal
                })
            }
            4 -> { //Dificultad personal
                listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnEstado.sortedByDescending {
                    it.dificultadPersonal
                })
            }
        }
        adapter.notifyDataSetChanged()
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

    /**
     * Cabecera: private fun initRecyclerView()
     * Descripcion: Este metodo se encarga de inicializar el RecyclerView
     * Precondiciones: Ninguna
     * Postcondiciones: Se inicializara el recycler
     * Entrada: N/A
     * Salida: N/A
     */
    private fun initRecyclerView() {
        adapter = MiListaAdapter(listaVideojuegosEnListaFiltrado, {onVideojuegoSeleccionado(it)}, {getNombreEstado(it)}, {getEstadoDelUsuarioObservador(it)}, {onVideojuegoAnyadidoOEditado(it)})
        binding.rVSoloVideojuegosEnLista.layoutManager = LinearLayoutManager(requireContext())
        binding.rVSoloVideojuegosEnLista.adapter = adapter
    }

    /**
     * Cabecera: private fun onVideojuegoSeleccionado(oVideojuego: clsListaConInfoDeVideojuego)
     * Descripcion: Este metodo se encarga de gestionar el click de un videojuego, dicho click enviara al usuario al Fragment de Detalles, pero con la informacion
     * que este asociada a nosotros, y no al usuario al que le estamos viendo la lista
     * Precondiciones: La lista no puede estar vacia
     * Postcondiciones: Se navegara al Fragment de Detalles, y este mostrara los detalles del videojuego seleccionado
     * Entrada:
     *      oVideojuego: clsListaConInfoDeVideojuego -> El videojuego el cual se ha seleccionado
     * Salida: N/A
     */
    private fun onVideojuegoSeleccionado(oVideojuegoConInfo: clsListaConInfoDeVideojuego){
        /*
        Si el usuario desde el que estamos dando al juego no es el nuestro, si no desde la lista de otro usuario, debemos hacer un postValue
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

    /**
     * Cabecera: private fun getNombreEstado(oVideojuegoConInfo: clsListaConInfoDeVideojuego)
     * Descripcion: Este metodo se encarga de devolver el nombre de estado de un videojuego en la lista, para posteriormente mostrarlo por pantalla
     * Precondiciones: La lista no puede estar vacia, por lo cual, se necesita conexion a Internet
     * Postcondiciones: Se devolvera el nombre del estado del videojuego en cuestion
     * Entrada:
     *      oVideojuegoConInfo: clsListaConInfoDeVideojuego -> el videojuego del cual se desea obtener el nombre del estado
     * Salida:
     *      nombreEstado: String -> el nombre del estado del videojuego deseado
     */
    private fun getNombreEstado(oVideojuegoConInfo: clsListaConInfoDeVideojuego): String {
        var nombreEstado = ""
        if(!oVideojuegoConInfo.idUsuario.isNullOrEmpty()){
            nombreEstado = videojuegoViewModel.listaEstados[oVideojuegoConInfo.estado-1].nombreEstado
        }
        return nombreEstado
    }

    /**
     * Cabecera: private fun getEstadoDelUsuarioObservador(oVideojuegoConInfo: clsListaConInfoDeVideojuego)
     * Descripcion: Este metodo devuelve el estado del usuario que esta observando la lista.
     * Pongamos un ejemplo:
     * Supongamos que el usuario A esta viendo la lista del usuario B. El usuario B tiene en su lista el videojuego
     * "The Witcher 3", pero el usuario A no tiene ese juego en su lista.
     * Entonces, si el usuario A esta viendo la lista del usuario B, le deberia salir un "+" en el videojuego "The
     * Witcher 3".
     * Basicamente seria algo como esto:
     * idDelEstadoDelUsuarioObservador: 0
     * idDelUsuarioDueñoDeLaLista: 3 (o 4, o el que sea).
     * Pero esa es la idea de este metodo, traer el id del estado del usuario que esta observando la lista de otro usuario
     * Precondiciones: La lista del usuario no puede estar vacia
     * Postcondiciones: Se devolvera el estado del usuario que esta viendo la lista
     * Entrada:
     *      oVideojuegoConInfo: clsListaConInfoDeVideojuego -> El videojuego del cual se desea obtener el estado del usuario observador
     * Salida:
     *      idEstadoObservador: Int -> el id del estado del usuario que esta observando la lista
     */
    private fun getEstadoDelUsuarioObservador(oVideojuegoConInfo: clsListaConInfoDeVideojuego): Int{
        val idEstadoObservador = videojuegoViewModel.listaConInfoDeVideojuegosModel.find {
            it.idVideojuego == oVideojuegoConInfo.idVideojuego
        }!!.estado
        return idEstadoObservador
    }

    /**
     * Cabecera: private fun onVideojuegoAnyadidoOEditado(oVideojuego: clsListaConInfoDeVideojuego)
     * Descripcion: Este metodo se encarga de gestionar el click de añadir o editar (el click al simbolo "+" para añadir, o el click al simbolo
     * con un lapiz en un rectangulo para editar), pero con la informacion que este asociada al usuario que esta viendo la lista, y no al usuario
     * que posee dicha lista
     * Precondiciones: La lista no puede estar vacia
     * Postcondiciones: Se navegara al Fragment de AnyadirOEditar, y este permitira añadir, editar, o eliminar, segun lo desee el usuario
     * Entrada:
     *      oVideojuego: clsListaConInfoDeVideojuego -> El videojuego a añadir/editar/eliminar
     * Salida: N/A
     */
    private fun onVideojuegoAnyadidoOEditado(oVideojuego: clsListaConInfoDeVideojuego){
        if(auth.currentUser!!.isAnonymous){
            MaterialAlertDialogHelper.errorPorSerAnonimo(this, auth)
        }else{
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
    }

    /**
     * Cabecera: private fun cargarVideojuegosEnLista()
     * Descripcion: Este metodo se encarga de cargar la lista de videojuegos del usuario en cuestion
     * Precondiciones: Conexion a Internet, el usuario debe tener al menos un videojuego en su lista
     * Postcondiciones: Se mostrara la lista del usuario por pantalla
     * Entrada: N/A
     * Salida: N/A
     */
    private fun cargarVideojuegosEnLista(){
        try{
            CoroutineScope(Dispatchers.IO).launch { //iniciamos la corrutina
                /*
                Diferenciaremos si el usuario quiere ver su propia lista de la lista de otro usuario mediante
                el value del MutableLiveData del usuarioViewModel, si es null, significa que el usuario quiere ver
                su propia lista, si no es null, es que el usuario quiere ver la lista de otro usuario
                 */
                if(usuarioViewModel.usuarioSeleccionado.value == null){
                    try{
                        videojuegoViewModel.cargarSoloVideojuegosEnListaDelUsuario(SharedData.idUsuario) //el onCreate le asigna el valor a listaConInfoDeVideojuegosModel de lo que devuelva la API
                    }catch (e: retrofit2.HttpException){
                        /*
                        Para entender este clear pongamos el siguiente ejemplo:
                        El usuario con nombre Pepito esta en su cuenta nueva, y no tiene videojuegos en su lista, pero Pepito va a ver la lista del usuario con
                        nombre Juanito, al ver la lista de Juanito, el viewModel tendra los valores de la lista de Juanito, por lo cual, si Pepito va a ver su propia
                        lista, le apareceran los videojuegos de la lista de Juanito, porque ha saltado excepcion y el metodo de cargarSoloVideojuegosEnListaDelUsuario()
                        solo asigna el valor a la lista del viewmodel en caso de que el resultado no sea nulo ni este vacio, y en este caso, la lista que recibimos
                        del usuario Pepito esta vacia, con lo cual, el viewmodel se quedaria con los datos de Juanito
                         */
                        videojuegoViewModel.listaDeVideojuegosSoloEnLista.clear()
                        Snackbar.make(requireView(), Mensajes.informacion.NO_VIDEOJUEGOS_EN_LISTA, Snackbar.LENGTH_SHORT).show()
                        activity?.runOnUiThread {
                            adapter.notifyDataSetChanged()
                        }
                    }catch (e: UnknownHostException){
                        videojuegoViewModel.listaDeVideojuegosSoloEnLista.clear()
                        activity?.runOnUiThread {
                            SnackbarHelper.errorNoInternet(this@MiListaFragment)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }else{
                    try{
                        videojuegoViewModel.cargarSoloVideojuegosEnListaDelUsuario(usuarioViewModel.usuarioSeleccionado.value!!.id)
                    }catch (e: retrofit2.HttpException){
                        videojuegoViewModel.listaDeVideojuegosSoloEnLista.clear()
                        Snackbar.make(requireView(), Mensajes.informacion.NO_VIDEOJUEGOS_EN_LISTA, Snackbar.LENGTH_SHORT).show()
                        activity?.runOnUiThread {
                            adapter.notifyDataSetChanged()
                        }
                    }catch (e: UnknownHostException){
                        videojuegoViewModel.listaDeVideojuegosSoloEnLista.clear()
                        activity?.runOnUiThread {
                            SnackbarHelper.errorNoInternet(this@MiListaFragment)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                listaVideojuegosEnListaCompleta = videojuegoViewModel.listaDeVideojuegosSoloEnLista //le asignamos valor a la lista completa
                if(videojuegoViewModel.listaEstados.isNullOrEmpty()){
                    //aunque lo comprobemos ya en el viewmodel, hacemos doble comprobacion por si acaso, y asi no hacemos la llamada para "nada"
                    try{
                        videojuegoViewModel.cargarListaEstados()
                    }catch (e: UnknownHostException){
                        activity?.runOnUiThread {
                            SnackbarHelper.errorNoInternet(this@MiListaFragment)
                        }
                    }
                }
                activity?.runOnUiThread{
                    actualizarListas()
                }
            }
        }catch (e: Exception) {
            Snackbar.make(
                requireView(),
                Mensajes.errores.CONEXION_INTERNET_FALLIDA,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Cabecera: private fun actualizarListas()
     * Descripcion: Este metodo se encarga de simplemente actualizar la lista y actualizar la vista a su vez
     * Precondiciones: N/A
     * Postcondiciones: Se actualizara la lista y se mostraran los cambios por pantalla
     * Entrada: N/A
     * Salida: N/A
     */
    private fun actualizarListas() {
        listaVideojuegosEnListaFiltrado.clear()
        listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnListaCompleta)//añadimos lo de la lista completa a la lista que vamos a ofrecer
        listaVideojuegosEnEstado.clear()
        listaVideojuegosEnEstado.addAll(listaVideojuegosEnListaCompleta)//añadimos lo de la lista a la lista que tiene filtrado SOLO los estados
        binding.pBIndeterminada.visibility = View.GONE //quitamos el progress bar(el circulo que indica que esta cargando)
        adapter.notifyDataSetChanged() //avisamos el adapter que hemos modificado la lista
    }

    /**
     * Metodo que se llama automaticamente cuando hacemos "Enter" en la barra de busqueda
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        InterfazUsuarioUtils.hideKeyboard(binding.root, this)
        return true
    }

    /**
     * Metodo que se llama automaticamente cada vez que el usuario escriba una letra en la barra de busqueda
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        listaVideojuegosEnListaFiltrado.clear()
        if(!newText.isNullOrEmpty()){
            listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnEstado.filter {
                it.nombreVideojuego.lowercase().contains(newText.lowercase())
            })
        }else{
            listaVideojuegosEnListaFiltrado.addAll(listaVideojuegosEnEstado)
        }
        adapter.notifyDataSetChanged()
        return true
    }

}