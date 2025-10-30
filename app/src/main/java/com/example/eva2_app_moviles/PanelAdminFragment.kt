package com.example.eva2_app_moviles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eva2_app_moviles.R
import com.example.eva2_app_moviles.data.AppDatabase
import com.example.eva2_app_moviles.data.Curso
import com.example.eva2_app_moviles.data.CursoDao
import kotlinx.coroutines.launch


class PanelAdminFragment : Fragment() {

    private lateinit var cursoDao: CursoDao
    private lateinit var adapter: CursoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_panel_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cursoDao = AppDatabase.getDatabase(requireContext()).cursoDao()

        adapter = CursoAdapter(listOf(),
            onEditar = { curso -> mostrarFormulario(curso) },
            onEliminar = { curso -> eliminarCurso(curso) }
        )

        val rvCursos = view.findViewById<RecyclerView>(R.id.rvCursos)
        rvCursos.adapter = adapter
        rvCursos.layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<Button>(R.id.btnAgregar).setOnClickListener {
            mostrarFormulario(null)
        }

        cargarCursos()
    }

    private fun cargarCursos() {
        lifecycleScope.launch {
            val cursos = cursoDao.obtenerTodos()
            adapter.actualizarDatos(cursos)
        }
    }

    private fun mostrarFormulario(curso: Curso?) {
        val dialogView = layoutInflater.inflate(R.layout.dialogo_curso, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
        val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcion)
        val etPrecio = dialogView.findViewById<EditText>(R.id.etPrecio)
        val etCategoria = dialogView.findViewById<EditText>(R.id.etCategoria)
        val btnGuardar = dialogView.findViewById<Button>(R.id.btnGuardar)

        if (curso != null) {
            etNombre.setText(curso.nombre)
            etDescripcion.setText(curso.descripcion)
            etPrecio.setText(curso.precio.toString())
            etCategoria.setText(curso.categoria)
        }

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val precioStr = etPrecio.text.toString().trim()
            val categoria = etCategoria.text.toString().trim()

            val categoriasValidas = listOf("Programación", "Redes", "Bases de datos")
            when {
                nombre.isEmpty() -> { etNombre.error = "Obligatorio"; return@setOnClickListener }
                descripcion.isEmpty() -> { etDescripcion.error = "Obligatorio"; return@setOnClickListener }
                precioStr.isEmpty() -> { etPrecio.error = "Obligatorio"; return@setOnClickListener }
                precioStr.toDoubleOrNull() == null || precioStr.toDouble() <= 0 -> {
                    etPrecio.error = "Debe ser un número mayor que 0"
                    return@setOnClickListener
                }
                categoria !in categoriasValidas -> {
                    etCategoria.error = "Categoría inválida"
                    return@setOnClickListener
                }
            }

            val precio = precioStr.toDouble()
            lifecycleScope.launch {
                if (curso == null) {
                    cursoDao.insertarCurso(
                        Curso(nombre = nombre, descripcion = descripcion, precio = precio, categoria = categoria)
                    )
                    Toast.makeText(requireContext(), "Curso creado", Toast.LENGTH_SHORT).show()
                } else {
                    cursoDao.actualizarCurso(
                        curso.copy(nombre = nombre, descripcion = descripcion, precio = precio, categoria = categoria)
                    )
                    Toast.makeText(requireContext(), "Curso actualizado", Toast.LENGTH_SHORT).show()
                }
                cargarCursos()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun eliminarCurso(curso: Curso) {
        lifecycleScope.launch {
            cursoDao.eliminarCurso(curso)
            Toast.makeText(requireContext(), "Curso eliminado", Toast.LENGTH_SHORT).show()
            cargarCursos()
        }
    }
}
