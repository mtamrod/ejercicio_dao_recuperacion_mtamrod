package org.recualberti.service

import org.recualberti.data.dao.IRecetaDaoH2
import org.recualberti.model.Receta

class RecetaService(private val recetaDAO: IRecetaDaoH2): IRecetaService {
    override fun crearReceta(receta: Receta) {
        recetaDAO.crearReceta()
    }

    override fun eliminarReceta(id: Int) {
        recetaDAO.borrar(id)
    }

    override fun actualizarReceta(id: Int, nombre: String, calorias: Int, ingredientes: List<String>, esVegana: Boolean) {
        recetaDAO.actualizar(id, nombre, calorias, ingredientes, esVegana)
    }

    override fun mostrarReceta(): Receta {
        return recetaDAO.leer()
    }

    override fun mostrarTodasReceta(): List<Receta> {
        return recetaDAO.leerTodos()
    }
}