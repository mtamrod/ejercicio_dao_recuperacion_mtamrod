package org.recualberti.app

import org.recualberti.model.Receta
import org.recualberti.service.IRecetaService
import org.recualberti.ui.IEntradaSalida

class MenuConsola(
    private val servicioReceta: IRecetaService,
    private val ui: IEntradaSalida
) {
    private var funcionando: Boolean = true

    fun iniciar() {

        while (funcionando) {
            ui.limpiarPantalla()
            ui.mostrar(
                """
                    === MENÚ DE RECETAS===
                    1. Crear
                    2. Buscar
                    3. Buscar Todas
                    4. Actualizar
                    5. Borrar
                    6. Salir
                """.trimIndent(),
                false
            )

            when (ui.leer("Opción: ", true)) {
                "1" -> crearReceta()
                "2" -> mostrarReceta()
                "3" -> mostrarTodasReceta()
                "4" -> actualizarReceta()
                "5" -> eliminarReceta()
                "6" -> salir()

                else -> ui.error("Opción no válida.", true)
            }

            //ui.pausa() //TODO
        }
    }

        private fun salir() {
            ui.mostrar("Saliendo...", false)
            funcionando = false
        }

        private fun crearReceta() {
            val id = 1 //TODO: Id generado automaticamente
            val nombre = ui.leer("Nombre: ", true)
            val calorias = ui.leer("Calorias: ", true).toInt()
            val ingredientes = ui.leerLista("Ingredientes: ", true)
            val esVegana = ui.leerBool("¿Es Vegana?: ", true)
            servicioReceta.crearReceta(Receta(id, nombre, calorias, ingredientes, esVegana))
            ui.mostrar("Usuario insertado.", true)
        }

        private fun mostrarReceta() {
            val recetaUnica = servicioReceta.mostrarReceta()
            ui.mostrar(
                "Id: ${recetaUnica.id} - NOmbre: ${recetaUnica.nombre} - Calorias: ${recetaUnica.calorias} - Ingredientes: ${recetaUnica.ingredientes} - Vegana: ${recetaUnica.esVegana}",
                true
            )
        }


    private fun mostrarTodasReceta() {
        val recetas = servicioReceta.mostrarTodasReceta()

        if (recetas.isEmpty()) ui.mostrar("No se encontraron recetas.", true)
        else recetas.forEach {
            ui.mostrar(
                "Id: ${it.id} - NOmbre: ${it.nombre} - Calorias: ${it.calorias} - Ingredientes: ${it.ingredientes} - Vegana: ${it.esVegana}",
                true
            )
        }
    }

    private fun eliminarReceta() {
        val id = ui.leer("Id de la receta a eliminar: ", false).toInt()
        servicioReceta.eliminarReceta(id)
        ui.mostrar("Receta eliminada.", false)
    }

    private fun actualizarReceta() {
        //val id = generadorId //TODO
        val nombre = ui.leer("Nombre: ", true)
        val calorias = ui.leer("Calorias: ", true).toInt()
        val ingredientes = ui.leerLista("Ingredientes: ", true)
        val esVegana = ui.leerBool("¿Vegana?: ", true)

        /*
        servicioReceta.actualizarReceta(id, nombre, calorias, ingredientes, esVegana)
        ui.mostrar("Receta Actualizada!", true)
        */

    }
}
