package org.recualberti.service

import org.recualberti.data.dao.IRecetaDAOH2
import org.recualberti.model.Receta

class RecetaService(private val recetaDAO: IRecetaDAOH2): IRecetaService {
    override fun crearReceta(receta: Receta) {
        recetaDAO.crearReceta(receta)
    }

    override fun eliminarReceta(id: Int) {
        recetaDAO.borrarReceta(id)
    }

    override fun actualizarReceta(receta: Receta) {
        recetaDAO.actualizarReceta(receta)
    }

    override fun buscarReceta(id: Int): Receta? {
        return recetaDAO.buscarReceta(id)
    }

    override fun obtenerTodas(): List<Receta> {
        return recetaDAO.obtenerTodas()
    }
}