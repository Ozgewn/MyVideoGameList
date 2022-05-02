package com.example.myvideogamelist.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.myvideogamelist.R
import com.example.myvideogamelist.databinding.FragmentAnyadirOEditarBinding
import com.example.myvideogamelist.databinding.ItemInfoVideojuegoBinding
import com.example.myvideogamelist.databinding.ListItemEstadoBinding
import com.example.myvideogamelist.models.clsEstado

class ListaEstadosAdapter(
    private val contexto: Context,
    private val viewResourceId: Int,
    private val listaEstados: List<clsEstado>
) : ArrayAdapter<clsEstado>(contexto, viewResourceId, listaEstados.toList()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v: View? = convertView
        if (v == null) {
            val vi = contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = vi.inflate(viewResourceId, null)
        }
        val estado: clsEstado? = listaEstados[position]
        if (estado != null) {
            val binding = ListItemEstadoBinding.bind(v!!)
            binding.tVNombreEstado.text = estado.nombreEstado
        }
        return v!!
    }

    override fun getCount(): Int = listaEstados.size

    override fun getItem(position: Int): clsEstado? = listaEstados[position]
}