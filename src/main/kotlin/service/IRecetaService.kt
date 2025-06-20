package org.recualberti.service

import org.recualberti.model.Receta

interface IRecetaService {
    fun crearReceta(receta: Receta)
    fun eliminarReceta(id: Int)
    fun actualizarReceta(receta: Receta)
    fun buscarReceta(nombre: String): Receta
    fun obtenerTodas(): List<Receta>
}