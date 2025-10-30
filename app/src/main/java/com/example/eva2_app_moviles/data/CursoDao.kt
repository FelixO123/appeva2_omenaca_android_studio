package com.example.eva2_app_moviles.data

import androidx.room.*
import com.example.eva2_app_moviles.data.Curso

@Dao
interface CursoDao {

    @Insert
    suspend fun insertarCurso(curso: Curso)

    @Update
    suspend fun actualizarCurso(curso: Curso)

    @Delete
    suspend fun eliminarCurso(curso: Curso)

    @Query("SELECT * FROM cursos")
    suspend fun obtenerTodos(): List<Curso>

    @Query("SELECT * FROM cursos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Curso?
}