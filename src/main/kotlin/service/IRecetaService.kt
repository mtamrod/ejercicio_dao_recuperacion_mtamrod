package org.recualberti.service

import org.recualberti.model.Receta

interface IRecetaService {
    fun crearReceta(receta: Receta)
    fun eliminarReceta(id: Int)
    fun actualizarReceta()
    fun mostrarReceta(): List<Receta>
    fun mostrarTodasReceta(): List<Receta>
}