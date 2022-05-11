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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComunidadFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentComunidadBinding? = null
    private val binding get() = _binding!!
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var adapter: ComunidadAdapter
    private val listaDeUsuariosEncontrados = mutableListOf<clsUsuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        adapter = ComunidadAdapter(listaDeUsuariosEncontrados) {onUsuarioSeleccionado(it)}
        binding.rVComunidad.layoutManager = LinearLayoutManager(requireContext())
        binding.rVComunidad.adapter = adapter
        binding.sVComunidad.setOnQueryTextListener(this)
    }

    private fun onUsuarioSeleccionado(oUsuario: clsUsuario){
        usuarioViewModel.usuarioSeleccionado.postValue(oUsuario)
        navController.navigate(R.id.perfilFragment)
    }

    fun initRecyclerView(nombreUsuario: String){
        CoroutineScope(Dispatchers.IO).launch {
            usuarioViewModel.cargarUsuariosPorNombre(nombreUsuario)
            activity?.runOnUiThread {
                listaDeUsuariosEncontrados.clear()
                listaDeUsuariosEncontrados.addAll(usuarioViewModel.listaUsuariosCompleto)
                adapter.notifyDataSetChanged()
                binding.pBIndeterminada.visibility = View.GONE
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            binding.pBIndeterminada.visibility = View.VISIBLE
            initRecyclerView(query)
            hideKeyboard()
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(!newText.isNullOrEmpty() && !usuarioViewModel.listaUsuariosCompleto.isNullOrEmpty()){
            listaDeUsuariosEncontrados.clear()
            listaDeUsuariosEncontrados.addAll(usuarioViewModel.listaUsuariosCompleto.filter {
                it.nombreUsuario.lowercase().contains(newText.lowercase())
            })
            adapter.notifyDataSetChanged()
        }
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