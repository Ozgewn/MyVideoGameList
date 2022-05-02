package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentMainContentBinding
import com.example.myvideogamelist.databinding.HeaderNavigationDrawerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainContentFragment : Fragment() {

    private var _binding: FragmentMainContentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
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
                    navControllerMainContent.popBackStack(R.id.comunidadFragment, true)
                    navControllerMainContent.popBackStack(R.id.rankingFragment, true)
                    navControllerMainContent.navigate(R.id.miListaFragment)
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.setOnItemReselectedListener {
            //nada, ya que si el usuario clicka "Ranking" muchas veces, no queremos que vuelva a cargar muchas veces, simplemente que ignore el click del usuario, por eso,
            //no hacemos nada
        }
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            //Vamos a manejar los items seleccionados
            menuItem.isChecked = false
            binding.drawerLayout.close()
            //TODO: Navegar al fragment de detalles
            true
        }
        binding.logout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_mainContentFragment_to_loginFragment)
        }
        val headerView: View = binding.navigationView.getHeaderView(0)
        headerView.setOnClickListener {
            Toast.makeText(requireContext(), "Esto te llevaria al perfil...", Toast.LENGTH_SHORT).show()
            //TODO: HACER ESTO
        }
        val bindingHeader: HeaderNavigationDrawerBinding = HeaderNavigationDrawerBinding.bind(headerView)
        bindingHeader.tVNombreUsuario.text="asgewsphpowsa" //TODO: poner nombre usuario aqui
    }

}