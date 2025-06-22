package org.recualberti

import org.recualberti.service.RecetaService
import org.recualberti.app.MenuConsola
import org.recualberti.data.dao.*
import org.recualberti.data.db.DataSourceFactory
import org.recualberti.data.db.DbStarter
import org.recualberti.ui.EntradaSalida

fun main() {
    val ds = DataSourceFactory.getDataSource()

    // Crea las tablas si no existen
    DbStarter.start(ds)

    val recetaDAO = RecetaDAOH2(ds)

    // Inicialización del controlador
    MenuConsola(
        RecetaService(recetaDAO),
        EntradaSalida()
    ).iniciar()
}