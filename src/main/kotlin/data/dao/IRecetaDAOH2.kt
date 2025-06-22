package org.recualberti.data.dao

import org.recualberti.model.Receta

interface IRecetaDAOH2 {
    fun crearReceta(receta: Receta)

    fun buscarReceta(id: Int): Receta?

    fun obtenerTodas(): MutableList<Receta>

    fun actualizarReceta(receta: Receta)

    fun borrarReceta(id: Int)
}