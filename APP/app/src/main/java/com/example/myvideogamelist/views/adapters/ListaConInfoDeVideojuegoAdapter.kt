package com.example.myvideogamelist.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.ItemInfoVideojuegoBinding
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import com.example.myvideogamelist.viewmodels.VideojuegoViewModel
import java.text.DecimalFormat

class ListaConInfoDeVideojuegoAdapter(val listaConInfoDeVideojuego: List<clsListaConInfoDeVideojuego>, private val listener: (clsListaConInfoDeVideojuego) -> Unit, private val listenerAnyadidoOEditado: (clsListaConInfoDeVideojuego) -> Unit):RecyclerView.Adapter<ListaConInfoDeVideojuegoAdapter.ListaConInfoDeVideojuegoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListaConInfoDeVideojuegoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListaConInfoDeVideojuegoViewHolder(layoutInflater.inflate(R.layout.item_info_videojuego, parent, false), listenerAnyadidoOEditado)
    }

    override fun onBindViewHolder(holder: ListaConInfoDeVideojuegoViewHolder, position: Int) {
        holder.completarTextos(listaConInfoDeVideojuego[position])
        holder.itemView.setOnClickListener { listener(listaConInfoDeVideojuego[position]) }
    }

    override fun getItemCount(): Int = listaConInfoDeVideojuego.size

    class ListaConInfoDeVideojuegoViewHolder(view: View, private val listenerAnyadidoOEditado: (clsListaConInfoDeVideojuego) -> Unit):RecyclerView.ViewHolder(view){

        val binding = ItemInfoVideojuegoBinding.bind(view)

        fun completarTextos(oVideojuegoConInfo: clsListaConInfoDeVideojuego){
            val df = DecimalFormat("#.##")
            binding.tVNombreVideojuego.text = oVideojuegoConInfo.nombreVideojuego
            Glide.with(binding.iVImagenVideojuego.context).load(oVideojuegoConInfo.urlImagenVideojuego).into(binding.iVImagenVideojuego)
            binding.tVGenerosVideojuego.text = oVideojuegoConInfo.generos
            if(oVideojuegoConInfo.notaMediaVideojuego > 0){ //podria hacer esto con operadores ternarios, pero lo dejo asi para mayor claridad
                binding.tVNotaMediaVideojuego.text = df.format(oVideojuegoConInfo.notaMediaVideojuego)
            }else{ //Si la nota es 0, significa que nadie lo ha puntuado, porque la nota media no puede ser menor de 1, asi que, si la nota es 0 significa que nadie lo ha valorado, entonces, no ponemos 0, ponemos un "-"
                binding.tVNotaMediaVideojuego.text = "-"
            }
            if(oVideojuegoConInfo.notaPersonal > 0){
                binding.tVNotaPersonalVideojuego.text = df.format(oVideojuegoConInfo.notaPersonal)
            }else{ //Si la nota es 0, significa que el usuario todavia no ha puntuado ese juego, porque no puede puntuarlo con menos de 1, por lo tanto, si la nota es 0, ponemos un "-"
                binding.tVNotaPersonalVideojuego.text = "-"
            }
            if(oVideojuegoConInfo.estado == 0){ //Si el usuario no tiene el videojuego en su lista...
                binding.iVAnyadirOEditar.setImageResource(R.drawable.ic_add) //pues ponemos el icono de añadir (el "+")
            }else{//en cambio, si el usuario SI tiene el juego en su lista...
                binding.iVAnyadirOEditar.setImageResource(R.drawable.ic_edit) //pues ponemos el icono de editar
            }
            binding.iVAnyadirOEditar.setOnClickListener {
                listenerAnyadidoOEditado(oVideojuegoConInfo)
            }
        }

    }

}