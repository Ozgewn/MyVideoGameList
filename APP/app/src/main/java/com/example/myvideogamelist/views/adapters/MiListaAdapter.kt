package com.example.myvideogamelist.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.ItemMiListaVideojuegoBinding
import com.example.myvideogamelist.models.clsListaConInfoDeVideojuego
import java.text.DecimalFormat

class MiListaAdapter(val listaConInfoDeVideojuego: List<clsListaConInfoDeVideojuego>,
                     private val listener: (clsListaConInfoDeVideojuego) -> Unit,
                     private val getNombreEstado: (clsListaConInfoDeVideojuego) -> String,
                     private val getEstadoDelUsuarioObservador:(clsListaConInfoDeVideojuego) -> Int,
                     private val listenerAnyadirOEditar: (clsListaConInfoDeVideojuego) -> Unit)
    :RecyclerView.Adapter<MiListaAdapter.MiListaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiListaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MiListaViewHolder(layoutInflater.inflate(R.layout.item_mi_lista_videojuego, parent, false), listenerAnyadirOEditar)
    }

    override fun onBindViewHolder(holder: MiListaViewHolder, position: Int) {
        holder.completarTextos(listaConInfoDeVideojuego[position], getNombreEstado(listaConInfoDeVideojuego[position]), getEstadoDelUsuarioObservador(listaConInfoDeVideojuego[position]))
        holder.itemView.setOnClickListener {
            listener(listaConInfoDeVideojuego[position])
        }
    }

    override fun getItemCount(): Int = listaConInfoDeVideojuego.size

    class MiListaViewHolder(view: View, private val listenerAnyadirOEditar: (clsListaConInfoDeVideojuego) -> Unit):RecyclerView.ViewHolder(view){
        val binding = ItemMiListaVideojuegoBinding.bind(view)

        fun completarTextos(oVideojuegoConInfo: clsListaConInfoDeVideojuego, nombreEstado: String, idEstadoObservador: Int){
            val df = DecimalFormat("#.##")
            with(binding){
                tVNombreVideojuego.text = oVideojuegoConInfo.nombreVideojuego
                Glide.with(iVImagenVideojuego.context).load(oVideojuegoConInfo.urlImagenVideojuego).into(iVImagenVideojuego)
                tVNombreEstado.text = nombreEstado
                if(oVideojuegoConInfo.dificultadMediaVideojuego > 0){
                    tVDificultadMediaVideojuego.text = df.format(oVideojuegoConInfo.dificultadMediaVideojuego)
                }else{
                    tVDificultadMediaVideojuego.text = "-"
                }
                if(oVideojuegoConInfo.notaMediaVideojuego > 0){
                    tVNotaMediaVideojuego.text = df.format(oVideojuegoConInfo.notaMediaVideojuego)
                }else{
                    tVNotaMediaVideojuego.text = "-"
                }
                if(oVideojuegoConInfo.dificultadPersonal > 0){
                    tVDificultadPersonalVideojuegoValue.text = oVideojuegoConInfo.dificultadPersonal.toString()
                }else{
                    tVDificultadPersonalVideojuegoValue.text = "-"
                }
                if(oVideojuegoConInfo.notaPersonal > 0){
                    tVNotaPersonalVideojuegoValue.text = oVideojuegoConInfo.notaPersonal.toString()
                }else{
                    tVNotaPersonalVideojuegoValue.text = "-"
                }
                if(idEstadoObservador > 0){
                    iVAnyadirOEditar.setImageResource(R.drawable.ic_edit)
                }else{
                    iVAnyadirOEditar.setImageResource(R.drawable.ic_add)
                }
                if(oVideojuegoConInfo.fechaDeComienzo.isNullOrEmpty()){
                    tVFechaComienzoValue.text = "-"
                }else{
                    tVFechaComienzoValue.text = oVideojuegoConInfo.fechaDeComienzo
                }
                if(oVideojuegoConInfo.fechaDeFinalizacion.isNullOrEmpty()){
                    tVFechaFinalizacionValue.text = "-"
                }else{
                    tVFechaFinalizacionValue.text = oVideojuegoConInfo.fechaDeFinalizacion
                }
                iVAnyadirOEditar.setOnClickListener {
                    listenerAnyadirOEditar(oVideojuegoConInfo)
                }
            }
        }

    }

}