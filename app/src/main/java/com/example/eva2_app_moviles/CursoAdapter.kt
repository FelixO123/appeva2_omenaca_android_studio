package com.example.eva2_app_moviles.ui

import com.example.eva2_app_moviles.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.eva2_app_moviles.data.Curso

class CursoAdapter(
    private var cursos: List<Curso>,
    private val onEditar: (Curso) -> Unit,
    private val onEliminar: (Curso) -> Unit
) : RecyclerView.Adapter<CursoAdapter.CursoViewHolder>() {

    inner class CursoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvPrecio: TextView = view.findViewById(R.id.tvPrecio)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_curso, parent, false)
        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        val curso = cursos[position]
        holder.tvNombre.text = curso.nombre
        holder.tvPrecio.text = "Precio: \$${curso.precio}"
        holder.btnEditar.setOnClickListener { onEditar(curso) }
        holder.btnEliminar.setOnClickListener { onEliminar(curso) }
    }

    override fun getItemCount(): Int = cursos.size

    fun actualizarDatos(nuevosCursos: List<Curso>) {
        cursos = nuevosCursos
        notifyDataSetChanged()
    }
}