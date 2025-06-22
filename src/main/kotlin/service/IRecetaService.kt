package org.recualberti.service

import org.recualberti.model.Receta

interface IRecetaService {
    fun crearReceta(receta: Receta)

    fun eliminarReceta(id: Int)

    fun actualizarReceta(receta: Receta)

    fun buscarReceta(id: Int): Receta?

    fun obtenerTodas(): List<Receta>
}