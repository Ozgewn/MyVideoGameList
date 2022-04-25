package com.example.myvideogamelist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentMainContentBinding
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
                    navControllerMainContent.navigate(R.id.rankingFragment)
                    true
                }
                R.id.page_comunidad -> {
                    navControllerMainContent.navigate(R.id.comunidadFragment)
                    true
                }
                R.id.page_mi_lista -> {
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
    }
}