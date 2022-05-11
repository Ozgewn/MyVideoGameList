package com.example.myvideogamelist.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.ItemComunidadBinding
import com.example.myvideogamelist.models.clsUsuario

class ComunidadAdapter(val listaDeUsuarios: List<clsUsuario>, private val listener: (clsUsuario) -> Unit): RecyclerView.Adapter<ComunidadAdapter.ComunidadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComunidadViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ComunidadViewHolder(layoutInflater.inflate(R.layout.item_comunidad, parent, false))
    }

    override fun onBindViewHolder(holder: ComunidadViewHolder, position: Int) {
        holder.completarTextos(listaDeUsuarios[position])
        holder.itemView.setOnClickListener {
            listener(listaDeUsuarios[position])
        }
    }

    override fun getItemCount(): Int = listaDeUsuarios.size

    class ComunidadViewHolder(view: View): RecyclerView.ViewHolder(view){

        val binding = ItemComunidadBinding.bind(view)

        fun completarTextos(oUsuario: clsUsuario){
            with(binding){
                tVNombreUsuario.text = oUsuario.nombreUsuario
                tVVideojuegosJugadosValue.text = oUsuario.videojuegosJugados.toString()
                tVVideojuegosJugandoValue.text = oUsuario.videojuegosJugando.toString()
                tVVideojuegosPlaneadosValue.text = oUsuario.videojuegosPlaneados.toString()
                tVVideojuegosEnPausaValue.text = oUsuario.videojuegosEnPausa.toString()
                tVVideojuegosDropeadosValue.text = oUsuario.videojuegosDropeados.toString()
            }
        }

    }

}