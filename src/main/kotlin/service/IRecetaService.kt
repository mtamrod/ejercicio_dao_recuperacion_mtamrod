package org.recualberti.service

import org.recualberti.model.Receta

interface IRecetaService {
    fun crearReceta(receta: Receta)
    fun eliminarReceta(id: Int)
    fun actualizarReceta(id: Int, nombre: String, calorias: Int, ingredientes: List<String>, esVegana: Boolean)
    fun mostrarReceta(): Receta
    fun mostrarTodasReceta(): List<Receta>
}