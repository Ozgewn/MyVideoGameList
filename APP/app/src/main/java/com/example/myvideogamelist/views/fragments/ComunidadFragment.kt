package com.example.myvideogamelist.views.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentComunidadBinding
import com.example.myvideogamelist.models.clsUsuario
import com.example.myvideogamelist.viewmodels.UsuarioViewModel
import com.example.myvideogamelist.views.adapters.ComunidadAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComunidadFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentComunidadBinding? = null
    private val binding get() = _binding!!
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var adapter: ComunidadAdapter
    private var listaDeUsuariosCompleta: List<clsUsuario> = emptyList()
    private val listaDeUsuariosFiltrada = mutableListOf<clsUsuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(usuarioViewModel.listaUsuariosCompleto.isNullOrEmpty()){
            cargarUsuarios()
        }else{
            listaDeUsuariosCompleta = usuarioViewModel.listaUsuariosCompleto
            listaDeUsuariosFiltrada.clear()
            listaDeUsuariosFiltrada.addAll(listaDeUsuariosCompleta)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComunidadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        initRecyclerView()
        binding.sVComunidad.setOnQueryTextListener(this)
    }

    override fun onResume() {
        super.onResume()
        if(!listaDeUsuariosFiltrada.isNullOrEmpty()){
            binding.pBIndeterminada.visibility = View.GONE
        }
    }

    private fun onUsuarioSeleccionado(oUsuario: clsUsuario){
        usuarioViewModel.usuarioSeleccionado.postValue(oUsuario)
        navController.navigate(R.id.perfilFragment)
    }

    fun initRecyclerView(){
        adapter = ComunidadAdapter(listaDeUsuariosFiltrada) {onUsuarioSeleccionado(it)}
        binding.rVComunidad.layoutManager = LinearLayoutManager(requireContext())
        binding.rVComunidad.adapter = adapter
    }

    fun cargarUsuarios(){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                usuarioViewModel.cargarUsuarios()
                listaDeUsuariosCompleta = usuarioViewModel.listaUsuariosCompleto
            }catch (e: retrofit2.HttpException){
                Snackbar.make(requireView(), "No se han encontrado usuarios", Snackbar.LENGTH_SHORT).show()
            }
            activity?.runOnUiThread {
                listaDeUsuariosFiltrada.clear()
                listaDeUsuariosFiltrada.addAll(usuarioViewModel.listaUsuariosCompleto)
                adapter.notifyDataSetChanged()
                binding.pBIndeterminada.visibility = View.GONE
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(!newText.isNullOrEmpty() && !usuarioViewModel.listaUsuariosCompleto.isNullOrEmpty()){
            listaDeUsuariosFiltrada.clear()
            listaDeUsuariosFiltrada.addAll(listaDeUsuariosCompleta.filter {
                it.nombreUsuario.lowercase().contains(newText.lowercase())
            })
        }else{
            listaDeUsuariosFiltrada.clear()
            listaDeUsuariosFiltrada.addAll(listaDeUsuariosCompleta)
        }
        adapter.notifyDataSetChanged()
        return true
    }

    /**
     * Oculta el teclado, sin mas, no hay que profundizar mucho en esto
     */
    //TODO: METER ESTE METODO EN CLASE DE UTILIDADES
    private fun hideKeyboard(){
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

}