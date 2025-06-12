package org.recualberti.data.dao

import org.recualberti.model.Receta

interface IRecetaDaoH2 {
    fun crearReceta()

    fun leer(): Receta

    fun leerTodos(): MutableList<Receta>

    fun actualizar(id: Int, nombre: String, calorias: Int, ingredientes: List<String>, esVegana: Boolean)

    fun borrar(id: Int)
}