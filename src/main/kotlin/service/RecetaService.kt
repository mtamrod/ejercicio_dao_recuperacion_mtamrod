package org.recualberti.service

import org.recualberti.data.dao.IRecetaDAOH2
import org.recualberti.model.Receta

class RecetaService(private val recetaDAO: IRecetaDAOH2): IRecetaService {
    override fun crearReceta(receta: Receta) {
        recetaDAO.crearReceta()
    }

    override fun eliminarReceta(id: Int) {
        recetaDAO.borrar(id)
    }

    override fun actualizarReceta(id: Int, nombre: String, calorias: Int, ingredientes: List<String>, esVegana: Boolean) {
        recetaDAO.actualizar(id, nombre, calorias, ingredientes, esVegana)
    }

    override fun mostrarReceta(id: Int): Receta {
        return recetaDAO.leer()
    }

    override fun mostrarTodasReceta(): List<Receta> {
        return recetaDAO.leerTodos()
    }
}