package org.recualberti

import org.recualberti.service.RecetaService
import org.recualberti.app.MenuConsola
import org.recualberti.data.dao.*
import org.recualberti.data.db.DataSourceFactory
import org.recualberti.data.db.DbStarter
import org.recualberti.ui.EntradaSalida

fun main() {
    val ds = DataSourceFactory.getDataSource()

    // Crea las tablas si no exiten
    DbStarter.start(ds)

    val recetaDAO = RecetaDaoH2(ds)

    // Configuración de servicios
    val servicioReceta = RecetaService(recetaDAO)

    // Inicialización del controlador
    MenuConsola(
        RecetaService(recetaDAO),
        EntradaSalida()
    ).iniciar()
}