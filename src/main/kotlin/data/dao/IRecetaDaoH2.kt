package org.recualberti.data.dao

import org.recualberti.model.Receta

interface IRecetaDaoH2 {
    fun crearReceta()

    fun leer(id: Int): Receta

    fun leerTodos(): MutableList<Receta>

    fun actualizar()

    fun borrar(id: Int)
}