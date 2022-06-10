package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentMainContentBinding
import com.example.myvideogamelist.databinding.HeaderNavigationDrawerBinding
import com.example.myvideogamelist.utils.MaterialAlertDialogHelper
import com.example.myvideogamelist.utils.SharedPreferencesManager
import com.example.myvideogamelist.viewmodels.UsuarioViewModel
import com.example.myvideogamelist.views.sharedData.SharedData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainContentFragment : Fragment() {

    private var _binding: FragmentMainContentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private lateinit var sharedPref: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = SharedPreferencesManager(context!!)
        if(sharedPref.getModoNoche()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.topAppBar)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.MaincontentFragment_nav) as NavHostFragment
        val navControllerMainContent = navHostFragment.navController

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            gestionarClickBarraNavegacion(item, navControllerMainContent)
        }

        binding.bottomNavigation.setOnItemReselectedListener {
            evitarEntrarMismoFragment(navHostFragment, navControllerMainContent, it)
        }

        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.menu.getItem(0).isChecked = false //pongo esto para evitar que se seleccione el primer elemento

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            gestionarClicksMenuHamburguesa(menuItem, navHostFragment, navControllerMainContent)
            false
        }

        binding.logout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_mainContentFragment_to_loginFragment)
        }

        val headerView: View = binding.navigationView.getHeaderView(0)

        headerView.setOnClickListener {
            irAlPerfilSiNoEsAnonimo(navControllerMainContent)
        }
        val bindingHeader: HeaderNavigationDrawerBinding = HeaderNavigationDrawerBinding.bind(headerView)
        SharedData.nombreUsuario.observe(this, Observer { //observo el nombre de usuario ya que este se rellenara cada vez que el usuario inicie sesion/abra la aplicacion, y se rellenara mediante una llamada a la API
            bindingHeader.tVNombreUsuario.text = it
        })
    }

    /**
     * Cabecera: private fun irAlPerfilSiNoEsAnonimo(navControllerMainContent: NavController)
     * Descripcion: Este metodo se encarga de navegar al perfil del usuario solo si este no ha iniciado sesion como anonimo
     * Precondiciones: El usuario debe haber iniciado sesion, se debe tener conexion a Internet
     * Postcondiciones: Se navegara al perfil del usuario si este no es anonimo
     * Entrada:
     *      navControllerMainContent: NavController -> El navController que controla el navHost que hay dentro de este Fragment
     * Salida: N/A
     */
    private fun irAlPerfilSiNoEsAnonimo(navControllerMainContent: NavController) {
        if(auth.currentUser!!.isAnonymous){
            MaterialAlertDialogHelper.errorPorSerAnonimo(this, auth)
        }else{
            usuarioViewModel.usuarioSeleccionado.postValue(null)
            navControllerMainContent.navigate(R.id.perfilFragment)
        }
        binding.drawerLayout.close()
    }

    /**
     * Cabecera: private fun gestionarClicksMenuHamburguesa(menuItem: MenuItem, navHostFragment: NavHostFragment, navControllerMainContent: NavController)
     * Descripcion: Este metodo se encarga de gestionar el click a las opciones del menu hamburguesa (en este caso solo a 1 opcion, la cual es ajustes)
     * Precondiciones: N/A
     * Postcondiciones: Se navegara al Fragment de Detalles
     * Entrada:
     *      menuItem: MenuItem -> el item del menu, en este caso lo uso para que no aparezca marcado
     *      navHostFragment: NavHostFragment -> el navHost, en este caso lo uso para que si el usuario le de varias veces a "Ajustes", solo se navegue si el
     *      usuario se encuentra en un Fragment distinto a este ultimo
     *      navControllerMainContent: NavController -> el navController que controla el navHost que hay dentro de este Fragment, en este caso lo uso
     *      para navegar al Fragment de Ajustes
     * Salida:
     */
    private fun gestionarClicksMenuHamburguesa(menuItem: MenuItem, navHostFragment: NavHostFragment, navControllerMainContent: NavController) {
        //Vamos a manejar los items seleccionados
        binding.drawerLayout.close()
        menuItem.isChecked = false
        if(!(navHostFragment.childFragmentManager.fragments.any {  it.javaClass == AjustesFragment().javaClass && it.isVisible })) {
            //If para evitar que si el usuario esta en ajustes, y ese clicka en ajustes mucha veces a traves del drawer layout, que se le guarden muchas veces la pagina de ajustes en el historial de navegacion
            navControllerMainContent.navigate(R.id.ajustesFragment)
        }
    }

    /**
     * Cabecera: private fun evitarEntrarMismoFragment(navHostFragment: NavHostFragment, navControllerMainContent: NavController, menuItem: MenuItem)
     * Descripcion: Este metodo se encarga de navegar a un fragment solo si el fragment al que se desea navegar es distinto al fragment en el que el usuario se encuentra
     * en el momento del click
     * Precondiciones: Se debe estar en un fragment distinto al que se desea navegar
     * Postcondiciones: Se navegara al Fragment en caso de que sea distinto al Fragment en el que estamos
     * Entrada:
     *      navHostFragment: NavHostFragment -> el navHost, en este caso lo uso para averiguar en que fragment esta el usuario
     *      navControllerMainContent: NavController -> el navController que controla el navHost que hay dentro de este Fragment, en este caso lo uso para
     *      navegar a los distintos fragments
     *      menuItem: MenuItem -> el item del menu, en este caso lo uso para averiguar que opcion se ha seleccionado, y navegar a dicha opcion si es distinta a
     *      la opcion en la que estamos
     * Salida:
     */
    private fun evitarEntrarMismoFragment(navHostFragment: NavHostFragment, navControllerMainContent: NavController, menuItem: MenuItem) {
        /*
        Para entender este if, hay que entender el problema:
        Si un usuario esta en "Ranking", y mientras esta en Ranking, le da muchas veces a Ranking en el menu de abajo, cargara muchas veces la lista, sobrecargando asi
        la API.
        Ademas, cuando por ejemplo estemos en Ranking y vayamos a editar un juego, la opcion de "Ranking" se quedara seleccionada porque todavia estamos en Ranking realmente, y si
        hacemos que el si el usuario reselecciona (que es lo que ocurriria en la situacion de ejemplo que acabo de exponer), no nos navegaria otra vez a Ranki
        Para evitar esos 2 problemas que he planteado, hacemos este if.
        Este if comprueba si el fragment en el que se encuentra actualmente el navHost es el de Ranking o es otro, si es otro, permitira navegar al fragment de ranking,
        y como no hay else, en caso de que estemos en Ranking y le volvamos a dar a Ranking, no pasara nada
        */
        if(!(navHostFragment.childFragmentManager.fragments.any {  it.javaClass == RankingFragment().javaClass && it.isVisible }) && menuItem.itemId == R.id.page_ranking){
            navControllerMainContent.navigate(R.id.rankingFragment)
        }
        if(!(navHostFragment.childFragmentManager.fragments.any {  it.javaClass == ComunidadFragment().javaClass && it.isVisible }) && menuItem.itemId == R.id.page_comunidad){
            navControllerMainContent.navigate(R.id.comunidadFragment)
        }
        if(!(navHostFragment.childFragmentManager.fragments.any {  it.javaClass == MiListaFragment().javaClass && it.isVisible }) && menuItem.itemId == R.id.page_mi_lista){
            if(auth.currentUser!!.isAnonymous){
                MaterialAlertDialogHelper.errorPorSerAnonimo(this, auth)
            }else{
                navControllerMainContent.navigate(R.id.miListaFragment)
            }
        }
    }

    /**
     * Cabecera: private fun gestionarClickBarraNavegacion(item: MenuItem, navControllerMainContent: NavController): Boolean
     * Descripcion: Este metodo se encarga de navegar a la opcion seleccionada, borrando el historial de las demas opciones seleccionadas anteriormente
     * Precondiciones: N/A
     * Postcondiciones: Se navegara al Fragment deseado
     * Entrada:
     *      item: MenuItem -> El item del menu, en este caso lo uso para averiguar que opcion se ha seleccionado
     *      navControllerMainContent: NavController -> el navController que controla el navHost que hay dentro de este Fragment, en este caso lo
     *      uso para navegar a los distintos fragments
     * Salida:
     *      gestionado: Boolean -> true si se ha gestionado la seleccion, false si no
     */
    private fun gestionarClickBarraNavegacion(item: MenuItem, navControllerMainContent: NavController): Boolean {
        var gestionado = false
        when(item.itemId) {
            R.id.page_ranking -> {
                /*
                Esto es para borrar los fragments del menu de abajo del historial de navegacion, el comportamiento que se obtendra es el siguiente:
                Al navegar hacia atras, iran al fragment del login, fragment el cual redirecciona al fragment del ranking (el principal), gracias a esto, no permitimos que el usuario
                le de hacia atras y navegue entre las distintas paginas del menu de abajo, esto lo prohibimos ya que imaginemos que el usuario ha estado dando muchos clicks
                a los menus de abajo, entonces por cada click, le deberia dar una vez al boton de atras. Gracias a borrar el historial de navegacion, conseguimos eso
                */
                navControllerMainContent.popBackStack(R.id.comunidadFragment, true)
                navControllerMainContent.popBackStack(R.id.miListaFragment, true)
                navControllerMainContent.navigate(R.id.rankingFragment)
                gestionado = true
            }
            R.id.page_comunidad -> {
                navControllerMainContent.popBackStack(R.id.rankingFragment, true)
                navControllerMainContent.popBackStack(R.id.miListaFragment, true)
                navControllerMainContent.navigate(R.id.comunidadFragment)
                gestionado = true
            }
            R.id.page_mi_lista -> {
                if(auth.currentUser!!.isAnonymous){
                    MaterialAlertDialogHelper.errorPorSerAnonimo(this, auth)
                }else{
                    usuarioViewModel.usuarioSeleccionado.postValue(null) //aqui le estamos diciendo al viewmodel que el usuario seleccionado es el usuario que tiene iniciada la sesion
                    navControllerMainContent.popBackStack(R.id.comunidadFragment, true)
                    navControllerMainContent.popBackStack(R.id.rankingFragment, true)
                    navControllerMainContent.navigate(R.id.miListaFragment)
                }
                gestionado = true
            }
        }
        return gestionado
    }

}