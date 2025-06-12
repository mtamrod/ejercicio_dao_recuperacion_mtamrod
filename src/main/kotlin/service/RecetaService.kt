package org.recualberti.service

import org.recualberti.data.dao.IRecetaDaoH2
import org.recualberti.model.Receta

class RecetaService(private val recetaDAO: IRecetaDaoH2): IRecetaService {
    override fun crearReceta(receta: Receta) {
        TODO("Not yet implemented")
    }

    override fun eliminarReceta(id: Int) {
        TODO("Not yet implemented")
    }

    override fun actualizarReceta() {
        TODO("Not yet implemented")
    }

    override fun mostrarReceta(): List<Receta> {
        TODO("Not yet implemented")
    }

    override fun mostrarTodasReceta(): List<Receta> {
        TODO("Not yet implemented")
    }


}