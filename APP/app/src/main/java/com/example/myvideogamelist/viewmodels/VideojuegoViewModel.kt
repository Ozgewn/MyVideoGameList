package com.example.myvideogamelist.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myvideogamelist.api.repositories.clsEstadoRepository
import com.example.myvideogamelist.api.repositories.clsListaConInfoDeVideojuegoRepository
import com.example.myvideogamelist.api.repositories.clsListaVideojuegoRepository
import com.example.myvideogamelist.models.clsEstado
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.models.clsListaVideojuego
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideojuegoViewModel: ViewModel() {

    val listaConInfoDeVideojuegosModel = mutableListOf<clsListaConInfoDeVideojuego>()
    val listaDeVideojuegosSoloEnLista = mutableListOf<clsListaConInfoDeVideojuego>()
    val listaEstados = mutableListOf<clsEstado>()
    private val repository = clsListaConInfoDeVideojuegoRepository()
    private val repositoryEstados = clsEstadoRepository()
    private val repositoryLista = clsListaVideojuegoRepository()
    val videojuegoSeleccionado = MutableLiveData<clsListaConInfoDeVideojuego>()

    /**
     * Cabecera: suspend fun cargarListaConInfoDeVideojuego(idUsuario: String)
     * Descripcion: Este metodo obtendra la lista de videojuegos con su info dependiendo del usuario, con una corrutina
     * Precondiciones: Se debe tener conexion a Internet
     * Postcondiciones: Se cargara la informacion de la lista de videojuegos
     * Entrada:
     *      idUsuario: String -> el id del usuario del que se desea obtener la lista de videojuegos con la informacion asociada
     * Salida: N/A
     */
    suspend fun cargarListaConInfoDeVideojuego(idUsuario: String){
        withContext(Dispatchers.IO){
            val result = repository.getListaVideojuegosPorUsuario(idUsuario)
            if(!result.isNullOrEmpty()){
                listaConInfoDeVideojuegosModel.removeAll(listaConInfoDeVideojuegosModel)
                result.forEach {
                    it.fechaDeComienzo = it.fechaDeComienzo?.substring(0, 10)
                    it.fechaDeFinalizacion = it.fechaDeFinalizacion?.substring(0, 10)
                }
                listaConInfoDeVideojuegosModel.addAll(result)
            }
        }
    }

    /**
     * Cabecera: suspend fun cargarSoloVideojuegosEnListaDelUsuario(idUSuario: String)
     * Descripcion: Este metodo obtendra una lista de los videojuegos que ESTEN en la lista del usuario en cuestion
     * Precondiciones: El usuario debe tener juegos en su lista, se debe tener conexion a Internet
     * Postcondiciones: Se cargara una lista con los videojuegos que SOLO ESTEN en la lista del usuario, y la informacion asociada a dicho usuario
     * Entrada:
     *      idUsuario: String -> el id del usuario del que se desea obtener la lista
     * Salida:
     */
    suspend fun cargarSoloVideojuegosEnListaDelUsuario(idUsuario: String){
        withContext(Dispatchers.IO){
            val result = repository.getSoloVideojuegosEnListaDeUsuario(idUsuario)
            if(!result.isNullOrEmpty()){
                listaDeVideojuegosSoloEnLista.removeAll(listaDeVideojuegosSoloEnLista)
                result.forEach {
                    it.fechaDeComienzo = it.fechaDeComienzo?.substring(0, 10)
                    it.fechaDeFinalizacion = it.fechaDeFinalizacion?.substring(0, 10)
                }
                listaDeVideojuegosSoloEnLista.addAll(result)
            }
        }
    }

    /**
     * Cabecera: suspend fun cargarListaEstados()
     * Descripcion: Este metodo obtendra la lista de estados con una corrutina
     * Precondiciones:  Se debe tener conexion a Internet
     * Postcondiciones: Se cargara una lista completa de estados
     * Entrada: N/A
     * Salida: N/A
     */
    suspend fun cargarListaEstados(){
        if(listaEstados.isNullOrEmpty()){
            withContext(Dispatchers.IO){
                val result = repositoryEstados.getListaEstados()
                if(!result.isNullOrEmpty()){
                    listaEstados.removeAll(listaEstados)
                    listaEstados.addAll(result)
                }
            }
        }
    }

    /**
     * Cabecera: suspend fun insertarVideojuegoEnLista(oVideojuegoEnLista: clsListaVideojuego): Int
     * Descripcion: Se insertara el videojuego en cuestion en la lista del usuario en cuestion
     * Precondiciones: El videojuego y el usuario deben existir previamente, se necesita conexion a Internet
     * Postcondiciones: Se insertara el videojuego en la lista del usuario
     * Entrada:
     *      oVideojuegoEnLista: clsListaVideojuego -> El videojuego a insertar
     * Salida:
     *      Int -> el numero de filas insertadas
     */
    suspend fun insertarVideojuegoEnLista(oVideojuegoEnLista: clsListaVideojuego): Int = repositoryLista.insertarVideojuegoEnLista(oVideojuegoEnLista)

    /**
     * Cabecera: suspend fun editarVideojuegoEnLista(oVideojuegoEnLista: clsListaVideojuego): Int
     * Descripcion: Se editara el videojuego en cuestion en la lista del usuario en cuestion
     * Precondiciones: El videojuego y el usuario deben existir previamente, se necesita conexion a Internet
     * Postcondiciones: Se editara el videojuego en la lista del usuario
     * Entrada:
     *      oVideojuegoEnLista: clsListaVideojuego -> El videojuego a editar
     * Salida:
     *      Int -> el numero de filas editadas
     */
    suspend fun editarVideojuegoEnLista(oVideojuegoEnLista: clsListaVideojuego): Int = repositoryLista.editarVideojuegoEnLista(oVideojuegoEnLista)

    /**
     * Cabecera: suspend fun eliminarVideojuegoEnLista(idUsuario: String, idVideojuego: Int): Int
     * Descripcion: Se borrara el videojuego en cuestion en la lista del usuario en cuestion
     * Precondiciones: El videojuego y el usuario deben existir previamente, se necesita conexion a Internet
     * Postcondiciones: Se borrara el videojuego en la lista del usuario
     * Entrada:
     *      idUsuario: String -> El id del usuario del que se desea borrar el videojuego
     *      idVideojuego: Int -> el id del videojuego del que se desea borrar de la lista
     * Salida:
     *      Int -> el numero de filas borradas
     */
    suspend fun eliminarVideojuegoEnLista(idUsuario: String, idVideojuego: Int): Int = repositoryLista.eliminarVideojuegoEnLista(idUsuario, idVideojuego)
}
