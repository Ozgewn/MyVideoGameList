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
import com.example.myvideogamelist.utils.SnackbarHelper
import com.example.myvideogamelist.viewmodels.UsuarioViewModel
import com.example.myvideogamelist.views.adapters.ComunidadAdapter
import com.example.myvideogamelist.views.mensajes.Mensajes
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

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

    /**
     * Cabecera: private fun onUsuarioSeleccionado(oUsuario: clsUsuario)
     * Descripcion: Este metodo se encarga de gestionar el click de un usuario, dicho click enviara al usuario al Fragment de Perfil
     * Precondiciones: La lista no puede estar vacia
     * Postcondiciones: Se navegara al Fragment de Perfil, y este mostrara los detalles del usuario seleccionado
     * Entrada:
     *      oUsuario: clsUsuario -> El usuario seleccionado
     * Salida: N/A
     */
    private fun onUsuarioSeleccionado(oUsuario: clsUsuario){
        usuarioViewModel.usuarioSeleccionado.postValue(oUsuario)
        navController.navigate(R.id.perfilFragment)
    }

    /**
     * Cabecera: private fun initRecyclerView()
     * Descripcion: Este metodo se encarga de inicializar el RecyclerView
     * Precondiciones: Ninguna
     * Postcondiciones: Se inicializara el recycler
     * Entrada: N/A
     * Salida: N/A
     */
    private fun initRecyclerView(){
        adapter = ComunidadAdapter(listaDeUsuariosFiltrada) {onUsuarioSeleccionado(it)}
        binding.rVComunidad.layoutManager = LinearLayoutManager(requireContext())
        binding.rVComunidad.adapter = adapter
    }

    /**
     * Cabecera: private fun cargarUsuarios()
     * Descripcion: Este metodo se encarga de cargar la lista de todos los usuarios
     * Precondiciones: Se necesita conexion a Internet
     * Postcondiciones: Se mostrara por pantalla una lista completa de todos los usuarios y su informacion
     * Entrada: N/A
     * Salida: N/A
     */
    private fun cargarUsuarios(){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                usuarioViewModel.cargarUsuarios()
                listaDeUsuariosCompleta = usuarioViewModel.listaUsuariosCompleto
            }catch (e: retrofit2.HttpException){
                activity?.runOnUiThread {
                    Snackbar.make(requireView(), Mensajes.errores.NO_USUARIOS_ENCONTRADOS, Snackbar.LENGTH_SHORT).show()
                }
            }catch (e: UnknownHostException){
                activity?.runOnUiThread {
                    SnackbarHelper.errorNoInternet(this@ComunidadFragment)
                }
            }
            activity?.runOnUiThread {
                listaDeUsuariosFiltrada.clear()
                listaDeUsuariosFiltrada.addAll(usuarioViewModel.listaUsuariosCompleto)
                adapter.notifyDataSetChanged()
                binding.pBIndeterminada.visibility = View.GONE
            }
        }
    }

    /**
     * Metodo que se llama automaticamente cuando hacemos "Enter" en la barra de busqueda
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    /**
     * Metodo que se llama automaticamente cada vez que el usuario escriba una letra en la barra de busqueda
     */
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

}