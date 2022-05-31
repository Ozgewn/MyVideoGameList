package com.example.myvideogamelist.views.fragments

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentMainContentBinding
import com.example.myvideogamelist.databinding.HeaderNavigationDrawerBinding
import com.example.myvideogamelist.utils.MaterialAlertDialogHelper
import com.example.myvideogamelist.utils.SharedPreferencesManager
import com.example.myvideogamelist.viewmodels.UsuarioViewModel
import com.example.myvideogamelist.views.sharedData.SharedData
import com.google.android.material.snackbar.Snackbar
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
                    true
                }
                R.id.page_comunidad -> {
                    navControllerMainContent.popBackStack(R.id.rankingFragment, true)
                    navControllerMainContent.popBackStack(R.id.miListaFragment, true)
                    navControllerMainContent.navigate(R.id.comunidadFragment)
                    true
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
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.setOnItemReselectedListener {
            /*
            Para entender este if, hay que entender el problema:
            Si un usuario esta en "Ranking", y mientras esta en Ranking, le da muchas veces a Ranking en el menu de abajo, cargara muchas veces la lista, sobrecargando asi
            la API.
            Ademas, cuando por ejemplo estemos en Ranking y vayamos a editar un juego, la opcion de "Ranking" se quedara seleccionada porque todavia estamos en Ranking realmente, y si
            hacemos que el si el usuario reselecciona (que es lo que ocurriria en la situacion de ejemplo que acabo de exponer), no nos navegaria otra vez a Ranking.

            Para evitar esos 2 problemas que he planteado, hacemos este if.
            Este if comprueba si el fragment en el que se encuentra actualmente el navHost es el de Ranking o es otro, si es otro, permitira navegar al fragment de ranking,
            y como no hay else, en caso de que estemos en Ranking y le volvamos a dar a Ranking, no pasara nada
             */
            if(!(navHostFragment.childFragmentManager.fragments.any {  it.javaClass == RankingFragment().javaClass && it.isVisible }) && it.itemId == R.id.page_ranking){
                navControllerMainContent.navigate(R.id.rankingFragment)
            }
            if(!(navHostFragment.childFragmentManager.fragments.any {  it.javaClass == ComunidadFragment().javaClass && it.isVisible }) && it.itemId == R.id.page_comunidad){
                navControllerMainContent.navigate(R.id.comunidadFragment)
            }
            if(!(navHostFragment.childFragmentManager.fragments.any {  it.javaClass == MiListaFragment().javaClass && it.isVisible }) && it.itemId == R.id.page_mi_lista){
                if(auth.currentUser!!.isAnonymous){
                    MaterialAlertDialogHelper.errorPorSerAnonimo(this, auth)
                }else{
                    navControllerMainContent.navigate(R.id.miListaFragment)
                }
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }
        binding.navigationView.menu.getItem(0).isChecked = false //pongo esto para evitar que se seleccione el primer elemento
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            //Vamos a manejar los items seleccionados
            binding.drawerLayout.close()
            menuItem.isChecked = false
            if(!(navHostFragment.childFragmentManager.fragments.any {  it.javaClass == AjustesFragment().javaClass && it.isVisible })) {
                //If para evitar que si el usuario esta en ajustes, y ese clicka en ajustes mucha veces a traves del drawer layout, que se le guarden muchas veces la pagina de ajustes en el historial de navegacion
                navControllerMainContent.navigate(R.id.ajustesFragment)
            }
            false
        }
        binding.logout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_mainContentFragment_to_loginFragment)
        }
        val headerView: View = binding.navigationView.getHeaderView(0)
        headerView.setOnClickListener {
            if(auth.currentUser!!.isAnonymous){
                MaterialAlertDialogHelper.errorPorSerAnonimo(this, auth)
            }else{
                usuarioViewModel.usuarioSeleccionado.postValue(null)
                navControllerMainContent.navigate(R.id.perfilFragment)
            }
            binding.drawerLayout.close()
        }
        val bindingHeader: HeaderNavigationDrawerBinding = HeaderNavigationDrawerBinding.bind(headerView)
        SharedData.nombreUsuario.observe(this, Observer { //observo el nombre de usuario ya que este se rellenara cada vez que el usuario inicie sesion/abra la aplicacion, y se rellenara mediante una llamada a la API
            bindingHeader.tVNombreUsuario.text = it
        })
    }

}